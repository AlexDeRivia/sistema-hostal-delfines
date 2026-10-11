package datos;

import dominio.Cliente;
import dominio.EstadoReserva;
import dominio.Habitacion;
import dominio.RegistroReserva;
import dominio.Reserva;
import dominio.Turno;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ReservaDaoAT extends Dao<Reserva> {

    @Override
    public Reserva crear(Reserva obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "reservas.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos de la reserva
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            int numeroHuespedes = obj.getNumeroHuespedes();
            double costoEfectivo = obj.getCostoEfectivo();
            double costoYape = obj.getCostoYape();
            String horasReservadas = obj.getHoras_reservadas();
            EstadoReserva estadoReserva = obj.getEstadoReserva();
            Cliente cliente = obj.getCliente();
            Habitacion habitacion = obj.getHabitacion();
            String horaIngreso = obj.getHora_ingreso();
            String horaSalida = obj.getHora_salida();
            int id_registroReserva = obj.getRegistroreserva_id();

            // Construir la línea para escribir en el archivo
            String line = id + "," + numeroHuespedes + "," + costoEfectivo + "," + costoYape + ","
                    + horasReservadas + "," + estadoReserva + "," + cliente.getId() + "," + habitacion.getId()
                    + "," + horaIngreso + "," + horaSalida + "," + id_registroReserva;

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
    public Reserva eliminar(Reserva obj) {
        List<Reserva> reservas = listado();

        // Implementa la lógica para eliminar la reserva
        reservas.removeIf(reserva -> reserva.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirReservasEnArchivo(reservas);

        return obj;
    }

    @Override
    public Reserva actualizar(Reserva obj) {
        List<Reserva> reservas = listado();

        // Implementa la lógica para actualizar la reserva
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == obj.getId()) {
                reservas.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirReservasEnArchivo(reservas);

        return obj;
    }

    @Override
    public Reserva buscar(int id) {
        List<Reserva> reservas = listado();

        // Implementa la lógica para buscar la reserva por ID
        for (Reserva reserva : reservas) {
            if (reserva.getId() == id) {
                return reserva;
            }
        }

        return null; // Reserva no encontrada
    }

    @Override
    public List<Reserva> listado() {
        List<Reserva> listaReservas = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "reservas.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 11) { // Asegurarse de que la línea tenga el formato correcto
                    Reserva reserva = new Reserva();
                    reserva.setId(Integer.parseInt(partes[0]));
                    reserva.setNumeroHuespedes(Integer.parseInt(partes[1]));
                    reserva.setCostoEfectivo(Double.parseDouble(partes[2]));
                    reserva.setCostoYape(Double.parseDouble(partes[3]));
                    reserva.setHoras_reservadas(partes[4]);
                    reserva.setEstadoReserva(EstadoReserva.valueOf(partes[5]));

                    // Cargar el Cliente a partir de su ID
                    int idCliente = Integer.parseInt(partes[6]);
                    ClienteDaoAT clienteDao = new ClienteDaoAT(); // Asume que existe un DAO para Cliente
                    Cliente cliente = clienteDao.buscar(idCliente);
                    reserva.setCliente(cliente);

                    // Cargar la Habitacion a partir de su ID
                    int idHabitacion = Integer.parseInt(partes[7]);
                    HabitacionDaoAT habitacionDao = new HabitacionDaoAT(); // Asume que existe un DAO para Habitacion
                    Habitacion habitacion = habitacionDao.buscar(idHabitacion);
                    reserva.setHabitacion(habitacion);

                    reserva.setHora_ingreso(partes[8]);
                    reserva.setHora_salida(partes[9]);
                    int idRegistroReservaEnReserva = Integer.parseInt(partes[10]);
                    reserva.setRegistroreserva_id(idRegistroReservaEnReserva); // Añadir el ID del registro de reserva

                    listaReservas.add(reserva);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaReservas;
    }
    
    public List<Reserva> listadoPorRegistroReserva(int idRegistroReserva) {
        List<Reserva> listaReservas = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "reservas.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 11) { // Asegurarse de que la línea tenga el formato correcto
                    int idReserva = Integer.parseInt(partes[0]);
                    int idRegistroReservaEnReserva = Integer.parseInt(partes[10]);

                    if (idRegistroReservaEnReserva == idRegistroReserva) {
                        Reserva reserva = new Reserva();
                        reserva.setId(idReserva);
                        reserva.setNumeroHuespedes(Integer.parseInt(partes[1]));
                        reserva.setCostoEfectivo(Double.parseDouble(partes[2]));
                        reserva.setCostoYape(Double.parseDouble(partes[3]));
                        reserva.setHoras_reservadas(partes[4]);
                        reserva.setEstadoReserva(EstadoReserva.valueOf(partes[5]));

                        // Puedes cargar el Cliente y la Habitación según su ID usando los DAO correspondientes
                        ClienteDaoAT clienteDao = new ClienteDaoAT();
                        Cliente cliente = clienteDao.buscar(Integer.parseInt(partes[6]));
                        reserva.setCliente(cliente);

                        HabitacionDaoAT habitacionDao = new HabitacionDaoAT();
                        Habitacion habitacion = habitacionDao.buscar(Integer.parseInt(partes[7]));
                        reserva.setHabitacion(habitacion);

                        reserva.setHora_ingreso(partes[8]);
                        reserva.setHora_salida(partes[9]);

                        listaReservas.add(reserva);
                    }
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaReservas;
    }

    public List<Reserva> listadoReservaCliente(int idCliente) {
        List<Reserva> listaReservasCliente = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "reservas.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 11) { // Asegurarse de que la línea tenga el formato correcto
                    int idReserva = Integer.parseInt(partes[0]);
                    int idClienteEnReserva = Integer.parseInt(partes[6]);

                    if (idClienteEnReserva == idCliente) {
                        Reserva reserva = new Reserva();
                        reserva.setId(idReserva);
                        reserva.setNumeroHuespedes(Integer.parseInt(partes[1]));
                        reserva.setCostoEfectivo(Double.parseDouble(partes[2]));
                        reserva.setCostoYape(Double.parseDouble(partes[3]));
                        reserva.setHoras_reservadas(partes[4]);
                        reserva.setEstadoReserva(EstadoReserva.valueOf(partes[5]));

                        // Puedes cargar la Habitación según su ID usando el DAO correspondiente
                        HabitacionDaoAT habitacionDao = new HabitacionDaoAT();
                        Habitacion habitacion = habitacionDao.buscar(Integer.parseInt(partes[7]));
                        reserva.setHabitacion(habitacion);

                        reserva.setHora_ingreso(partes[8]);
                        reserva.setHora_salida(partes[9]);

                        listaReservasCliente.add(reserva);
                    }
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaReservasCliente;
    }

    public List<Reserva> listadoPorFecha(String fecha) {
        List<Reserva> listaReservasPorFecha = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "RegistroReservas.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 4) { // Asegurarse de que la línea tenga el formato correcto
                    RegistroReserva registroReserva = new RegistroReserva();
                    registroReserva.setId(Integer.parseInt(partes[0]));
                    registroReserva.setFecha(partes[1]);
                    registroReserva.setLiquidacion(Double.parseDouble(partes[2]));

                    // Manejar la conversión de String a enum para el turno
                    try {
                        registroReserva.setTurno(Turno.valueOf(partes[3]));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error al convertir el nombre del turno a un valor de enum Turno: " + e.getMessage());
                    }

                    // Cargar las reservas asociadas al registro de reserva
                    ReservaDaoAT reservaDao = new ReservaDaoAT();
                    List<Reserva> listReservas = reservaDao.listadoPorRegistroReserva(registroReserva.getId());

                    // Filtrar las reservas por la fecha especificada
                    for (Reserva reserva : listReservas) {
                        // Utilizar la fecha de RegistroReserva para filtrar
                        if (registroReserva.getFecha().equals(fecha)) {
                            listaReservasPorFecha.add(reserva);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaReservasPorFecha;
    }

    public List<Cliente> clientesConReservasActivas() {
        List<Cliente> clientesConReservasActivas = new ArrayList<>();

        for (Reserva reserva : listado()) {
            // Obtén la habitación asociada a la reserva
            Habitacion habitacion = reserva.getHabitacion();

            // Verifica si la habitación está ocupada (disponible == false) y si la reserva está activa
            if (!habitacion.isDisponible() && reserva.getEstadoReserva() == EstadoReserva.CONFIRMADO) {
                Cliente cliente = reserva.getCliente();

                // Agrega el cliente a la lista si aún no está presente
                if (!clientesConReservasActivas.contains(cliente)) {
                    clientesConReservasActivas.add(cliente);
                }
            }
        }

        return clientesConReservasActivas;
    }

    private void escribirReservasEnArchivo(List<Reserva> reservas) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "reservas.txt"))) {
            for (Reserva reserva : reservas) {
                String line = reserva.getId() + "," + reserva.getNumeroHuespedes() + "," + reserva.getCostoEfectivo()
                        + "," + reserva.getCostoYape() + "," + reserva.getHoras_reservadas() + ","
                        + reserva.getEstadoReserva() + "," + reserva.getCliente().getId() + ","
                        + reserva.getHabitacion().getId() + "," + reserva.getHora_ingreso() + ","
                        + reserva.getHora_salida() + "," + reserva.getRegistroreserva_id();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<Reserva> reservas = listado();

        if (!reservas.isEmpty()) {
            Reserva ultimaReserva = reservas.get(reservas.size() - 1);
            return ultimaReserva.getId();
        } else {
            return 0; // Otra lógica si no hay reservas
        }
    }
}
