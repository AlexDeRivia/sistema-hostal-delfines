package dominio;

import java.util.ArrayList;

public class RegistroReserva {
    private int id;
    private String fecha;
    private double liquidacion;
    private Turno turno;
    private Empleado empleado;
    private ArrayList<Reserva> listReservas;

    public RegistroReserva() {
        listReservas = new ArrayList<>();
    }

    public void agregarListaReservas(Reserva r){
        listReservas.add(r);
    }
    
    public void removerListaReservas(Reserva r){
        listReservas.remove(r);
    }
    
    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public double getLiquidacion() {
        return liquidacion;
    }

    public Turno getTurno() {
        return turno;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public ArrayList<Reserva> getListReservas() {
        return listReservas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setLiquidacion(double liquidacion) {
        this.liquidacion = liquidacion;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setListReservas(ArrayList<Reserva> listReservas) {
        this.listReservas = listReservas;
    }
}
