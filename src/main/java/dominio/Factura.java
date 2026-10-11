package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Factura {
    protected int id;
    protected String fecha;
    protected String ruc;
    protected Venta venta;
    public Factura() {
        Random r = new Random();
        this.setId(r.nextInt(1000));
        this.setRuc("20136957253");
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        fecha = fechaHoraActual.format(formatter);
        return fecha;
    }

    public String getRuc() {
        return ruc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
    
    public Venta getVenta() {
        return venta;
    }

    
    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    
    public void imprimirFactura() {
        // Encabezado de la boleta
        System.out.println("==================== FACTURA ====================");
        System.out.println("Fecha: " + getFecha());
        System.out.println("Factura ID: " + this.getId());
        System.out.println("RUC: " + this.getRuc());
        System.out.println("===============================================");
    }
}
