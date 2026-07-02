package com.hecos.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hecos.entity.RawSyncRecord;
import com.hecos.repository.RawSyncRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final RawSyncRecordRepository repo;

    public record SyncResult(int received, int saved, int duplicates) {}

    public SyncResult processRecords(UUID userId, String type, JsonNode records) {
        int saved = 0, duplicates = 0;

        for (JsonNode record : records) {
            var hcId = record.path("healthConnectId").asText(null);
            if (hcId == null || hcId.isBlank()) continue;

            if (repo.existsByUserIdAndTypeAndHealthConnectId(userId, type, hcId)) {
                duplicates++;
            } else {
                repo.save(RawSyncRecord.builder()
                    .userId(userId)
                    .type(type)
                    .healthConnectId(hcId)
                    .sourceApp(record.path("sourceApp").asText(null))
                    .data(record.toString())
                    .build());
                saved++;
            }
        }

        return new SyncResult(records.size(), saved, duplicates);
    }

    public Map<String, Long> getSummaryByType(UUID userId) {
        var result = new LinkedHashMap<String, Long>();
        for (var row : repo.countByType(userId)) {
            result.put((String) row[0], (Long) row[1]);
        }
        return result;
    }

    public long getTotalCount(UUID userId) {
        return repo.countByUserId(userId);
    }

    public Page<RawSyncRecord> getRecords(UUID userId, String type, Instant from, Instant to, int page, int size) {
        Specification<RawSyncRecord> spec = (root, query, cb) -> cb.equal(root.get("userId"), userId);

        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }
        if (from != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("receivedAt"), from));
        }
        if (to != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("receivedAt"), to));
        }

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "receivedAt"));
        return repo.findAll(spec, pageable);
    }
}
