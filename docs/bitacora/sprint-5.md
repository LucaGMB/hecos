# Bitácora – Sprint 5

**Fecha:** miércoles 2 de julio de 2026
**Participantes:** Luca Guarna, Gael

---

## Objetivo del sprint

Completar el panel web: agregar la tabla de registros con filtros y la página de perfil/settings que faltaban del prototipo del Sprint 4, y sumar el endpoint de backend necesario para listarlos.

---

## Tareas completadas

| # | Tarea | Responsable | Estado |
|---|-------|-------------|--------|
| 16 | Frontend web: desarrollo del panel de visualización con React | Luca | ✅ Done |

---

## Lo que se implementó

### Backend (Spring Boot)

- **`GET /api/health/records`** en `SyncController`: lista registros paginados (`page`/`size`) con filtros opcionales por `type` y rango `from`/`to` sobre `receivedAt`.
- **`RawSyncRecordRepository.findByFilters`**: query JPQL con `Page<RawSyncRecord>` y filtros opcionales vía `:param IS NULL OR ...`.
- **`SyncService.getRecords`**: arma el `Pageable` y delega al repositorio.
- **`AuthController`**: `/auth/google` ahora también devuelve `avatarUrl` para mostrarlo en el perfil.

### Frontend Web (React)

- **`pages/Records.tsx`**: tabla paginada de registros con filtros por tipo, rango de fechas y filas por página; cada fila permite expandir el JSON crudo del registro.
- **`pages/Profile.tsx`**: datos de la cuenta de Google (avatar/nombre/email) y preferencias de visualización (rango de fechas por defecto, filas por página) persistidas en `localStorage` y usadas como default en Registros/Dashboard.
- **`components/NavBar.tsx`**: navegación compartida entre Dashboard, Registros y Perfil, reemplaza el header que estaba duplicado en `Dashboard.tsx`.
- **`constants.ts`**: `TYPE_LABELS`/`COLORS` y helpers de preferencias, extraídos de `Dashboard.tsx` para no duplicarlos entre páginas.

---

## Decisiones técnicas

- El filtro de fechas se aplica sobre `receivedAt` (cuándo llegó el registro al servidor) y no sobre el timestamp real del dato de salud, porque ese timestamp vive dentro del JSON crudo (`data`) y no está indexado — es la misma limitación de diseño de la tabla genérica `raw_sync_records` del Sprint 3.
- Las preferencias de usuario (rango por defecto, filas por página) se guardan en `localStorage` en vez de en el backend, para no agregar una tabla nueva solo para esto.

---

## PR

- PR #26: `feature/sprint-5-frontend-completo` → `main`

---

## Obstáculos

- Ninguno relevante.
