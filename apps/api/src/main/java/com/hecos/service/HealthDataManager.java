package com.hecos.service;

import com.hecos.entity.base.BaseHealthRecord;
import com.hecos.repository.HealthRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HealthDataManager {

    private final HealthRecordRepository repository;

    @Transactional
    public BaseHealthRecord agregarRegistro(BaseHealthRecord record) {
        if (record.getHealthConnectId() != null && repository.existsByHealthConnectId(record.getHealthConnectId())) {
            return null;
        }
        return repository.save(record);
    }

    @Transactional
    public List<BaseHealthRecord> agregarRegistros(List<BaseHealthRecord> records) {
        List<String> hcIds = records.stream()
                .map(BaseHealthRecord::getHealthConnectId)
                .filter(id -> id != null)
                .toList();

        List<String> existingIds = hcIds.isEmpty() ? List.of() :
                repository.findByUserIdAndHealthConnectIdIn(records.getFirst().getUserId(), hcIds)
                        .stream()
                        .map(BaseHealthRecord::getHealthConnectId)
                        .toList();

        List<BaseHealthRecord> toSave = records.stream()
                .filter(r -> r.getHealthConnectId() == null || !existingIds.contains(r.getHealthConnectId()))
                .toList();

        return repository.saveAll(toSave);
    }

    public List<BaseHealthRecord> listarTodos() {
        return repository.findAll();
    }

    public List<BaseHealthRecord> listarPorUsuario(UUID userId) {
        return repository.findByUserId(userId);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseHealthRecord> List<T> listarPorTipo(Class<T> tipo, UUID userId) {
        return (List<T>) repository.findByTypeAndUserId(tipo, userId);
    }

    public List<BaseHealthRecord> listarPorRangoDeFechas(UUID userId, Instant desde, Instant hasta) {
        return repository.findByUserIdAndDateRange(userId, desde, hasta);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseHealthRecord> List<T> listarPorTipoYRango(Class<T> tipo, UUID userId, Instant desde, Instant hasta) {
        return (List<T>) repository.findByTypeAndUserIdAndDateRange(tipo, userId, desde, hasta);
    }

    public long cantidadTotal(UUID userId) {
        return repository.countByUserId(userId);
    }

    public long cantidadPorTipo(Class<? extends BaseHealthRecord> tipo, UUID userId) {
        return repository.countByTypeAndUserId(tipo, userId);
    }

    public boolean eliminarRegistro(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
