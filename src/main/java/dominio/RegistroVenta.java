package dominio;

import java.util.ArrayList;

public class RegistroVenta {
    private int id;
    private String fecha;
    private double liquidacion;
    private Turno turno;
    private Empleado empleado;
    private ArrayList<Venta> listVentas;

    public RegistroVenta() {
        listVentas = new ArrayList<>();
    }

    public void agregarListaVentas(Venta v){
        listVentas.add(v);
    }
    
    public void removerListaVentas(Venta v){
        listVentas.remove(v);
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

    public ArrayList<Venta> getListVentas() {
        return listVentas;
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

    public void setListVentas(ArrayList<Venta> listVentas) {
        this.listVentas = listVentas;
    }
}
