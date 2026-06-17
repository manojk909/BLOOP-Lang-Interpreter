package com.bloop.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Thread-local routing layer for System.out / System.err.
 *
 * <p>Once {@link #install()} is called (at JVM startup, before Spring Boot
 * initialises its own logging), all writes to System.out or System.err are
 * transparently forwarded to whichever {@link ByteArrayOutputStream} was
 * registered for the <em>current thread</em> via {@link #startCapture}.
 * Threads that have no capture registered (e.g. Spring/Logback internals)
 * fall back to the original streams saved before installation.
 *
 * <p>This design means:
 * <ul>
 *   <li>Multiple BLOOP executions running on different threads never
 *       mix their output.</li>
 *   <li>Spring Boot's own logging is completely unaffected.</li>
 *   <li>No global locks are needed for thread safety.</li>
 * </ul>
 */
public final class OutputCapture {

    private static volatile boolean installed = false;

    /** Saved references to the real streams before we intercept them. */
    private static PrintStream ORIGINAL_OUT;
    private static PrintStream ORIGINAL_ERR;

    /** Per-thread capture streams. Null means "not capturing on this thread". */
    private static final ThreadLocal<OutputStream> threadOut = new ThreadLocal<>();
    private static final ThreadLocal<OutputStream> threadErr = new ThreadLocal<>();

    private OutputCapture() {}

    /**
     * Replace System.out and System.err with thread-routing wrappers.
     * Must be called exactly once, before any code writes to System.out/err.
     */
    public static synchronized void install() {
        if (installed) return;

        ORIGINAL_OUT = System.out;
        ORIGINAL_ERR = System.err;

        System.setOut(buildRoutingStream(threadOut, ORIGINAL_OUT));
        System.setErr(buildRoutingStream(threadErr, ORIGINAL_ERR));

        installed = true;
    }

    /**
     * Register capture buffers for the calling thread.
     * All subsequent System.out / System.err writes on this thread will
     * go into these buffers until {@link #stopCapture()} is called.
     */
    public static void startCapture(ByteArrayOutputStream out,
                                    ByteArrayOutputStream err) {
        threadOut.set(out);
        threadErr.set(err);
    }

    /**
     * Deregister capture for the calling thread.
     * After this call, any System.out/err writes on this thread fall back
     * to the original streams (i.e. normal console output).
     */
    public static void stopCapture() {
        threadOut.remove();
        threadErr.remove();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static PrintStream buildRoutingStream(
            ThreadLocal<OutputStream> local, PrintStream fallback) {

        OutputStream router = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                OutputStream target = local.get();
                if (target != null) target.write(b);
                else                fallback.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                OutputStream target = local.get();
                if (target != null) target.write(b, off, len);
                else                fallback.write(b, off, len);
            }

            @Override
            public void flush() throws IOException {
                OutputStream target = local.get();
                if (target != null) target.flush();
                else                fallback.flush();
            }
        };

        return new PrintStream(router, /*autoFlush=*/true);
    }
}
