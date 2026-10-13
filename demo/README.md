# Demo interactiva de Hostal Delfines

Versión estática para explorar los flujos principales sin iniciar GlassFish. Conserva la identidad visual del sistema y permite:

- Consultar habitaciones y sus próximas reservas.
- Crear, editar, buscar y cancelar reservas. La tarifa y la comprobación de horarios siguen las reglas principales del sistema Java.
- Registrar clientes ficticios.
- Registrar una venta y actualizar el stock de ejemplo.

Los datos se guardan exclusivamente en `localStorage` del navegador. No hay servidor, base de datos, cuentas ni intercambio de datos entre visitantes. El botón **Reiniciar** restaura los ejemplos. Esta demo no sustituye al WAR de la raíz del repositorio ni representa un sistema de producción.

## Vista local

Servir esta carpeta con cualquier servidor estático, por ejemplo:

```sh
python -m http.server 4173 --directory demo
```

Abrir `http://localhost:4173/`.

## Cloudflare Pages

Conectar el repositorio privado mediante la integración de GitHub. Configurar **Root directory** como `demo`, sin comando de compilación y con **Build output directory** `.`. Publicar solo este directorio; no se necesitan Pages Functions, Workers, D1, KV ni variables de entorno. Mantener el plan Free.

Antes de compartir el enlace, recorrer todos los flujos en escritorio y móvil, comprobar que el botón Reiniciar restaura los datos y verificar que no aparezca ninguna solicitud a una API del sistema.
