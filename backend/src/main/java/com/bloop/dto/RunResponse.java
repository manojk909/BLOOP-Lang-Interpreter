package com.bloop.dto;

/**
 * Response body for POST /api/run.
 */
public class RunResponse {

    private String  stdout;
    private String  stderr;
    private boolean success;
    private long    executionTimeMs;

    public RunResponse() {}

    public RunResponse(String stdout, String stderr,
                       boolean success, long executionTimeMs) {
        this.stdout         = stdout;
        this.stderr         = stderr;
        this.success        = success;
        this.executionTimeMs = executionTimeMs;
    }

    public String  getStdout()          { return stdout; }
    public String  getStderr()          { return stderr; }
    public boolean isSuccess()          { return success; }
    public long    getExecutionTimeMs() { return executionTimeMs; }

    public void setStdout(String stdout)                   { this.stdout = stdout; }
    public void setStderr(String stderr)                   { this.stderr = stderr; }
    public void setSuccess(boolean success)                { this.success = success; }
    public void setExecutionTimeMs(long executionTimeMs)   { this.executionTimeMs = executionTimeMs; }
}
