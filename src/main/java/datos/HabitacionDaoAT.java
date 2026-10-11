package datos;

import dominio.Habitacion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDaoAT extends Dao<Habitacion>{
    @Override
    public Habitacion crear(Habitacion obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "habitaciones.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos de la habitación
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            int numero = obj.getNumero();
            int numeroCamas = obj.getNumeroCamas();
            boolean ventanaAfuera = obj.isVentanaAfuera();
            boolean disponible = obj.isDisponible();

            // Construir la línea para escribir en el archivo
            String line = id + "," + numero + "," + numeroCamas + "," + ventanaAfuera + "," + disponible;

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
    public Habitacion eliminar(Habitacion obj) {
        List<Habitacion> habitaciones = listado();

        // Implementa la lógica para eliminar la habitación
        habitaciones.removeIf(habitacion -> habitacion.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirHabitacionesEnArchivo(habitaciones);

        return obj;
    }

    @Override
    public Habitacion actualizar(Habitacion obj) {
        List<Habitacion> habitaciones = listado();

        // Implementa la lógica para actualizar la habitación
        for (int i = 0; i < habitaciones.size(); i++) {
            if (habitaciones.get(i).getId() == obj.getId()) {
                habitaciones.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirHabitacionesEnArchivo(habitaciones);

        return obj;
    }

    @Override
    public Habitacion buscar(int id) {
        List<Habitacion> habitaciones = listado();

        // Implementa la lógica para buscar la habitación por ID
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.getId() == id) {
                return habitacion;
            }
        }

        return null; // Habitación no encontrada
    }

    @Override
    public List<Habitacion> listado() {
        List<Habitacion> listaHabitaciones = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "habitaciones.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 5) { // Asegurarse de que la línea tenga el formato correcto
                    Habitacion habitacion = new Habitacion();
                    habitacion.setId(Integer.parseInt(partes[0]));
                    habitacion.setNumero(Integer.parseInt(partes[1]));
                    habitacion.setNumeroCamas(Integer.parseInt(partes[2]));
                    habitacion.setVentanaAfuera(Boolean.parseBoolean(partes[3]));
                    habitacion.setDisponible(Boolean.parseBoolean(partes[4]));
                    listaHabitaciones.add(habitacion);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaHabitaciones;
    }

    private void escribirHabitacionesEnArchivo(List<Habitacion> habitaciones) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "habitaciones.txt"))) {
            for (Habitacion habitacion : habitaciones) {
                String line = habitacion.getId() + "," + habitacion.getNumero() + "," + habitacion.getNumeroCamas()
                        + "," + habitacion.isVentanaAfuera() + "," + habitacion.isDisponible();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<Habitacion> habitaciones = listado();

        if (!habitaciones.isEmpty()) {
            Habitacion ultimaHabitacion = habitaciones.get(habitaciones.size() - 1);
            return ultimaHabitacion.getId();
        } else {
            return 0; // Otra lógica si no hay habitaciones
        }
    }
}
