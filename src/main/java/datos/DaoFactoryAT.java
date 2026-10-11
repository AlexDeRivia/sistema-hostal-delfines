package datos;

import dominio.Administrador;
import dominio.Cliente;
import dominio.Compra;
import dominio.Empleado;
import dominio.Factura;
import dominio.Habitacion;
import dominio.LineaVenta;
import dominio.Producto;
import dominio.Proveedor;
import dominio.RegistroCompra;
import dominio.RegistroReserva;
import dominio.RegistroVenta;
import dominio.Reserva;
import dominio.Venta;

public class DaoFactoryAT extends AbstractDaoFactory{
    
    @Override
    public Dao<Reserva> getReservaDAO() {
        return new ReservaDaoAT();
    }

    @Override
    public Dao<Empleado> getEmpleadoDAO() {
        return new EmpleadoDaoAT();
    }

    @Override
    public Dao<Producto> getProductoDAO() {
        return new ProductoDaoAT();
    }

    @Override
    public Dao<LineaVenta> getLineaVentaDAO() {
        return new LineaVentaDaoAT();
    }

    @Override
    public Dao<Venta> getVentaDAO() {
        return new VentaDaoAT();
    }

    @Override
    public Dao<RegistroReserva> getRegistroReservaDAO() {
        return new RegistroReservaDaoAT();
    }

    @Override
    public Dao<RegistroVenta> getRegistroVentaDAO() {
        return new RegistroVentaDaoAT();
    }
    
    @Override
    public Dao<RegistroCompra> getRegistroCompraDAO() {
        return new RegistroCompraDaoAT();
    }
    
    @Override
    public Dao<Habitacion> getHabitacionDAO(){
        return new HabitacionDaoAT();
    }
    @Override
    public Dao<Factura> getFacturaDAO(){
        return new FacturaDaoAT();
    }
    @Override
    public Dao<Cliente> getClienteDAO(){
        return new ClienteDaoAT();
    }

    @Override
    public Dao<Compra> getCompraDAO() {
        return new CompraDaoAT();
    }

    @Override
    public Dao<Proveedor> getProveedorDAO() {
        return new ProveedorDaoAT();
    }

    @Override
    public Dao<Administrador> getAdminDAO() {
        return new AdminDaoAT();
    }
}
