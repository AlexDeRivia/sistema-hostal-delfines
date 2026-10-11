package datos;

import dominio.Administrador;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AdminDaoAT extends Dao<Administrador> {
    
    @Override
    public Administrador crear(Administrador obj) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Administrador eliminar(Administrador obj) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Administrador actualizar(Administrador obj) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Administrador buscar(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Administrador> listado() {
        List<Administrador> lista = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "administradores.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 6) { // Asegurarse de que la línea tenga el formato correcto
                    Administrador admin = new Administrador();
                    admin.setId(Integer.parseInt(partes[0]));
                    admin.setDni(partes[1]);
                    admin.setNombre(partes[2]);
                    admin.setApellido(partes[3]);
                    admin.setUsername(partes[4]);
                    admin.setClave(partes[5]);
                    lista.add(admin);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return lista;
    }
}
