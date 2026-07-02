# Bitácora – Sprint 6

**Fecha:** lunes 7 de julio de 2026
**Participantes:** Luca Guarna, Gael

---

## Objetivo del sprint

Cerrar lo que quedaba pendiente para la entrega final: sync automático en la app Android con cola de reintentos, y dejar lista la configuración de deploy en Dokploy (backend + frontend + base de datos).

---

## Tareas completadas

| # | Tarea | Responsable | Estado |
|---|-------|-------------|--------|
| 9 | App Android: envío de datos con sync manual y automático | Luca | ✅ Done |
| 12 | Deploy: configurar Dokploy para backend, frontend y base de datos | Luca | ✅ Done |
| 17 | Deploy en Dokploy y sincronización automática | Luca | ✅ Done |
| 13 | Documentación: bitácora de seguimiento por sprint | Luca, Gael | ✅ Done |

---

## Lo que se implementó

### App Android (Kotlin)

- **Cola local con Room** (`data/local/PendingSyncBatch`, `PendingSyncBatchDao`, `HecosDatabase`): cuando el envío de un tipo de dato falla (sin red o error del servidor), el lote se guarda localmente en vez de perderse.
- **`SyncService.syncAll()`** ahora primero intenta vaciar la cola pendiente (reintentar lotes fallidos) y recién después sincroniza los datos nuevos; lo que vuelve a fallar queda en la cola con su contador de intentos.
- **Sync automático con WorkManager** (`work/AutoSyncWorker`, `work/AutoSyncScheduler`): trabajo periódico con constraint de red conectada y backoff exponencial ante fallos. Frecuencia configurable por el usuario (1h/6h/12h/24h).
- **`SyncSettingsDialog`**: diálogo simple desde `HomeActivity` para activar/desactivar el sync automático y elegir la frecuencia.

### Deploy (Dokploy)

- **`docker-compose.yml`**: se agregó el servicio `web` (frontend, con sus build args de Vite) que faltaba, y las credenciales de la base de datos y de la API se parametrizaron con variables de entorno en vez de quedar hardcodeadas.
- **`.env.example`** (raíz): documenta todas las variables necesarias para levantar los tres servicios.
- **`docs/DEPLOY.md`**: guía corta de los 3 servicios a crear en Dokploy (PostgreSQL, backend, frontend), remarcando que las variables `VITE_*` del frontend van como *Build-time Arguments* y no como *Environment Variables* (error ya documentado en el Sprint 4), y los redirect URIs a actualizar en Google Cloud Console.

### Documentación

- Se agregaron bitácoras individuales por integrante (`docs/bitacora/bitacora-luca.md`, `docs/bitacora/bitacora-gael.md`) además de esta bitácora técnica compartida por sprint.

---

## Decisiones técnicas

- WorkManager persiste el trabajo periódico en su propia base de datos y sobrevive a reinicios del dispositivo sin necesidad de un `BroadcastReceiver` de `BOOT_COMPLETED`.
- El deploy real en el servidor de Dokploy queda como paso manual (fuera del repo): esta entrega deja toda la configuración y documentación necesaria para hacerlo sin errores.

---

## PRs

- PR #25: `feature/sprint-6-deploy-dokploy` → `main`
- PR #27: `feature/sprint-6-sync-automatico` → `main`
- PR #28: `docs/sprint-5-6-bitacora` → `main`

---

## Obstáculos

- Ninguno relevante.
