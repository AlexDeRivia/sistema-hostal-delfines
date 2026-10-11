package dominio;

import java.util.ArrayList;

public class Compra {
    private int id;
    private Proveedor proveedor;
    private int id_registroCompra;
    private ArrayList<LineaCompra> lineaCompras;

    public Compra() {
        lineaCompras = new ArrayList<>();
    }
    
    public double total(){
        
        return 0.0;
    }

    public void agregarLineaCompra(LineaCompra lc){
        lineaCompras.add(lc);
    }
    
    public void removerLineaCompra(LineaCompra lc){
        lineaCompras.remove(lc);
    }
    
    public int getId() {
        return id;
    }
    
    public Proveedor getProveedor() {
        return proveedor;
    }

    public int getId_registroCompra() {
        return id_registroCompra;
    }

    public ArrayList<LineaCompra> getLineaCompras() {
        return lineaCompras;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public void setId_registroCompra(int id_registroCompra) {
        this.id_registroCompra = id_registroCompra;
    }

    public void setLineaCompras(ArrayList<LineaCompra> lineaCompras) {
        this.lineaCompras = lineaCompras;
    }
    
}
