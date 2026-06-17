package com.bloop.controller;

import com.bloop.dto.RunRequest;
import com.bloop.dto.RunResponse;
import com.bloop.service.BloopService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Exposes two endpoints:
 *
 * <pre>
 *   POST /api/run    — execute BLOOP source code
 *   GET  /api/health — liveness probe for Render
 * </pre>
 */
@RestController
@RequestMapping("/api")
public class RunController {

    private final BloopService bloopService;

    public RunController(BloopService bloopService) {
        this.bloopService = bloopService;
    }

    /**
     * Execute BLOOP source code.
     *
     * @param request  JSON body: {@code { "code": "..." }}
     * @return JSON: stdout, stderr, success, executionTimeMs
     */
    @PostMapping(
        value    = "/run",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RunResponse run(@RequestBody RunRequest request) {
        return bloopService.run(request.getCode());
    }

    /**
     * Health check — Render pings this to verify the service is up.
     */
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
