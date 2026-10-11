package dominio;

public class BoletaVenta extends Boleta{
    
    @Override
    public void imprimirBoleta() {
        super.imprimirBoleta();
        // Información específica de la venta
        
        System.out.println("ID de Venta: " + this.venta.getId());
        System.out.println("ID de Registro de Venta: " + this.venta.getId_registroVenta());
        System.out.println("Cliente: " + this.venta.getCliente().getNombre() + " " + this.venta.getCliente().getApellido());
        System.out.println("===============================================");

        // Detalles de la venta
        System.out.println("Detalle de la venta:");
        for (LineaVenta lv : this.venta.getLineaVentas()) {
            System.out.println("Producto: " + lv.getProducto().getNombre());
            System.out.println("Cantidad: " + lv.getCantidad());
            System.out.println("Precio Unitario: " + lv.getPrecioUnitario());
            System.out.println("Subtotal: " + lv.subtotal());
            System.out.println("-----------------------------------------------");
        }

        // Total de la venta
        System.out.println("Total: " + this.venta.total());
    }


}
