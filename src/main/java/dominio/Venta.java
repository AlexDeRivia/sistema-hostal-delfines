package dominio;

import java.util.ArrayList;

public class Venta {
    private int id;
    private int id_registroVenta;
    private Cliente cliente;
    private ArrayList<LineaVenta> lineaVentas;

    public Venta() {
        lineaVentas = new ArrayList<>();
    }
    
    public double total(){
        double total_final = 0;
        for(LineaVenta lv: this.getLineaVentas()){
            total_final = total_final + lv.subtotal();
        }
        return total_final;
    }

    public void agregarLineaVenta(LineaVenta lv){
        lineaVentas.add(lv);
    }
    
    public void removerLineaVenta(LineaVenta lv){
        lineaVentas.remove(lv);
    }
    
    public int getId() {
        return id;
    }

    public int getId_registroVenta() {
        return id_registroVenta;
    }
    
    public Cliente getCliente() {
        return cliente;
    }

    public ArrayList<LineaVenta> getLineaVentas() {
        return lineaVentas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_registroVenta(int id_registroVenta) {
        this.id_registroVenta = id_registroVenta;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setLineaVentas(ArrayList<LineaVenta> lineaVentas) {
        this.lineaVentas = lineaVentas;
    }
}
