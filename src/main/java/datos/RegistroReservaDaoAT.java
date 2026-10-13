package datos;

import dominio.RegistroReserva;
import dominio.Reserva;
import dominio.Turno;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class RegistroReservaDaoAT extends Dao<RegistroReserva>{
    
    @Override
    public RegistroReserva crear(RegistroReserva obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "RegistroReservas.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos del registro de reserva
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            obj.setId(id);
            String fecha = obj.getFecha();
            double liquidacion = obj.getLiquidacion();
            Turno turno = obj.getTurno();
            int idempleado = obj.getEmpleado().getId();
            //List<Reserva> listReservas = obj.getListReservas();

            // Construir la línea para escribir en el archivo
            String line = id + "," + idempleado +"," + fecha + "," + turno + "," + liquidacion;

            // Escribir la línea en el archivo
            bw.write(line);
            bw.newLine();

            bw.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return obj;
    }

    @Override
    public RegistroReserva eliminar(RegistroReserva obj) {
        List<RegistroReserva> registrosReserva = listado();

        // Implementa la lógica para eliminar el registro de reserva
        registrosReserva.removeIf(registroReserva -> registroReserva.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirRegistrosReservaEnArchivo(registrosReserva);

        return obj;
    }

    @Override
    public RegistroReserva actualizar(RegistroReserva obj) {
        List<RegistroReserva> registrosReserva = listado();

        // Implementa la lógica para actualizar el registro de reserva
        for (int i = 0; i < registrosReserva.size(); i++) {
            if (registrosReserva.get(i).getId() == obj.getId()) {
                registrosReserva.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirRegistrosReservaEnArchivo(registrosReserva);

        return obj;
    }

    @Override
    public RegistroReserva buscar(int id) {
        List<RegistroReserva> registrosReserva = listado();

        // Implementa la lógica para buscar el registro de reserva por ID
        for (RegistroReserva registroReserva : registrosReserva) {
            if (registroReserva.getId() == id) {
                return registroReserva;
            }
        }

        return null; // Registro de reserva no encontrado
    }

    @Override
    public List<RegistroReserva> listado() {
        List<RegistroReserva> listaRegistrosReserva = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "RegistroReservas.txt"))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");
                
                if (partes.length == 5) { // Asegurarse de que la línea tenga el formato correcto
                    RegistroReserva registroReserva = new RegistroReserva();
                    registroReserva.setId(Integer.parseInt(partes[0]));
                    EmpleadoDaoAT edat = new EmpleadoDaoAT();
                    int idEmpleado = Integer.parseInt(partes[1]);
                    dominio.Empleado empleado = edat.buscar(idEmpleado);
                    if (empleado == null) {
                        empleado = new dominio.Empleado();
                        empleado.setId(idEmpleado);
                    }
                    registroReserva.setEmpleado(empleado);
                    registroReserva.setFecha(String.valueOf(partes[2]));
                    // Asegúrate de que el nombre del turno está en mayúsculas para el valor de Turno enum
                    try {
                        registroReserva.setTurno(Turno.valueOf(partes[3].toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // Manejar la excepción, por ejemplo, imprimir un mensaje de error
                        System.err.println("Error al convertir el nombre del turno a un valor de enum Turno: " + e.getMessage());
                    }
                    // También deberías cargar las reservas asociadas al registro de reserva
                    ReservaDaoAT reservaDao = new ReservaDaoAT(); // Asume que existe un DAO para Reserva
                    List<Reserva> listReservas = reservaDao.listadoPorRegistroReserva(registroReserva.getId());
                    registroReserva.setListReservas((ArrayList<Reserva>) listReservas);
                    registroReserva.setLiquidacion(Double.parseDouble(partes[4]));
                    listaRegistrosReserva.add(registroReserva);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }
        return listaRegistrosReserva;
    }

    private void escribirRegistrosReservaEnArchivo(List<RegistroReserva> registrosReserva) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "RegistroReservas.txt"))) {
            for (RegistroReserva registroReserva : registrosReserva) {
                String line = registroReserva.getId() + "," + registroReserva.getEmpleado().getId() + "," +
                        registroReserva.getFecha() + "," + registroReserva.getTurno() + "," + registroReserva.getLiquidacion();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private String obtenerIdReservas(List<Reserva> listReservas) {
        StringBuilder ids = new StringBuilder();
        for (Reserva reserva : listReservas) {
            ids.append(reserva.getId()).append(",");
        }
        // Elimina la última coma si hay reservas
        if (ids.length() > 0) {
            ids.deleteCharAt(ids.length() - 1);
        }
        return ids.toString();
    }

    public int obtenerUltimoId() {
        List<RegistroReserva> registrosReserva = this.listado();

        if (!registrosReserva.isEmpty()) {
            RegistroReserva ultimoRegistroReserva = registrosReserva.get(registrosReserva.size() - 1);
            return ultimoRegistroReserva.getId();
        } else {
            return 0; // Otra lógica si no hay registros de reserva
        }
    }

    
}
