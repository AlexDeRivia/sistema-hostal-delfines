package dominio;

public class LineaVenta {
    private int id;
    private int venta_id;
    private int cantidad;
    private Producto producto;
    private double precioUnitario;

    public LineaVenta() {
    }

    public LineaVenta(int cantidad, Producto producto, double precioUnitario) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.precioUnitario = precioUnitario;
    }
    
    public double subtotal(){
        return precioUnitario * cantidad;
    }

    public int getId() {
        return id;
    }

    public int getVenta_id() {
        return venta_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setVenta_id(int venta_id) {
        this.venta_id = venta_id;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
