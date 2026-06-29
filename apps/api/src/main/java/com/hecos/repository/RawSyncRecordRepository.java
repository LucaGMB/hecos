package com.hecos.repository;

import com.hecos.entity.RawSyncRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RawSyncRecordRepository extends JpaRepository<RawSyncRecord, UUID> {

    boolean existsByUserIdAndTypeAndHealthConnectId(UUID userId, String type, String healthConnectId);

    long countByUserId(UUID userId);

    @Query("SELECT r.type, COUNT(r) FROM RawSyncRecord r WHERE r.userId = :userId GROUP BY r.type ORDER BY r.type")
    List<Object[]> countByType(UUID userId);
}
