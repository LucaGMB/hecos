package com.hecos.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hecos.entity.RawSyncRecord;
import com.hecos.repository.RawSyncRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
