# Deploy en Dokploy

Tres servicios a crear en Dokploy:

## 1. PostgreSQL (Managed Database)

- Crear un servicio de tipo **Database → PostgreSQL**.
- Configurar: nombre de DB, usuario y contraseña (equivalentes a `DB_NAME`, `DB_USER`, `DB_PASSWORD`).
- Anotar el host interno que asigna Dokploy para usarlo como `DB_HOST` en el backend.

## 2. Backend (Docker app, `apps/api`)

- Tipo de app: **Docker**, build context `apps/api` (usa `apps/api/Dockerfile`).
- **Environment Variables**:
  - `DB_HOST`
  - `DB_PORT`
  - `DB_NAME`
  - `DB_USER`
  - `DB_PASSWORD`
  - `PORT` (default `8080`)
  - `GOOGLE_CLIENT_ID`
  - `GOOGLE_CLIENT_SECRET`
  - `JWT_SECRET`
- No requiere Build-time Arguments.
- Configurar dominio/subdominio (ej. `api.hecos.tudominio.com`) si aplica.

## 3. Frontend (Docker app, `apps/web`)

- Tipo de app: **Docker**, build context `apps/web` (usa `apps/web/Dockerfile`).
- **Build-time Arguments** (⚠️ NO como Environment Variables — son `ARG` de Vite, se embeben en el bundle al compilar):
  - `VITE_API_URL`
  - `VITE_GOOGLE_CLIENT_ID`
- Sin Environment Variables en runtime (nginx solo sirve estáticos).
- Configurar dominio/subdominio (ej. `hecos.tudominio.com`) si aplica.

> Error ya documentado en `docs/bitacora/sprint-4.md`: si `VITE_*` se cargan como Environment Variables en vez de Build-time Arguments, el build falla (`cannot create .../apps/web/.env: Directory nonexistent`).

## Google OAuth2 (Google Cloud Console)

Actualizar en las credenciales OAuth del proyecto:

- **Authorized redirect URI** (backend): `https://<dominio-backend>/login/oauth2/code/google` (o el callback que use Spring Security).
- **Authorized JavaScript origin** (frontend): `https://<dominio-frontend>`.

## Referencia

- Variables completas y valores de ejemplo: ver `.env.example` en la raíz del repo.
- Decisiones de arquitectura de Dockerfiles: `apps/web/Dockerfile`, `apps/api/Dockerfile`.
