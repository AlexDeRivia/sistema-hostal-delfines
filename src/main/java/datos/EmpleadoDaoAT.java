package datos;

import dominio.Empleado;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDaoAT extends Dao<Empleado> {

    @Override
    public Empleado crear(Empleado obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "empleados.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos del empleado
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            String dni = obj.getDni();
            String nombre = obj.getNombre();
            String apellido = obj.getApellido();
            String username = obj.getUsername();
            String clave = obj.getClave();

            // Construir la línea para escribir en el archivo
            String line = id + "," + dni + "," + nombre + "," + apellido + "," + username + "," + clave;

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
    public Empleado eliminar(Empleado obj) {
        List<Empleado> empleados = listado();

        // Implementa la lógica para eliminar el empleado
        empleados.removeIf(empleado -> empleado.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirEmpleadosEnArchivo(empleados);

        return obj;
    }

    @Override
    public Empleado actualizar(Empleado obj) {
        List<Empleado> empleados = listado();

        // Implementa la lógica para actualizar el empleado
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getId() == obj.getId()) {
                empleados.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirEmpleadosEnArchivo(empleados);

        return obj;
    }

    @Override
    public Empleado buscar(int id) {
        List<Empleado> empleados = listado();

        // Implementa la lógica para buscar el empleado por ID
        for (Empleado empleado : empleados) {
            if (empleado.getId() == id) {
                return empleado;
            }
        }

        return null; // Empleado no encontrado
    }

    @Override
    public List<Empleado> listado() {
        List<Empleado> listaEmpleados = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "empleados.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 6) { // Asegurarse de que la línea tenga el formato correcto
                    Empleado empleado = new Empleado();
                    empleado.setId(Integer.parseInt(partes[0]));
                    empleado.setDni(partes[1]);
                    empleado.setNombre(partes[2]);
                    empleado.setApellido(partes[3]);
                    empleado.setUsername(partes[4]);
                    empleado.setClave(partes[5]);
                    listaEmpleados.add(empleado);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaEmpleados;
    }

    private void escribirEmpleadosEnArchivo(List<Empleado> empleados) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "empleados.txt"))) {
            for (Empleado empleado : empleados) {
                String line = empleado.getId() + "," + empleado.getDni() + "," + empleado.getNombre() + "," + empleado.getApellido()+ "," +empleado.getUsername() + "," +empleado.getClave();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<Empleado> empleado = listado();

        if (!empleado.isEmpty()) {
            Empleado ultimaEmpleado = empleado.get(empleado.size() - 1);
            return ultimaEmpleado.getId();
        } else {
            return 0; // Otra lógica si no hay jugadas
        }
    }
}