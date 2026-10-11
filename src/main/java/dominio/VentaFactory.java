package dominio;

public class VentaFactory implements AbstractFactory{
    
    @Override
    public Boleta crearBoleta() {
        return new BoletaVenta();
    }

    @Override
    public Factura crearFactura() {
        return new FacturaVenta();
    }
}
