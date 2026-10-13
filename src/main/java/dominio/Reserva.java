package dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Reserva {
    private int id;
    
    private int numeroHuespedes;
    private double costoEfectivo;
    private double costoYape;
    private String horas_reservadas;
    private String hora_ingreso;
    private String hora_salida;
    private String fechaIngreso;
    private EstadoReserva estadoReserva;
    private Cliente cliente;
    private Habitacion habitacion;
    private int registroreserva_id;
    
    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public int getId() {
        return id;
    }

    public int getNumeroHuespedes() {
        return numeroHuespedes;
    }

    public double getCostoEfectivo() {
        return costoEfectivo;
    }

    public double getCostoYape() {
        return costoYape;
    }

    public String getHoras_reservadas() {
        return horas_reservadas;
    }
    
    public String getHora_ingreso() {
        return hora_ingreso;
    }

    public String getHora_salida() {
        if (horas_reservadas == null) {
            return "Error: horas_reservadas no está inicializado";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime horaIngreso = LocalTime.parse(this.getHora_ingreso(), formatter);
        LocalTime horaSalida = horaIngreso.plusHours(Long.parseLong(horas_reservadas));
        hora_salida = horaSalida.format(formatter);
        return hora_salida;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaSalida() {
        if (fechaIngreso == null || fechaIngreso.isBlank()) {
            return "";
        }
        return LocalDate.parse(fechaIngreso)
                .atTime(LocalTime.parse(hora_ingreso, DateTimeFormatter.ofPattern("HH:mm:ss")))
                .plusHours(Long.parseLong(horas_reservadas)).toLocalDate().toString();
    }

    public LocalDateTime getInicio() {
        return LocalDate.parse(fechaIngreso)
                .atTime(LocalTime.parse(hora_ingreso, DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    public LocalDateTime getFin() {
        return getInicio().plusHours(Long.parseLong(horas_reservadas));
    }

    public int getRegistroreserva_id() {
        return registroreserva_id;
    }
    
    public void setEstadoReserva(EstadoReserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumeroHuespedes(int numeroHuespedes) {
        this.numeroHuespedes = numeroHuespedes;
    }

    public void setCostoEfectivo(double costoEfectivo) {
        this.costoEfectivo = costoEfectivo;
    }

    public void setCostoYape(double costoYape) {
        this.costoYape = costoYape;
    }

    public void setHoras_reservadas(String horas_reservadas) {
        this.horas_reservadas = horas_reservadas;
    }
    
    public void setHora_ingreso(String hora_ingreso) {
        this.hora_ingreso = hora_ingreso;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public void setRegistroreserva_id(int registroreserva_id) {
        this.registroreserva_id = registroreserva_id;
    }
    
}
