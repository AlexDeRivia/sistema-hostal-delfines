package dominio;

public class Producto {
    private int id;
    private String nombre;
    private int stock;
    private double precioVenta;
    private double precioCompra;
    private Categoria categoria;

    public Producto() {
    }

    public Producto(int id) {
        this.id = id;
    }

    public Producto(String nombre, int stock, double precioVenta, double precioCompra, Categoria categoria) {
        this.nombre = nombre;
        this.stock = stock;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
