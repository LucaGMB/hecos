# HECOS - Contexto del proyecto

## Que es HECOS

HECOS (HEalth COnnect Server) es una plataforma para extraer todos los datos de salud de Health Connect (Android), enviarlos a un servidor y visualizarlos en un panel web. El nombre oficial es HECOS, sin aclarar el acronimo.

## Para que materia es

Trabajo Practico de Desarrollo de Sistemas, Escuela Tecnica Nº3. Dura 3 semanas (6 sprints, uno por clase). Las clases son lunes y miercoles. El profesor dio libertad para salirse de la consigna original (que pedia Java SE puro con consola), asi que estamos haciendo un proyecto real con arquitectura profesional.

La consigna pide que se siga POO con herencia, polimorfismo, clases padre/hija, clase administradora con colecciones. Eso se cumple en el backend con las entidades JPA.

## Stack

- Backend: Java + Spring Boot + JPA/Hibernate
- Base de datos: PostgreSQL (una tabla por cada tipo de dato de Health Connect)
- App mobile: Kotlin + Health Connect SDK (Android)
- Frontend web: React
- Auth: Google Sign-In (en Android, backend y web)
- Deploy: Dokploy (selfhosted)

## Estructura del monorepo

```
hecos/
├── apps/
│   ├── api/          <- Spring Boot (backend)
│   ├── mobile/       <- Kotlin Android (app)
│   └── web/          <- React (panel web)
├── docs/
│   ├── uml/          <- Diagrama de clases (draw.io)
│   └── bitacora/     <- Documentacion por sprint
```

## Modelo de datos

Se usa una tabla por cada tipo de dato de Health Connect. Hay aproximadamente 50 tipos (Steps, HeartRate, Sleep, ExerciseSession, Distance, Calories, Speed, Weight, BloodPressure, OxygenSaturation, etc.).

Todas las entidades heredan de una clase padre HealthRecord que tiene los campos comunes (id, userId, timestamp, source). Cada hija tiene sus atributos especificos y su propia tabla en PostgreSQL.

La entidad User tiene los datos de Google Auth (googleId, email, name, avatar).

## Como funciona

1. El usuario se loguea con Google en la app Android
2. La app lee TODOS los datos de Health Connect (historial completo, no solo datos nuevos)
3. Los datos se envian al servidor (Spring Boot)
4. El servidor los procesa, evita duplicados y los guarda en PostgreSQL
5. El usuario se loguea en el panel web (tambien con Google) y ve todos sus datos con graficos, filtros, etc.

La app tiene sync manual (boton) y automatico (configurable en frecuencia/horarios). Si el servidor no esta disponible, los datos se guardan en una cola local y se reintentan.

## Distribucion del trabajo

- Luca: parte de datos, backend, app mobile, prototipo del frontend (con Claude Code)
- Gael: diseño en Figma (a partir del prototipo), UML en draw.io, frontend definitivo en React

## Gestion del proyecto

- Repo: https://github.com/LucaGMB/hecos
- Board: https://github.com/users/LucaGMB/projects/2
- Se usa GitHub Projects en vez de Trello
- Labels de criticidad: Baja, Moderada, Alta, Critica, Bloqueante
- Columnas del board: Product Backlog > To Do > In Progress > Testing > Done

## Sprints

Sprint 1 (ya paso): Analisis y definicion. Se penso la idea y se definieron las clases.

Sprint 2 (lunes 22 de junio): Desarrollo de entidades, diagrama UML, autenticacion con Google (OAuth2 + JWT).

Sprint 3 (miercoles 25 de junio): Capa de repositorios y servicios, HealthDataManager.

Sprint 4: API REST, app Android con lectura de Health Connect.

Sprint 5: Frontend React.

Sprint 6: Deploy en Dokploy, sync de la app, testing, bitacora.

## Entregables para la materia

- Link al GitHub Project (se revisa el historial de movimientos de tarjetas)
- Bitacora con capturas del board y minutas de cada sprint
- Diagrama de clases UML (imagen o PDF)
- Codigo fuente

## Cosas a tener en cuenta

- El proyecto tiene que servir para portfolio y uso personal, no solo para aprobar
- Se extraen TODOS los tipos de datos de Health Connect, sin excepcion
- El frontend es un prototipo primero, despues se hace el diseño en Figma y se implementa bien
- A futuro se podria agregar login con Strava, UI en la app mobile, y seleccion de que datos enviar
