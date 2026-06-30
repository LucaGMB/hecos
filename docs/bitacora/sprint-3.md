# Bitácora – Sprint 3

**Fecha:** miércoles 25 de junio de 2026  
**Participantes:** Luca Guarna, Gael

---

## Objetivo del sprint

Implementar la capa de repositorios y servicios del backend, la clase administradora `HealthDataManager`, y desarrollar la app Android con lectura de Health Connect y sincronización de datos.

---

## Tareas completadas

| # | Tarea | Responsable | Estado |
|---|-------|-------------|--------|
| 6 | Capa de servicio y repositorios Spring Boot | Luca | ✅ Done |
| 7 | API REST: endpoints para recibir y consultar datos | Luca | ✅ Done |
| 8 | App Android: lectura de todos los datos de Health Connect | Luca | ✅ Done |

---

## Lo que se implementó

### Backend (Spring Boot)

- **`JwtAuthFilter`**: filtro de Spring Security que lee el header `Authorization: Bearer <token>`, valida el JWT con `JwtService` y setea el usuario autenticado en el `SecurityContext`. Esto protege todos los endpoints bajo `/api/**`.
- **`RawSyncRecord`**: entidad JPA que mapea a la tabla `raw_sync_records` en PostgreSQL. Almacena cada registro de salud recibido con su tipo, `healthConnectId` (para deduplicación), datos en JSON y referencia al usuario.
- **`RawSyncRecordRepository`**: repositorio JPA con método `existsByUserIdAndTypeAndHealthConnectId` para evitar duplicados, y `countByType` para el resumen.
- **`SyncService`** (backend): servicio que itera los registros recibidos, los desduplicada y los persiste.
- **`SyncController`**: controlador REST con dos endpoints:
  - `POST /api/health/{type}` — recibe un array JSON de registros del celular
  - `GET /api/health/summary` — devuelve totales por tipo de dato

### App Android (Kotlin)

- **`HealthConnectReader`**: lee los 34 tipos de datos de Health Connect del último año y los serializa a JSON. Maneja tipos con timestamp de intervalo y de instante.
- **`RecordTypes`**: mapa de slugs a clases de Health Connect (`steps`, `heart-rate`, `sleep-session`, etc.).
- **`SyncService`** (mobile): orquesta la lectura completa y el envío tipo por tipo al backend, exponiendo progreso vía `StateFlow`.
- **`TokenStore`**: guarda el JWT y datos del usuario en DataStore encriptado.
- **`ApiClient` + `HecosApi`**: cliente Retrofit con los endpoints `POST /auth/google` y `POST /api/health/{type}`.
- **`AuthActivity`**: pantalla de login con Google Sign-In que intercambia el idToken con el backend.
- **`HomeActivity`**: pantalla principal con botón de sincronización, barra de progreso y estado por tipo.

---

## Decisiones técnicas

- Se usa una tabla genérica `raw_sync_records` en lugar de una tabla por tipo. Esto permite recibir y deduplicar cualquier tipo de dato sin implementar 34 repositorios separados. Las entidades JPA específicas del Sprint 2 sirven para el modelo conceptual (POO/herencia requerida por la materia).
- La deduplicación se hace por la combinación `(userId, type, healthConnectId)` — constraint único en la DB y verificación previa con `existsByUserIdAndTypeAndHealthConnectId`.

---

## PR mergeado

- PR #19: `feature/sprint-3-mobile` → `main`

---

## Obstáculos

- `IntervalRecord` e `InstantaneousRecord` de Health Connect SDK `1.1.0-alpha10` pasaron a ser `internal`, removido el bloque fallback (fix en PR #20).
- `JsonArray` de Gson no tiene extensión `.isNotEmpty()` en Kotlin — cambiado a `.size() > 0`.
