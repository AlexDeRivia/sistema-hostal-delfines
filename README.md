# Sistema Hostal Delfines

Aplicación web Jakarta EE 10 para administrar reservas, habitaciones, empleados, compras y ventas. Genera un archivo WAR y usa archivos de texto como almacenamiento principal.

## Requisitos

- JDK 11 o superior y Maven 3.8 o superior para compilar.
- GlassFish 7.x (Jakarta EE 10) para ejecutar el WAR. El `Dockerfile` usa GlassFish Embedded 7.1.0 y Java 17.
- Un directorio **persistente y escribible** para los datos. No se requiere MySQL en el modo de archivos.

## Configuración

Definir las variables de entorno en el servidor, nunca en el repositorio:

| Variable | Uso |
| --- | --- |
| `HOSTAL_ADMIN_USER` | Usuario administrador inicial, obligatorio para iniciar sesión como administrador. |
| `HOSTAL_ADMIN_PASSWORD` | Contraseña nueva y exclusiva del administrador, obligatoria. |
| `HOSTAL_DATA_DIR` | Directorio persistente para los archivos de datos. Si falta, se usa `~/.hostal-delfines`. |
| `HOSTAL_DEMO_SEED` | Opcional: `true` carga habitaciones, clientes, productos, un proveedor y una reserva ficticios al iniciar con datos vacíos. |

La aplicación crea los archivos de datos vacíos la primera vez que los necesita. Para una demo pública, usar únicamente datos ficticios y un volumen persistente montado en `/data`. El sistema no importa datos del repositorio anterior.

El almacenamiento en archivos está pensado para una sola instancia y una demo de uso ligero. No ofrece transacciones entre compras, ventas, inventario y registros; para operación comercial real o varias réplicas se necesita una base de datos transaccional.

Los datos de ejemplo se cargan una sola vez si los catálogos están vacíos. Para reiniciar una demo, usar un volumen nuevo y vacío; la aplicación no elimina datos existentes.

`HOSTAL_DB_URL`, `HOSTAL_DB_USER` y `HOSTAL_DB_PASSWORD` son opcionales para las clases JDBC heredadas. Los flujos activos usan archivos y no requieren base de datos. No configurar estas variables para la demo de archivos.

## Compilación

```sh
mvn clean package
```

El resultado es `target/sistema-hostal-delfines-1.0.war`. Para el contenedor:

```sh
docker build -t hostal-delfines .
docker run --rm -p 127.0.0.1:8080:8080 \
  -e HOSTAL_ADMIN_USER -e HOSTAL_ADMIN_PASSWORD \
  -v hostal-delfines-data:/data hostal-delfines
```

Definir las dos variables de administrador en el entorno antes de ejecutar el ejemplo. La aplicación queda bajo `/application/`. El ejemplo solo escucha en localhost. Para acceso desde Internet, publicar el puerto de la aplicación mediante un proxy con HTTPS, limitar intentos en `/SvLoginAdmin` y `/SvLoginEmpleado`, conservar el volumen y configurar las variables como secretos del proveedor. El proxy debe enviar HSTS. No exponer una consola de administración de GlassFish. Las cookies de sesión tienen la marca `Secure`, por lo que el navegador necesita HTTPS para mantener la sesión.

## Seguridad de los datos

Las contraseñas de empleados nuevos se guardan con PBKDF2. Una contraseña antigua en texto plano se reemplaza por su hash tras un inicio de sesión correcto. La contraseña de administrador antigua que apareció en el historial previo debe revocarse donde se haya reutilizado. Copiar únicamente código fuente y recursos al repositorio nuevo; no copiar `target/`, archivos de datos, `.env` ni la historia Git anterior.
