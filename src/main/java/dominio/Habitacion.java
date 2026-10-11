package dominio;

public class Habitacion {
    private int id;
    private int numero;
    private int numeroCamas;
    private boolean ventanaAfuera;
    private boolean disponible;

    public Habitacion() {
    }

    public Habitacion(int numero, int numeroCamas, boolean ventanaAfuera, boolean disponible) {
        this.numero = numero;
        this.numeroCamas = numeroCamas;
        this.ventanaAfuera = ventanaAfuera;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public int getNumeroCamas() {
        return numeroCamas;
    }

    public boolean isVentanaAfuera() {
        return ventanaAfuera;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setNumeroCamas(int numeroCamas) {
        this.numeroCamas = numeroCamas;
    }

    public void setVentanaAfuera(boolean ventanaAfuera) {
        this.ventanaAfuera = ventanaAfuera;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
}
