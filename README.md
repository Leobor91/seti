# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por �ltimo el inicio y configuraci�n de la aplicaci�n.

Lee el art�culo [Clean Architecture � Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Cómo levantar el servicio en local

Sigue estos pasos para ejecutar el servicio en tu entorno local:

1. **Prerrequisitos:**
   - Java 17 instalado y configurado en tu PATH.
   - Git (opcional, si necesitas clonar el repo).
   - No necesitas instalar Gradle: el wrapper (`gradlew.bat`) ya está incluido.

2. **Clona el repositorio (si no lo tienes):**
   ```powershell
   git clone https://github.com/Leobor91/seti.git
   cd franchise-management
   ```

3. **Construye todos los módulos:**
   ```powershell
   .\gradlew.bat build
   ```

4. **Levanta la aplicación en modo desarrollo:**
   ```powershell
   .\gradlew.bat :applications:app-service:bootRun
   ```
   Esto inicia el único entrypoint de Spring Boot en el puerto 8080 por defecto.

5. *Endpoints principales:**
   Consulta la sección "API — Endpoints principales" en el README para ejemplos de uso con curl.

6. **Notas adicionales:**
    - Si necesitas cambiar el puerto, edita `applications/app-service/src/main/resources/application.properties` o usa la variable de entorno `SERVER_PORT`.
    - Para correr los tests:
      ```powershell
      .\gradlew.bat test
      ```
    - Para generar el jar ejecutable:
      ```powershell
      .\gradlew.bat :applications:app-service:bootJar
      ```

# Empaquetado y despliegue con Docker

El proyecto incluye una implementación básica para empaquetar y ejecutar la aplicación usando Docker. Para construir y correr la imagen:

1. Genera el jar ejecutable:
   ```powershell
   .\gradlew.bat :applications:app-service:bootJar
   ```
   El jar se ubicará en `applications/app-service/build/libs/`.

2. Construye la imagen Docker:
   ```powershell
   docker build -t franchise-app -f applications/app-service/Dockerfile .
   ```

3. Ejecuta el contenedor:
   ```powershell
   docker run -p 8080:8080 franchise-app
   ```

Puedes personalizar variables de entorno (como el puerto) usando la opción `-e` de Docker.

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el m�dulo m�s interno de la arquitectura, pertenece a la capa del dominio y encapsula la l�gica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este m�dulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define l�gica de aplicaci�n y reacciona a las invocaciones desde el m�dulo de entry points, orquestando los flujos hacia el m�dulo de entities.

## Infrastructure
### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no est�n arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
basadas en el patr�n de dise�o [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicaci�n o el inicio de los flujos de negocio.


Este m�dulo es el m�s externo de la arquitectura, es el encargado de ensamblar los distintos m�dulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma autom�tica, inyectando en �stos instancias concretas de las dependencias declaradas. Adem�s inicia la aplicaci�n (es el �nico m�dulo del proyecto donde encontraremos la funci�n �public static void main(String[] args)�.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**
## API — Endpoints principales

A continuación se muestran los endpoints públicos y ejemplos `curl` para los handlers principales del proyecto.

ProductHandler
- Crear producto
```
curl -X POST http://localhost:8080/api/v1/products \
	-H "Content-Type: application/json" \
	-d '{"name":"Producto A","branchId":"b1","stock":10}'
```

- Actualizar nombre de producto
```bash
curl -X PUT http://localhost:8080/api/v1/products/update-name/{id} \
   -H "Content-Type: application/json" \
   -d '{"name":"Producto Actualizado","branchId":"b1"}'
```
Reemplaza {id} por el ID real del producto.
 - Actualizar stock de producto
 ```bash
 curl -X PUT http://localhost:8080/api/v1/products/update-stock/{id} \
    -H "Content-Type: application/json" \
    -d '{"id":"{id}","stock":15}'
 ```

Reemplaza {id} por el ID real del producto.
- Eliminar producto
```
curl -X DELETE http://localhost:8080/api/v1/products/{id}
```
Reemplaza {id} por el ID real del producto.
- Obtener productos top stock por franquicia

```bash
curl -X GET "http://localhost:8080/api/v1/products/top-stock-products?franchiseId=f1"
```

BranchHandler
- Crear sucursal
```bash
curl -X POST http://localhost:8080/api/v1/branches \
   -H "Content-Type: application/json" \
   -d '{"name":"Sucursal 1","franchiseId":"f1"}'
```
- Actualizar sucursal
```bash
curl -X PUT http://localhost:8080/api/v1/branches/{id} \
   -H "Content-Type: application/json" \
   -d '{"name":"Sucursal Actualizada"}'
```
Reemplaza {id} por el ID real de la sucursal.

FranchiseHandler
- Crear franquicia
```bash
curl -X POST http://localhost:8080/api/v1/franchises \
   -H "Content-Type: application/json" \
   -d '{"name":"Franquicia 1"}'
```
- Actualizar franquicia
```bash
curl -X PUT http://localhost:8080/api/v1/franchises/{id} \
   -H "Content-Type: application/json" \
   -d '{"name":"Franquicia Actualizada"}'
```
Reemplaza {id} por el ID real de la franquicia.

# 📘 Consideraciones de Diseño y Evolución

Este documento detalla las decisiones técnicas y las recomendaciones estratégicas tomadas para el proyecto **Franchise Management**.

---

## 🏛️ Arquitectura y Patrones

### 1. Arquitectura Hexagonal (Clean Architecture)
El proyecto se organiza en capas desacopladas para garantizar que la lógica de negocio sea independiente de la tecnología externa:
* **Domain:** Modelos de negocio e interfaces de puertos.
* **UseCase:** Orquestación de la lógica de aplicación.
* **Infrastructure:** Adaptadores de persistencia (MongoDB) y puntos de entrada (REST).
* **Applications:** Configuración y arranque del servicio.

### 2. Reactividad con WebFlux
Se utiliza **Spring WebFlux** y **Project Reactor** (`Mono` / `Flux`) para lograr un modelo no bloqueante.
* **Escalabilidad:** Optimizado para alta concurrencia con un uso mínimo de hilos.
* **Restricción:** Se debe evitar cualquier operación bloqueante en los flujos principales; en caso de necesidad, se deben delegar a un `Scheduler` específico.

### 3. Puertos y Adaptadores
La comunicación entre capas se realiza mediante interfaces. Esto permite:
* Sustituir la base de datos o servicios externos sin afectar la lógica de negocio.
* Facilitar el testing mediante el uso de Mocks de los puertos.

---

## 🛠️ Estándares de Implementación

### Gestión de Datos
* **Modelado:** Uso de entidades inmutables en el dominio. Se emplean DTOs específicos como `TopStockProduct` para agregaciones complejas que combinan datos de sucursal y producto.
* **Consistencia:** Dado el uso de MongoDB, se favorece la **consistencia eventual**. Las transacciones se aplican solo donde el soporte de Mongo lo permite y la criticidad del negocio lo requiere.

### Validación y Errores
* **Entrada:** Validación declarativa mediante `@Valid` y DTOs de Request.
* **Excepciones:** Los casos de uso lanzan errores tipados (ej: `NotFoundException`). Los controladores/handlers mapean estos errores a respuestas HTTP estandarizadas a través de un `GlobalErrorHandler`.

### Seguridad e Idempotencia
* **Idempotencia:** Los endpoints de creación están diseñados para aceptar un `id` opcional, permitiendo que el cliente gestione reintentos sin duplicar datos.
* **Seguridad:** Actualmente en fase de diseño. Se recomienda la integración futura de **OAuth2/JWT**.

---

## 🚀 Operaciones y Despliegue

### Observabilidad
* **Logs:** Implementación de logs estructurados con `Slf4j`.
* **Métricas:** Exposición de *health checks* y métricas operacionales vía **Spring Boot Actuator**.
* **Recomendación:** Incorporar trazas distribuidas (OpenTelemetry) para entornos de nube.

### Estrategia de Pruebas
* **Unitarias:** Pruebas de lógica pura en Use Cases con JUnit y Mockito.
* **De Capa (Slice Tests):** Uso de `@WebFluxTest` y `WebTestClient` para validar endpoints sin levantar el contexto completo.
* **Integración:** Se recomienda el uso de contenedores (Testcontainers) o bases de datos embebidas para los adaptadores de persistencia.

### CI/CD y Entornos
* **Contenedorización:** Uso de `Dockerfile` para empaquetado estándar.
* **Secretos:** Gestión de credenciales mediante variables de entorno o proveedores de secretos (AWS Secrets Manager / Vault).
* **Infraestructura:** Se sugiere el uso de **IaC (Terraform)** y pipelines automatizados para el despliegue en clusters gestionados (EKS/GKE).

---




