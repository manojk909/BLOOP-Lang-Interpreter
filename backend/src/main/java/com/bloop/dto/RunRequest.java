package com.bloop.dto;

/**
 * Request body for POST /api/run.
 */
public class RunRequest {

    private String code;

    public RunRequest() {}

    public RunRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
