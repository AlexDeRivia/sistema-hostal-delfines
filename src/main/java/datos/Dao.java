package datos;

import java.sql.Connection;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class Dao<T> {
    public static final String PATH = initializeDataDirectory();
    public Connection connect = Conexion.getInstance();

    private static String initializeDataDirectory() {
        String configured = System.getenv("HOSTAL_DATA_DIR");
        Path directory = configured == null || configured.isBlank()
                ? Paths.get(System.getProperty("user.home"), ".hostal-delfines")
                : Paths.get(configured);
        try {
            Files.createDirectories(directory);
            for (String name : new String[] {
                "clientes.txt", "compras.txt", "empleados.txt", "facturas.txt",
                "habitaciones.txt", "lineascompra.txt", "lineasventa.txt",
                "LineaVentas.txt", "productos.txt", "proveedores.txt",
                "RegistroReservas.txt", "registroscompra.txt", "RegistroVentas.txt",
                "reservas.txt", "usuarios.txt", "Ventas.txt"
            }) {
                Path file = directory.resolve(name);
                if (Files.notExists(file)) {
                    Files.createFile(file);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se puede preparar el directorio de datos: " + directory, e);
        }
        return directory.toAbsolutePath().toString() + java.io.File.separator;
    }
    
    public abstract T crear(T obj);
    public abstract T eliminar(T obj);
    public abstract T actualizar(T obj);
    public abstract T buscar(int id);
    public abstract List<T> listado();
}
