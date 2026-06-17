package interpreter;

import Instructions.*;
import com.bloop.util.OutputCapture;
import environment.Environment;
import tokens.Token;
import tokens.Tokenizer;
import parser.Parser;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Orchestrates the BLOOP pipeline: Tokenizer → Parser → execution.
 *
 * <p>Two entry points are provided:
 * <ol>
 *   <li>{@link #run(String)} — original CLI behaviour (writes to real
 *       System.out/err, throws on error instead of calling System.exit so
 *       the Spring Boot JVM is never killed).</li>
 *   <li>{@link #runAndCapture(String)} — web-API version: uses the
 *       {@link OutputCapture} thread-local routing to capture all output,
 *       returns a {@link RunResult} instead of printing, and never
 *       calls System.exit.</li>
 * </ol>
 *
 * <p><strong>Existing interpreter classes are entirely unchanged.</strong>
 * Only this orchestration class is modified.
 */
public class Interpreter {

    // ── CLI entry point ───────────────────────────────────────────────────────

    /**
     * Run BLOOP source code the original way (stdout/stderr go to the
     * console).  System.exit(1) is replaced with a RuntimeException so
     * this method is safe to call from inside the Spring Boot JVM without
     * killing the process.
     */
    public void run(String sourceCode) {
        try {
            Tokenizer         tokenizer    = new Tokenizer(sourceCode);
            List<Token>       tokens       = tokenizer.tokenize();
            Parser            parser       = new Parser(tokens);
            List<Instruction> instructions = parser.parse();
            Environment       env          = new Environment();

            for (Instruction instruction : instructions) {
                instruction.execute(env);
            }
        } catch (RuntimeException e) {
            System.err.println("BLOOP Error: " + e.getMessage());
            // Replaced System.exit(1) with a throw so the JVM is not killed.
            throw new RuntimeException("BLOOP execution failed: " + e.getMessage(), e);
        }
    }

    // ── Web-API entry point ───────────────────────────────────────────────────

    /**
     * Run BLOOP source code with all output captured to in-memory buffers.
     *
     * <p>Uses {@link OutputCapture} — a thread-local routing layer installed
     * once at JVM startup — so concurrent web requests never mix their output
     * and Spring Boot's own logging is unaffected.
     *
     * @param sourceCode  raw BLOOP source code string
     * @return a {@link RunResult} containing stdout, stderr, and success flag
     */
    public RunResult runAndCapture(String sourceCode) {
        ByteArrayOutputStream stdoutBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream stderrBuf = new ByteArrayOutputStream();

        OutputCapture.startCapture(stdoutBuf, stderrBuf);
        boolean success = true;

        try {
            Tokenizer         tokenizer    = new Tokenizer(sourceCode);
            List<Token>       tokens       = tokenizer.tokenize();
            Parser            parser       = new Parser(tokens);
            List<Instruction> instructions = parser.parse();
            Environment       env          = new Environment();

            for (Instruction instruction : instructions) {
                instruction.execute(env);
            }
        } catch (RuntimeException e) {
            // Write error to the capture buffer (goes via OutputCapture routing)
            System.err.println("BLOOP Error: " + e.getMessage());
            success = false;
        } finally {
            // Always deregister capture so this thread returns to normal behaviour
            OutputCapture.stopCapture();
        }

        return new RunResult(
                stdoutBuf.toString(),
                stderrBuf.toString(),
                success
        );
    }

    // ── Result holder ─────────────────────────────────────────────────────────

    /**
     * Immutable result from {@link #runAndCapture(String)}.
     */
    public static final class RunResult {
        public final String  stdout;
        public final String  stderr;
        public final boolean success;

        public RunResult(String stdout, String stderr, boolean success) {
            this.stdout  = stdout;
            this.stderr  = stderr;
            this.success = success;
        }
    }
}
