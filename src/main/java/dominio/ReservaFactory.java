package dominio;

public class ReservaFactory implements AbstractFactory{

    @Override
    public Boleta crearBoleta() {
        return new BoletaReserva();
    }

    @Override
    public Factura crearFactura() {
        return new FacturaReserva();
    }
}
