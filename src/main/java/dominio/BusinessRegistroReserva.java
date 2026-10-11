package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BusinessRegistroReserva {
    Dao<RegistroReserva> rreservaDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getRegistroReservaDAO();
    
    public List<RegistroReserva> listado() {
        return rreservaDAO.listado();
    }
    
    public RegistroReserva crear(RegistroReserva obj) {
        return rreservaDAO.crear(obj);
    }
    
    public RegistroReserva actualizar(RegistroReserva obj) {
        return rreservaDAO.actualizar(obj);
    }

    public RegistroReserva registroReservaDiario(Turno turno) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        String fecha = fechaHoraActual.format(formatter);

        List<RegistroReserva> listado = this.listado();
        for (RegistroReserva rc : listado) {
            if (rc.getFecha().equals(fecha) && rc.getTurno().equals(turno)) {
                return rc;
            }
        }
        return null;
    }

    public List<RegistroReserva> actualizarHabitacionesDispo(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        String fecha = fechaHoraActual.format(formatter);
        BusinessHabitacion habitacionInformation = new BusinessHabitacion();
        
        for(RegistroReserva rrsv : this.listado()){
            if(rrsv.getFecha().equals(fecha)){
                for(Reserva rsv:rrsv.getListReservas()){
                    LocalTime horaActual = LocalTime.now();
                    LocalTime horaSalida = LocalTime.parse(rsv.getHora_salida(), DateTimeFormatter.ofPattern("HH:mm:ss"));
                    if(horaSalida.isBefore(horaActual)){
                        rsv.setEstadoReserva(EstadoReserva.CONFIRMADO);
                        rsv.getHabitacion().setDisponible(true);
                        habitacionInformation.actualizar(rsv.getHabitacion());
                    }
                }
            }
        }
        return null;
    }
    
    public List<Reserva> listadoFecha(String fechaSeleccionada) {
        List<Reserva> reservasFiltradas = new ArrayList<>();
        for (RegistroReserva rrsv : this.listado()) {
            if(rrsv.getFecha().equals(fechaSeleccionada)){
                if(!rrsv.getListReservas().isEmpty()){
                    for(Reserva rv : rrsv.getListReservas()){
                        reservasFiltradas.add(rv);
                    }
                }
            }
        }
        
        return reservasFiltradas;
    }
}
