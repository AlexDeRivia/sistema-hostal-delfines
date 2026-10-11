package dominio;

public class LineaCompra {
    private int id;
    private int compra_id;
    private int cantidad;
    private Producto producto;
    private double precioUnitario;

    public LineaCompra() {
    }

    public LineaCompra(int cantidad, Producto producto, double precioUnitario) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.precioUnitario = precioUnitario;
    }
    
    public double subtotal(){
        
        return 0.0;
    }

    public int getId() {
        return id;
    }

    public int getCompra_id() {
        return compra_id;
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

    public void setCompra_id(int compra_id) {
        this.compra_id = compra_id;
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
