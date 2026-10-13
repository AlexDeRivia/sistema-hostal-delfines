package web;

import dominio.BusinessCliente;
import dominio.BusinessHabitacion;
import dominio.BusinessProducto;
import dominio.BusinessProveedor;
import dominio.Categoria;
import dominio.Cliente;
import dominio.EstadoReserva;
import dominio.Habitacion;
import dominio.Producto;
import dominio.Proveedor;
import dominio.RegistroReserva;
import dominio.Reserva;
import dominio.ReservaService;
import dominio.Turno;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.time.LocalDate;

@WebListener
public class DemoDataListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (!"true".equalsIgnoreCase(System.getenv("HOSTAL_DEMO_SEED"))) {
            return;
        }
        BusinessHabitacion habitaciones = new BusinessHabitacion();
        BusinessCliente clientes = new BusinessCliente();
        BusinessProducto productos = new BusinessProducto();
        BusinessProveedor proveedores = new BusinessProveedor();
        if (!habitaciones.listado().isEmpty() || !clientes.listado().isEmpty()
                || !productos.listado().isEmpty() || !proveedores.listado().isEmpty()) {
            return;
        }
        habitaciones.crear(new Habitacion(101, 1, true, true));
        habitaciones.crear(new Habitacion(102, 2, false, true));
        habitaciones.crear(new Habitacion(103, 1, false, true));
        clientes.crear(new Cliente("00000001", "Cliente", "Ejemplo"));
        clientes.crear(new Cliente("00000002", "Huésped", "Demo"));
        productos.crear(new Producto("Agua embotellada", 12, 3.0, 1.5, Categoria.ALIMENTOS));
        productos.crear(new Producto("Cepillo dental", 8, 5.0, 2.5, Categoria.USO_PERSONAL));
        Proveedor proveedor = new Proveedor();
        proveedor.setRazonSocial("Proveedor de ejemplo");
        proveedor.setRuc("20000000001");
        proveedores.crear(proveedor);

        RegistroReserva registro = new RegistroReserva();
        registro.setFecha(LocalDate.now().toString());
        registro.setTurno(Turno.MANANA);
        registro.setEmpleado(OperadorDemo.administrador());
        Reserva reserva = new Reserva();
        reserva.setCliente(clientes.listado().get(0));
        reserva.setHabitacion(habitaciones.getHabitacionByNumero(101));
        reserva.setFechaIngreso(LocalDate.now().plusDays(1).toString());
        reserva.setHora_ingreso("14:00:00");
        reserva.setHoras_reservadas("3");
        reserva.setNumeroHuespedes(1);
        reserva.setCostoEfectivo(25);
        reserva.setEstadoReserva(EstadoReserva.PENDIENTE);
        ReservaService.crear(reserva, registro);
    }
}
