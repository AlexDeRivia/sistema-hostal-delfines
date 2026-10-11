package datos;

import dominio.Usuario;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoAT extends Dao<Usuario>{
    @Override
    public Usuario crear(Usuario obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "usuarios.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos del usuario
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            String username = obj.getUsername();
            String clave = obj.getClave();

            // Construir la línea para escribir en el archivo
            String line = id + "," + username + "," + clave;

            // Escribir la línea en el archivo
            bw.write(line);
            bw.newLine();

            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Usuario eliminar(Usuario obj) {
        List<Usuario> usuarios = listado();

        // Implementa la lógica para eliminar el usuario
        usuarios.removeIf(usuario -> usuario.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirUsuariosEnArchivo(usuarios);

        return obj;
    }

    @Override
    public Usuario actualizar(Usuario obj) {
        List<Usuario> usuarios = listado();

        // Implementa la lógica para actualizar el usuario
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == obj.getId()) {
                usuarios.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirUsuariosEnArchivo(usuarios);

        return obj;
    }

    @Override
    public Usuario buscar(int id) {
        List<Usuario> usuarios = listado();

        // Implementa la lógica para buscar el usuario por ID
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }

        return null; // Usuario no encontrado
    }

    @Override
    public List<Usuario> listado() {
        List<Usuario> listaUsuarios = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "usuarios.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 3) { // Asegurarse de que la línea tenga el formato correcto
                    Usuario usuario = new Usuario();
                    usuario.setId(Integer.parseInt(partes[0]));
                    usuario.setUsername(partes[1]);
                    usuario.setClave(partes[2]);
                    listaUsuarios.add(usuario);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaUsuarios;
    }

    private void escribirUsuariosEnArchivo(List<Usuario> usuarios) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "usuarios.txt"))) {
            for (Usuario usuario : usuarios) {
                String line = usuario.getId() + "," + usuario.getUsername() + "," + usuario.getClave();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<Usuario> usuarios = listado();

        if (!usuarios.isEmpty()) {
            Usuario ultimoUsuario = usuarios.get(usuarios.size() - 1);
            return ultimoUsuario.getId();
        } else {
            return 0; // Otra lógica si no hay usuarios
        }
    }
}
