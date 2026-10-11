package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Connection connect;
    public static Connection getInstance() {
        String url = System.getenv("HOSTAL_DB_URL");
        if (url == null || url.isBlank()) {
            return null;
        }
        if (connect == null) {
            try {
                connect = DriverManager.getConnection(url,
                        System.getenv("HOSTAL_DB_USER"), System.getenv("HOSTAL_DB_PASSWORD"));
            } catch (SQLException e) {
                throw new IllegalStateException("No se pudo conectar a la base de datos configurada", e);
            }
        }
        return connect;
    }
}

