package com.hecos.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hecos.config.HealthRecordTypeRegistry;
import com.hecos.entity.base.BaseHealthRecord;
import com.hecos.service.HealthDataManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthDataController {

    private final HealthDataManager healthDataManager;
    private final HealthRecordTypeRegistry typeRegistry;
    private final ObjectMapper objectMapper;

    @PostMapping("/{type}")
    public ResponseEntity<?> syncRecords(@PathVariable String type,
                                         @RequestBody List<JsonNode> records,
                                         Authentication auth) {
        var userId = (UUID) auth.getPrincipal();
        var clazz = typeRegistry.getClass(type);
        if (clazz.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Unknown type: " + type));
        }

        List<BaseHealthRecord> parsed = new ArrayList<>();
        for (var node : records) {
            try {
                var record = objectMapper.treeToValue(node, clazz.get());
                record.setUserId(userId);
                parsed.add(record);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Failed to parse record",
                    "details", e.getMessage()
                ));
            }
        }

        var saved = healthDataManager.agregarRegistros(parsed);
        return ResponseEntity.ok(Map.of(
            "received", records.size(),
            "saved", saved.size(),
            "duplicates", records.size() - saved.size()
        ));
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getRecords(@PathVariable String type,
                                        @RequestParam(required = false) Instant from,
                                        @RequestParam(required = false) Instant to,
                                        Authentication auth) {
        var userId = (UUID) auth.getPrincipal();
        var clazz = typeRegistry.getClass(type);
        if (clazz.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Unknown type: " + type));
        }

        List<? extends BaseHealthRecord> results;
        if (from != null && to != null) {
            results = healthDataManager.listarPorTipoYRango(clazz.get(), userId, from, to);
        } else {
            results = healthDataManager.listarPorTipo(clazz.get(), userId);
        }

        return ResponseEntity.ok(results);
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(Authentication auth) {
        var userId = (UUID) auth.getPrincipal();
        var summary = new LinkedHashMap<String, Object>();
        long total = 0;

        var typeCounts = new LinkedHashMap<String, Long>();
        for (var slug : typeRegistry.getAllSlugs()) {
            var clazz = typeRegistry.getClass(slug).orElseThrow();
            long count = healthDataManager.cantidadPorTipo(clazz, userId);
            if (count > 0) {
                typeCounts.put(slug, count);
                total += count;
            }
        }

        summary.put("totalRecords", total);
        summary.put("types", typeCounts);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/types")
    public ResponseEntity<?> getAvailableTypes() {
        return ResponseEntity.ok(typeRegistry.getAllSlugs());
    }
}
