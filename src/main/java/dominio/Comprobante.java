package dominio;

public abstract class Comprobante<T> {
    
    public abstract T imprimirBoleta(T obj);
    public abstract T imprimirFactura(T obj);
}
