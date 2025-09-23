# EarthWay
EarthWay es una plataforma web desarrollada con **Spring Boot**, **PostgreSQL/PostGIS** y **React** que permite gestionar usuarios, organizaciones, eventos, publicaciones, reportes y certificados. Además, integra funcionalidades de **geolocalización** y seguridad basada en **JWT y roles de usuario**.

---

## Características

- Registro y autenticación de usuarios con JWT.
- Gestión de roles: `ROLE_ADMIN` y `ROLE_USER`.
- CRUD completo para **usuarios, organizaciones, eventos y publicaciones**.
- Geolocalización de eventos usando **PostGIS**.
- Generación de reportes y certificados.
- Frontend en **React** con diseño responsivo.
- Integración con **Cloudinary** para subida de imágenes.

---

## Tecnologías

- **Backend**: Spring Boot, Java 17, Spring Security, JWT, Postgres/PostGIS, Hibernate.
- **Frontend**: React, Tailwind CSS, React Query, Axios.
- **Otros**: Cloudinary (almacenamiento de imágenes), Mapbox/Leaflet (geolocalización).

---

## Instalación

### Requisitos

- Java 17 o superior
- Maven
- Node.js y npm
- Docker (opcional, para la base de datos)
- PostgreSQL con extensión PostGIS

### Backend

1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/earthway.git
cd earthway/backend


2. Configurar variables de entorno en application.properties o .env:
spring.datasource.url=jdbc:postgresql
spring.datasource.username=postgres
spring.datasource.password=tu_password
jwt.secret=tu_clave_secreta
cloudinary.url=tu_url_cloudinary
d

3. Ejecutar la aplicación

Endpoints principales
Usuarios
    POST /api/v1/auth/register – Registrar usuario
    POST /api/v1/auth/login – Login y obtención de JWT
    GET /api/v1/users – Obtener todos los usuarios (solo admin)
Organizaciones
    POST /api/v1/organization – Crear organización
    GET /api/v1/organization/{id} – Obtener detalles de una organización
Eventos
    POST /api/v1/event – Crear evento
    GET /api/v1/event – Obtener lista de eventos


 Contacto
     Autor: Carlos Altán
     GitHub: https://github.com/carlosaltan18