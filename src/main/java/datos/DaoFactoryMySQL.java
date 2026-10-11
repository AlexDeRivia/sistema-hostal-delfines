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
import dominio.Usuario;
import dominio.Venta;

public class DaoFactoryMySQL extends AbstractDaoFactory{

    @Override
    public Dao<Reserva> getReservaDAO() {
        return new ReservaDaoMySQL();
    }

    @Override
    public Dao<Empleado> getEmpleadoDAO() {
        return new EmpleadoDaoMySQL();
    }

    @Override
    public Dao<Producto> getProductoDAO() {
        return new ProductoDaoMySQL();
    }

    @Override
    public Dao<LineaVenta> getLineaVentaDAO() {
        return new LineaVentaDaoMySQL();
    }

    @Override
    public Dao<Venta> getVentaDAO() {
        return new VentaDaoMySQL();
    }

    @Override
    public Dao<RegistroReserva> getRegistroReservaDAO() {
        return new RegistroReservaDaoMySQL();
    }

    @Override
    public Dao<RegistroVenta> getRegistroVentaDAO() {
        return new RegistroVentaDaoMySQL();
    }
    
    @Override
    public Dao<RegistroCompra> getRegistroCompraDAO() {
        return new RegistroCompraDaoMySQL();
    }
    
    @Override
    public Dao<Habitacion> getHabitacionDAO(){
        return new HabitacionDaoMySQL();
    }
    @Override
    public Dao<Factura> getFacturaDAO(){
        return new FacturaDaoMySQL();
    }
    @Override
    public Dao<Cliente> getClienteDAO(){
        return new ClienteDaoMySQL();
    }
    
    @Override
    public Dao<Compra> getCompraDAO() {
        return new CompraDaoMySQL();
    }

    @Override
    public Dao<Proveedor> getProveedorDAO() {
        return new ProveedorDaoMySQL();
    }

    @Override
    public Dao<Administrador> getAdminDAO() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
