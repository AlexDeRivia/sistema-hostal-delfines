package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Boleta {
    protected int id;
    protected String fecha;
    protected Venta venta;
    public Boleta() {
        Random r = new Random();
        this.setId(r.nextInt(1000));
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

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public void imprimirBoleta() {
        // Encabezado de la boleta
        System.out.println("==================== BOLETA ====================");
        System.out.println("Fecha: " + getFecha());
        System.out.println("Boleta ID: " + this.getId());
        System.out.println("===============================================");
    }
    public Venta getVenta() {
        return venta;
    }

    
    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}
