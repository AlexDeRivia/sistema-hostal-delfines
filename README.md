# Sistema Hostal Delfines

<p align="center">
  <a href="https://sistema-hostal-delfines.pages.dev/" aria-label="Abrir la demo interactiva de Sistema Hostal Delfines">
    <img src="docs/readme-cover.svg" alt="Sistema Hostal Delfines: recepción, reservas y operación comercial junto al mar" width="100%">
  </a>
</p>

<p align="center">
  <a href="https://sistema-hostal-delfines.pages.dev/"><strong>Explorar la demo</strong></a>
  &nbsp;·&nbsp;
  <a href="#el-sistema">Conocer el sistema</a>
  &nbsp;·&nbsp;
  <a href="#ejecutar-el-sistema-completo">Ejecutarlo localmente</a>
</p>

Sistema de gestión para un hostal: organiza reservas y habitaciones, registra clientes y conecta la recepción con las operaciones de compras, inventario y ventas. El repositorio contiene la **aplicación completa en Jakarta EE** y una **demo estática interactiva** pensada para explorar sus flujos sin instalar un servidor.

> **Para empezar:** [abre la demo pública](https://sistema-hostal-delfines.pages.dev/), consulta las habitaciones y crea una reserva. Todo lo que registres allí es ficticio y queda únicamente en tu navegador.

## El sistema

| Área | Aplicación Jakarta EE | Demo pública |
| :-- | :-- | :-- |
| Recepción | Reservas, estados, horarios, pagos y comprobantes | Crear, editar, buscar y cancelar reservas de ejemplo |
| Habitaciones | Administración y consulta de disponibilidad | Consulta de habitaciones y próximas estancias |
| Clientes | Registro y mantenimiento | Registro de clientes ficticios |
| Operación comercial | Productos, proveedores, compras, ventas y movimientos | Venta de ejemplo con actualización de stock local |
| Acceso | Sesiones de administrador y empleado | Acceso libre, sin cuentas |

La demo reproduce los flujos principales de recepción y ventas, pero **no es el WAR ejecutándose en Cloudflare**. El código del sistema completo vive en [`src/main/`](src/main/); la versión pública está en [`demo/`](demo/README.md).

### Un recorrido de tres minutos

1. En [Habitaciones](https://sistema-hostal-delfines.pages.dev/#habitaciones), revisa la capacidad y las próximas estancias.
2. En [Reservas](https://sistema-hostal-delfines.pages.dev/#reservas), registra una estancia; prueba un horario ocupado para ver la validación.
3. En [Ventas](https://sistema-hostal-delfines.pages.dev/#ventas), agrega productos y registra una venta para ver cómo cambia el stock de ejemplo.

Puedes volver al punto de partida con **Reiniciar**. La demo guarda los cambios en `localStorage`: persisten al recargar, no se comparten entre visitantes y no llegan a una base de datos. No introduzcas datos reales de huéspedes.

## Cómo está construido

| Capa | Tecnología y función |
| :-- | :-- |
| Interfaz completa | JSP, CSS y JavaScript en una aplicación web Jakarta EE 10 |
| Lógica | Java 11, servlets y clases de dominio para las operaciones del hostal |
| Empaquetado y ejecución | Maven genera un WAR; el contenedor incluido utiliza GlassFish Embedded 7.1.0 y Java 17 |
| Persistencia activa del WAR | Archivos de texto en un directorio configurable y persistente; las clases JDBC heredadas son opcionales |
| Demo pública | HTML, CSS y JavaScript estáticos en Cloudflare Pages; estado local en el navegador |

```text
src/main/java/dominio/   Reglas y entidades del negocio
src/main/java/datos/     Acceso a archivos y adaptadores JDBC heredados
src/main/java/web/       Servlets, control de acceso y datos de ejemplo
src/main/webapp/         Vistas JSP y recursos del sistema completo
demo/                   Demo interactiva independiente
Dockerfile              Construcción del WAR y ejecución en GlassFish
```

<details>
<summary><strong>¿Por qué hay dos versiones?</strong></summary>

El sistema completo necesita GlassFish y almacenamiento persistente. La demo pública ofrece una visita inmediata, sin arranque en frío ni cuentas de prueba, para mostrar los flujos principales. Su estado es individual por navegador; no reemplaza el almacenamiento ni la gestión de usuarios del WAR.

</details>

## Ejecutar el sistema completo

Necesitas **JDK 11 o superior** y **Maven 3.8 o superior** para compilar. Para ejecutar el WAR fuera de Docker, usa **GlassFish 7.x**. El `Dockerfile` ya incluye el entorno de construcción y GlassFish Embedded.

```sh
mvn clean package
```

El artefacto queda en `target/sistema-hostal-delfines-1.0.war`. Para construirlo y ejecutarlo en un contenedor:

```sh
docker build -t hostal-delfines .
docker run --rm -p 127.0.0.1:8080:8080 \
  -e HOSTAL_ADMIN_USER -e HOSTAL_ADMIN_PASSWORD \
  -e HOSTAL_DEMO_SEED=true \
  -v hostal-delfines-data:/data hostal-delfines
```

Define `HOSTAL_ADMIN_USER` y `HOSTAL_ADMIN_PASSWORD` en el entorno **antes** de ejecutar el contenedor. Usa una contraseña nueva y exclusiva; no la incluyas en comandos versionados. Después abre `http://127.0.0.1:8080/application/`. El puerto del ejemplo escucha solo en tu equipo.

### Configuración

| Variable | Función |
| :-- | :-- |
| `HOSTAL_ADMIN_USER` | Usuario inicial de administrador; necesario para iniciar sesión. |
| `HOSTAL_ADMIN_PASSWORD` | Contraseña inicial de administrador; necesaria para iniciar sesión. |
| `HOSTAL_DATA_DIR` | Directorio escribible para los archivos de datos. En Docker se fija en `/data`; fuera de Docker, el valor predeterminado es `~/.hostal-delfines`. |
| `HOSTAL_DEMO_SEED` | Opcional. Con `true`, crea datos ficticios una sola vez si los catálogos están vacíos. |
| `HOSTAL_DB_URL`, `HOSTAL_DB_USER`, `HOSTAL_DB_PASSWORD` | Opcionales para clases JDBC heredadas. Los flujos activos de esta configuración usan archivos y no requieren MySQL. |

El volumen `hostal-delfines-data` conserva los datos entre reinicios. El sembrado de ejemplo no sobrescribe registros existentes; para partir de cero, utiliza un volumen nuevo y vacío.

<details>
<summary><strong>Notas para un servidor accesible desde Internet</strong></summary>

El modo de archivos está pensado para **una sola instancia y uso ligero**. No ofrece transacciones entre reservas, compras, ventas e inventario; una operación real con varias instancias necesita almacenamiento transaccional.

Publica el WAR detrás de un proxy HTTPS, conserva el volumen y configura las credenciales como secretos del proveedor. Limita los intentos de inicio de sesión y no expongas la consola de GlassFish. Las cookies de sesión utilizan la marca `Secure`, por lo que el navegador necesita HTTPS para mantener la sesión. Si la antigua contraseña de administrador del repositorio original se reutilizó en otro lugar, revócala allí.

</details>

## Demo estática y publicación

Para abrir la demo en local basta un servidor de archivos estáticos:

```sh
python -m http.server 4173 --directory demo
```

Visita `http://127.0.0.1:4173/`. No necesita GlassFish, MySQL, claves ni variables de entorno.

La [demo publicada](https://sistema-hostal-delfines.pages.dev/) usa **Cloudflare Pages Direct Upload**. No está conectada a GitHub: subir cambios al repositorio no actualiza automáticamente el sitio. El procedimiento para publicar una nueva versión está en la [guía de la demo](demo/README.md).
