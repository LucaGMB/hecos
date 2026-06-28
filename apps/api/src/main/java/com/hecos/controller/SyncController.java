package com.hecos.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hecos.service.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @PostMapping("/{type}")
    public ResponseEntity<?> sync(
            @PathVariable String type,
            @RequestBody JsonNode records,
            Authentication auth) {

        if (!records.isArray()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Expected a JSON array"));
        }

        var userId = UUID.fromString(auth.getName());
        var result = syncService.processRecords(userId, type, records);
        return ResponseEntity.ok(Map.of(
            "received", result.received(),
            "saved", result.saved(),
            "duplicates", result.duplicates()
        ));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> summary(Authentication auth) {
        var userId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(Map.of(
            "total", syncService.getTotalCount(userId),
            "byType", syncService.getSummaryByType(userId)
        ));
    }
}
