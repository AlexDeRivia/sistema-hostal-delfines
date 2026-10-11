package dominio;

public class ComprobanteVenta {
    private Venta venta;
    private AbstractFactory comprobanteFactory;
    public ComprobanteVenta() {
        comprobanteFactory = new VentaFactory();
    }
    
    
    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    
    public void generarBoletaVenta(){
        Boleta bv = comprobanteFactory.crearBoleta();
        bv.setVenta(this.getVenta());
        bv.imprimirBoleta();
    }
    
    public void generarFacturaVenta(){
        Factura f = comprobanteFactory.crearFactura();
        f.setVenta(this.getVenta());
        f.imprimirFactura();
    }
}
