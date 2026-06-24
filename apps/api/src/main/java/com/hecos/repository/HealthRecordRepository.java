package com.hecos.repository;

import com.hecos.entity.base.BaseHealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HealthRecordRepository extends JpaRepository<BaseHealthRecord, UUID> {

    List<BaseHealthRecord> findByUserId(UUID userId);

    Optional<BaseHealthRecord> findByHealthConnectId(String healthConnectId);

    boolean existsByHealthConnectId(String healthConnectId);

    @Query("SELECT r FROM BaseHealthRecord r WHERE TYPE(r) = :type AND r.userId = :userId")
    List<BaseHealthRecord> findByTypeAndUserId(@Param("type") Class<? extends BaseHealthRecord> type,
                                                @Param("userId") UUID userId);

    @Query("SELECT r FROM BaseHealthRecord r WHERE r.userId = :userId AND r.createdAt BETWEEN :from AND :to")
    List<BaseHealthRecord> findByUserIdAndDateRange(@Param("userId") UUID userId,
                                                     @Param("from") Instant from,
                                                     @Param("to") Instant to);

    @Query("SELECT r FROM BaseHealthRecord r WHERE TYPE(r) = :type AND r.userId = :userId AND r.createdAt BETWEEN :from AND :to")
    List<BaseHealthRecord> findByTypeAndUserIdAndDateRange(@Param("type") Class<? extends BaseHealthRecord> type,
                                                           @Param("userId") UUID userId,
                                                           @Param("from") Instant from,
                                                           @Param("to") Instant to);

    @Query("SELECT COUNT(r) FROM BaseHealthRecord r WHERE r.userId = :userId")
    long countByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(r) FROM BaseHealthRecord r WHERE TYPE(r) = :type AND r.userId = :userId")
    long countByTypeAndUserId(@Param("type") Class<? extends BaseHealthRecord> type,
                               @Param("userId") UUID userId);

    List<BaseHealthRecord> findByUserIdAndHealthConnectIdIn(UUID userId, List<String> healthConnectIds);
}
