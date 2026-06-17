package com.bloop.service;

import com.bloop.dto.RunResponse;
import interpreter.Interpreter;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Executes BLOOP source code in an isolated thread with a hard 5-second
 * timeout and a 10,000-character output cap.
 *
 * <p>Each call to {@link #run} spins up a fresh single-thread executor so
 * that abandoned (timed-out) threads never block subsequent requests.
 * The {@link com.bloop.util.OutputCapture} thread-local routing installed at
 * startup ensures that concurrent executions never mix their output.
 */
@Service
public class BloopService {

    private static final int    TIMEOUT_SECONDS  = 5;
    private static final int    MAX_OUTPUT_CHARS = 10_000;
    private static final String TIMEOUT_MESSAGE  =
            "Runtime error: execution timed out after 5 seconds.";

    public RunResponse run(String code) {
        long start = System.currentTimeMillis();

        // Validate input
        if (code == null || code.isBlank()) {
            return new RunResponse("", "Error: no code provided.",
                                   false, 0L);
        }

        // Each request gets its own single-thread executor so a timed-out
        // (potentially still-running) task never blocks the next request.
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Interpreter.RunResult> future = executor.submit(() -> {
            Interpreter interp = new Interpreter();
            return interp.runAndCapture(code);
        });

        Interpreter.RunResult result = null;
        try {
            result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            long elapsed = System.currentTimeMillis() - start;
            return new RunResponse("", TIMEOUT_MESSAGE, false, elapsed);
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - start;
            String msg = e.getCause() != null
                    ? e.getCause().getMessage()
                    : e.getMessage();
            return new RunResponse("", "Internal error: " + msg, false, elapsed);
        } finally {
            executor.shutdownNow();
        }

        long elapsed = System.currentTimeMillis() - start;

        // Apply output cap
        String stdout = result.stdout;
        if (stdout.length() > MAX_OUTPUT_CHARS) {
            stdout = stdout.substring(0, MAX_OUTPUT_CHARS)
                     + "\n[output truncated at 10,000 characters]";
        }

        String  stderr  = result.stderr.trim();
        boolean success = result.success && stderr.isEmpty();

        return new RunResponse(stdout, stderr, success, elapsed);
    }
}
