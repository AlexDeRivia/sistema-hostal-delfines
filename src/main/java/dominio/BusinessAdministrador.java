package dominio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class BusinessAdministrador {
    
    public Administrador login(String username, String clave) {
        String configuredUser = System.getenv("HOSTAL_ADMIN_USER");
        String configuredPassword = System.getenv("HOSTAL_ADMIN_PASSWORD");
        if (username == null || clave == null || configuredUser == null || configuredPassword == null
                || configuredUser.isBlank() || configuredPassword.isBlank()) {
            return null;
        }
        if (!MessageDigest.isEqual(username.getBytes(StandardCharsets.UTF_8),
                configuredUser.getBytes(StandardCharsets.UTF_8))
                || !MessageDigest.isEqual(clave.getBytes(StandardCharsets.UTF_8),
                        configuredPassword.getBytes(StandardCharsets.UTF_8))) {
            return null;
        }
        Administrador admin = new Administrador();
        admin.setUsername(configuredUser);
        admin.setNombre(configuredUser);
        admin.setApellido("");
        return admin;
    }
}
