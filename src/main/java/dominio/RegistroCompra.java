package dominio;

import java.util.ArrayList;

public class RegistroCompra {
    private int id;
    private String fecha;
    private Turno turno;
    private Empleado empleado;
    private ArrayList<Compra> listCompras;

    public RegistroCompra() {
        listCompras = new ArrayList<>();
    }
    
    public void agregarListaCompras(Compra c){
        listCompras.add(c);
    }
    
    public void removerListaCompras(Compra c){
        listCompras.remove(c);
    }
    
    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public Turno getTurno() {
        return turno;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public ArrayList<Compra> getListCompras() {
        return listCompras;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setListCompras(ArrayList<Compra> listCompras) {
        this.listCompras = listCompras;
    }
}
