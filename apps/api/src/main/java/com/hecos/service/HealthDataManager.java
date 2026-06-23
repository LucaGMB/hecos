package com.hecos.service;

import com.hecos.entity.base.BaseHealthRecord;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthDataManager {

    private final List<BaseHealthRecord> records = new ArrayList<>();

    public void agregarRegistro(BaseHealthRecord record) {
        records.add(record);
    }

    public boolean eliminarRegistro(BaseHealthRecord record) {
        return records.remove(record);
    }

    public List<BaseHealthRecord> listarTodos() {
        return new ArrayList<>(records);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseHealthRecord> List<T> listarPorTipo(Class<T> tipo) {
        return records.stream()
                .filter(tipo::isInstance)
                .map(r -> (T) r)
                .toList();
    }

    public void mostrarTodos() {
        if (records.isEmpty()) {
            System.out.println("No hay registros almacenados.");
            return;
        }
        for (BaseHealthRecord record : records) {
            System.out.println(record);
        }
    }

    public int cantidadTotal() {
        return records.size();
    }

    public void limpiar() {
        records.clear();
    }

    @PostConstruct
    public void init() {
        System.out.println("HealthDataManager inicializado. Listo para gestionar registros de salud.");
    }
}
