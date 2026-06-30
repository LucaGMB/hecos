# Bitácora – Sprint 4

**Fecha:** lunes 30 de junio de 2026  
**Participantes:** Luca Guarna, Gael

---

## Objetivo del sprint

Desarrollar el prototipo del frontend web con React, que permita autenticación con Google y visualización de los datos sincronizados desde el celular.

---

## Tareas completadas

| # | Tarea | Responsable | Estado |
|---|-------|-------------|--------|
| 11 | Frontend web: prototipo de panel de visualización | Luca | ✅ Done |
| 16 | Frontend web: desarrollo del panel de visualización con React | Luca | ✅ Done |

---

## Lo que se implementó

### Frontend Web (React + Vite + TypeScript)

- **`api.ts`**: cliente Axios con interceptor de JWT. Expone `authGoogle(idToken)` para el login y `getSummary()` para obtener el resumen de datos.
- **`auth.ts`**: manejo de sesión en `localStorage` con helpers `saveSession`, `getSession`, `clearSession`.
- **`App.tsx`**: router con `BrowserRouter` y rutas protegidas via `PrivateRoute`. Envuelve la app con `GoogleOAuthProvider`.
- **`Login.tsx`**: página de login con botón de Google via `@react-oauth/google`. Al autenticar, intercambia el `credential` (idToken) con el backend y guarda el JWT.
- **`Dashboard.tsx`**: panel principal que muestra:
  - Total de registros sincronizados (tarjeta con número grande)
  - Gráfico de barras (Recharts) con los 12 tipos de datos más frecuentes
  - Grilla de tarjetas con el conteo por cada tipo
  - Labels en español para los 34 tipos de Health Connect
- **`Dashboard.module.css`** / **`Login.module.css`**: estilos con CSS Modules. Diseño oscuro con variable `--surface`, colores de Google.

### Infraestructura

- **`Dockerfile`**: build multietapa — Node 22 Alpine para compilar con Vite, nginx Alpine para servir el dist.
- **`nginx.conf`**: SPA routing con `try_files $uri $uri/ /index.html`.
- **`.env.example`**: documenta las variables de build `VITE_API_URL` y `VITE_GOOGLE_CLIENT_ID`.
- Configurado en **Dokploy** como aplicación Docker con Build-time Arguments para las variables de Vite.

---

## Decisiones técnicas

- Las variables `VITE_*` son **build-time** (Vite las embebe en el JS en compilación), no runtime. Por eso se pasan como Build Arguments en Dokploy, no como Environment Variables.
- Se usa CSS Modules en lugar de Tailwind/styled-components para mantener el proyecto simple y sin dependencias extra.
- El gráfico muestra máximo 12 tipos ordenados por frecuencia para evitar que el eje X quede ilegible.

---

## PRs mergeados

- PR #21: `feature/sprint-4-web` → `main` (frontend React + bitácora Sprint 3)
- PR #22: `feature/sprint-4-web` → `main` (clases Sprint 3 en diagrama UML)

---

## Obstáculos

- Error en Dokploy al deployar el frontend: `cannot create .../code/main/apps/web/.env: Directory nonexistent`. Causa: las variables `VITE_*` estaban configuradas como Environment Variables en lugar de Build-time Arguments. Fix: moverlas a la sección "Build-time Arguments" en Dokploy.
