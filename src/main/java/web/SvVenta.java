package web;

import dominio.BusinessCliente;
import dominio.BusinessProducto;
import dominio.BusinessRegistroVenta;
import dominio.BusinessVenta;
import dominio.Cliente;
import dominio.Empleado;
import dominio.LineaVenta;
import dominio.Producto;
import dominio.RegistroVenta;
import dominio.Turno;
import dominio.Venta;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "SvVenta", urlPatterns = {"/SvVenta"})
public class SvVenta extends HttpServlet {

    private BusinessProducto productoInformation;
    private BusinessCliente clienteInformation;
    private BusinessRegistroVenta rventaInformation;
    private BusinessVenta ventaInformation;
    private Empleado empleadoSession;

    public SvVenta() {
        this.empleadoSession = new Empleado();
        empleadoSession.setId(1);
        empleadoSession.setDni("75465361");
        empleadoSession.setNombre("Nilton");
        empleadoSession.setUsername("admin");
        productoInformation = new BusinessProducto();
        clienteInformation = new BusinessCliente();
        rventaInformation = new BusinessRegistroVenta();
        ventaInformation = new BusinessVenta();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("eliminar".equals(accion)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        RegistroVenta rventa = this.iniciarModuloVenta(false);
        getVenta(request);
        List<Producto> productos = productoInformation.listado();
        List<Cliente> clientes = clienteInformation.listado();
        request.setAttribute("productos", productos);
        request.setAttribute("clientes", clientes);
        request.setAttribute("rventa", rventa);

        System.out.println("accion");
        if (accion != null) {
            switch (accion) {
                case "actualizar":
                    this.actualizarVenta(request, response);
                    return;
                default:
            }
            response.sendRedirect(request.getContextPath() + "/SvVenta");
        } else {
            request.getRequestDispatcher("VistaVenta.jsp").forward(request, response);
        }

    }

    public String obtenerTurno() {
        LocalTime hora = LocalTime.now();
        if (hora.isAfter(LocalTime.of(6, 0)) && hora.isBefore(LocalTime.of(12, 1))) {
            return Turno.MANANA.name();
        } else if (hora.isAfter(LocalTime.of(12, 0)) && hora.isBefore(LocalTime.of(18, 1))) {
            return Turno.TARDE.name();
        } else {
            return Turno.NOCHE.name();
        }
    }

    public String obtenerFecha() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        return fechaHoraActual.format(formatter);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "agregar":
                    this.agregarProducto(request, response);
                    System.out.println("Agregando");
                    break;
                case "update":
                    this.update(request, response);
                    break;
                case "eliminar":
                    this.eliminarProducto(request, response);
                    break;
                case "guardar":
                    this.guardar(request, response);
                    HttpSession session = request.getSession();
                    session.removeAttribute("venta");
                    session.removeAttribute("total");
                    break;
                default:
            }
        }
        response.sendRedirect(request.getContextPath() + "/SvVenta");
    }

    private RegistroVenta iniciarModuloVenta(boolean guardar) {
        //CREACION DEL REGISTRO DE COMPRA DIARIO
        RegistroVenta rvTemp = null;
        Turno turno = Turno.valueOf(this.obtenerTurno());
        rvTemp = rventaInformation.registroVentaDiario(turno);
        if (rvTemp != null) {
            return rvTemp;
        } else {
            rvTemp = new RegistroVenta();
            rvTemp.setFecha(this.obtenerFecha());
            rvTemp.setTurno(turno);
            rvTemp.setEmpleado(empleadoSession);
            return guardar ? rventaInformation.crear(rvTemp) : rvTemp;
        }
    }

    private Venta getVenta(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Venta venta = (Venta) session.getAttribute("venta");
        if (venta == null) {
            venta = new Venta();
            session.setAttribute("venta", venta);
        }
        return venta;
    }

    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Venta venta = getVenta(request);
        //Recuperamos datos del formulario
        String cliente = String.valueOf(request.getParameter("cliente_name"));
        Cliente c = clienteInformation.getClienteByNombre(cliente);
        String producto = String.valueOf(request.getParameter("producto_name"));
        Producto p = productoInformation.getProductoByNombre(producto);
        String cant = request.getParameter("cantidad");
        int cantidad = 0;
        if (!cant.equals(" ")) {
            cantidad = Integer.parseInt(cant);
        }

        venta.setId(1);
        venta.setCliente(c);
        venta.agregarLineaVenta(new LineaVenta(cantidad, p, p.getPrecioVenta()));

        HttpSession session = request.getSession();
        session.setAttribute("venta", venta);
        session.setAttribute("total", venta.total());
    }

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Venta venta = getVenta(request);
        int idLineaVenta = Integer.parseInt(request.getParameter("idLineaVenta"));
        venta.removerLineaVenta(venta.getLineaVentas().get(idLineaVenta));
        HttpSession session = request.getSession();
        session.setAttribute("venta", venta);
        session.setAttribute("total", venta.total());
    }

    private void actualizarVenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Venta venta = getVenta(request);
        int lineaVenta_id = Integer.parseInt(request.getParameter("idLineaVenta"));

        request.setAttribute("posicion", lineaVenta_id);
        request.setAttribute("lineaVenta", venta.getLineaVentas().get(lineaVenta_id));

        String jspUpdate = "UpdateLineaVenta.jsp";
        request.getRequestDispatcher(jspUpdate).forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Venta venta = getVenta(request);
        int lineaVenta_id = Integer.parseInt(request.getParameter("posicion"));
        int nuevaCantidad = Integer.parseInt(request.getParameter("cantidad"));
        venta.getLineaVentas().get(lineaVenta_id).setCantidad(nuevaCantidad);

        HttpSession session = request.getSession();
        session.setAttribute("total", venta.total());
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Venta venta = getVenta(request);
        if (!venta.getLineaVentas().isEmpty()) {
            RegistroVenta rventa = iniciarModuloVenta(true);
            rventa.setLiquidacion(rventa.getLiquidacion() + venta.total());
            rventaInformation.actualizar(rventa);

            venta.setId_registroVenta(rventa.getId());
            ventaInformation.crear(venta);
        }
    }
}
