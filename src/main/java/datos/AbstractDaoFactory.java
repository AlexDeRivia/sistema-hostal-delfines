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

public abstract class AbstractDaoFactory {
    
    public abstract Dao<Reserva> getReservaDAO();
    public abstract Dao<Empleado> getEmpleadoDAO();
    public abstract Dao<Administrador> getAdminDAO();
    public abstract Dao<Producto> getProductoDAO();
    public abstract Dao<LineaVenta> getLineaVentaDAO();
    public abstract Dao<Venta> getVentaDAO();
    public abstract Dao<RegistroReserva> getRegistroReservaDAO();
    public abstract Dao<RegistroVenta> getRegistroVentaDAO();
    public abstract Dao<RegistroCompra> getRegistroCompraDAO();
    public abstract Dao<Habitacion> getHabitacionDAO();
    public abstract Dao<Factura> getFacturaDAO();
    public abstract Dao<Cliente> getClienteDAO();
    public abstract Dao<Compra> getCompraDAO();
    public abstract Dao<Proveedor> getProveedorDAO();
    public static AbstractDaoFactory getFactory(FactoryType type){
 
        if(type.equals(type.ARCHIVO_TEXTO_DAO_FACTORY)){
            return new DaoFactoryAT();
        }
        if(type.equals(type.MySQL_DAO_FACTORY)){
           return new DaoFactoryMySQL();
        }
        return null;
    }
}
