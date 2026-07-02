# Bitácora individual – Luca Guarna

Proyecto HECOS. Registro personal de lo hecho en cada sprint.

---

## Sprint 1 – Análisis y definición (antes del 22/06)

- Participé en la definición de la idea general del proyecto (extraer datos de Health Connect y centralizarlos en un panel web).
- Ayudé a listar los tipos de datos de Health Connect a modelar (~50 tipos) y sus atributos principales.
- Sin impedimentos.

---

## Sprint 2 – lunes 22 de junio de 2026

- Implementé las entidades JPA (`BaseHealthRecord` y ~50 hijas), la entidad `User` y el enum `Criticidad`.
- Configuré la autenticación con Google (OAuth2 en el backend + generación de JWT con `JwtService`).
- Impedimento: ninguno relevante, sprint fluido.
- PRs: #14, #15.

---

## Sprint 3 – miércoles 25 de junio de 2026

- Desarrollé la capa de repositorios y servicios (`HealthDataManager`, `RawSyncRecordRepository`, `SyncService`) y los endpoints REST (`POST /api/health/{type}`, `GET /api/health/summary`).
- Implementé la app Android: lectura completa de Health Connect (34 tipos) y sync manual contra el backend.
- Impedimento: la SDK de Health Connect (`1.1.0-alpha10`) cambió la visibilidad de algunas clases (`IntervalRecord`/`InstantaneousRecord` pasaron a `internal`), tuve que resolverlo en un fix aparte al día siguiente.
- PRs: #18, #19, #20 (fix).

---

## Sprint 4 – lunes 30 de junio de 2026

- Armé el prototipo del frontend en React (login con Google, dashboard con resumen y gráfico de registros por tipo).
- Escribí el cliente HTTP (`api.ts`) y el manejo de sesión (`auth.ts`).
- Arreglé un bug de permisos de Health Connect en dispositivos Samsung.
- Impedimento: error al deployar el frontend en Dokploy por confundir variables de build (`VITE_*`) con variables de entorno de runtime. Resuelto pasándolas como Build-time Arguments.
- PRs: #21, #23, #24.

---

## Sprint 5 – miércoles 2 de julio de 2026

- Agregué el endpoint de backend para listar registros paginados y filtrados por tipo/fecha (`GET /api/health/records`).
- Completé el frontend: página de tabla de registros con filtros, y página de perfil con preferencias de visualización.
- Sin impedimentos grandes, sprint de cierre del frontend.

---

## Sprint 6 – lunes 7 de julio de 2026

- Implementé el sync automático en la app Android (WorkManager periódico) y una cola local (Room) para reintentar envíos cuando el servidor no responde.
- Dejé lista la configuración de deploy (docker-compose con los 3 servicios, variables documentadas) para el deploy final en Dokploy.
- Actualicé el estado real de las tarjetas del board, que estaba desactualizado respecto al código.
