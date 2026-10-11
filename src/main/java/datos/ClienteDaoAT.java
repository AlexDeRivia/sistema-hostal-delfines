package datos;

import dominio.Cliente;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoAT extends Dao<Cliente>{
    
    @Override
    public Cliente crear(Cliente obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "clientes.txt"), StandardOpenOption.APPEND);
            
            // Obtener los atributos del cliente
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            String dni = obj.getDni();
            String nombre = obj.getNombre();
            String apellido = obj.getApellido();
            
            // Construir la línea para escribir en el archivo
            String line = id + "," + dni + "," + nombre + "," + apellido;
            
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
    public Cliente eliminar(Cliente obj) {
        List<Cliente> clientes = listado();

        // Implementa la lógica para eliminar el cliente
        // Puedes, por ejemplo, crear una nueva lista sin el cliente que deseas eliminar
        clientes.removeIf(cliente -> cliente.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirClientesEnArchivo(clientes);

        return obj;
    }

    @Override
    public Cliente actualizar(Cliente obj) {
        List<Cliente> clientes = listado();

        // Implementa la lógica para actualizar el cliente
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == obj.getId()) {
                clientes.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirClientesEnArchivo(clientes);

        return obj;
    }

    @Override
    public Cliente buscar(int id) {
        List<Cliente> clientes = listado();

        // Implementa la lógica para buscar el cliente por ID
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }

        return null; // Cliente no encontrado
    }

    @Override
    public List<Cliente> listado() {
        List<Cliente> listaClientes = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "clientes.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 4) { // Asegurarse de que la línea tenga el formato correcto
                    Cliente cliente = new Cliente();
                    cliente.setId(Integer.parseInt(partes[0]));
                    cliente.setDni(partes[1]);
                    cliente.setNombre(partes[2]);
                    cliente.setApellido(partes[3]);
                    listaClientes.add(cliente);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaClientes;
    }

    private void escribirClientesEnArchivo(List<Cliente> clientes) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "clientes.txt"))) {
            for (Cliente cliente : clientes) {
                String line = cliente.getId() + "," + cliente.getDni() + "," + cliente.getNombre() + "," + cliente.getApellido();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public int obtenerUltimoId() {
        List<Cliente> cliente = listado();

        if (!cliente.isEmpty()) {
            Cliente ultimoCliente = cliente.get(cliente.size() - 1);
            return ultimoCliente.getId();
        } else {
            return 0; // Otra lógica si no hay jugadas
        }
    }

}
