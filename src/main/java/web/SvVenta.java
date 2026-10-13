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

    public SvVenta() {
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
        RegistroVenta rventa = this.iniciarModuloVenta(false, request);
        Venta venta = getVenta(request);
        List<Producto> productos = productoInformation.listado();
        List<Cliente> clientes = clienteInformation.listado();
        request.setAttribute("productos", productos);
        request.setAttribute("clientes", clientes);
        request.setAttribute("rventa", rventa);
        request.setAttribute("totalVenta", venta.total());

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
        try {
            if (accion != null) {
                switch (accion) {
                case "agregar":
                    this.agregarProducto(request, response);
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
                    response.sendRedirect(request.getContextPath() + "/SvVenta?ok=guardada");
                    return;
                default:
                }
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
            request.getSession().setAttribute("errorVenta", ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/SvVenta");
    }

    private RegistroVenta iniciarModuloVenta(boolean guardar, HttpServletRequest request) {
        //CREACION DEL REGISTRO DE COMPRA DIARIO
        RegistroVenta rvTemp = null;
        Turno turno = Turno.valueOf(this.obtenerTurno());
        Empleado operador = OperadorActual.desde(request);
        rvTemp = rventaInformation.registroVentaDiario(turno, operador.getId());
        if (rvTemp != null) {
            return rvTemp;
        } else {
            rvTemp = new RegistroVenta();
            rvTemp.setFecha(this.obtenerFecha());
            rvTemp.setTurno(turno);
            rvTemp.setEmpleado(operador);
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
        Cliente c = clienteInformation.buscar(Integer.parseInt(request.getParameter("clientId")));
        Producto p = productoInformation.buscar(Integer.parseInt(request.getParameter("productId")));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        if (c == null || p == null || cantidad <= 0) {
            throw new IllegalArgumentException("Selecciona cliente, producto y cantidad válida.");
        }
        if (venta.getCliente() != null && venta.getCliente().getId() != c.getId()) {
            throw new IllegalArgumentException("Una venta solo puede tener un cliente.");
        }
        long enCarrito = venta.getLineaVentas().stream()
                .filter(linea -> linea.getProducto().getId() == p.getId())
                .mapToLong(LineaVenta::getCantidad).sum();
        if ((long) cantidad + enCarrito > p.getStock()) {
            throw new IllegalArgumentException("No hay stock suficiente para esta venta.");
        }
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
        if (nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        LineaVenta linea = venta.getLineaVentas().get(lineaVenta_id);
        int anterior = linea.getCantidad();
        linea.setCantidad(nuevaCantidad);
        try {
            validarCarrito(venta);
        } catch (IllegalArgumentException ex) {
            linea.setCantidad(anterior);
            throw ex;
        }

        HttpSession session = request.getSession();
        session.setAttribute("total", venta.total());
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Venta venta = getVenta(request);
        synchronized (Producto.class) {
            validarCarrito(venta);
            RegistroVenta rventa = iniciarModuloVenta(true, request);
            venta.setId_registroVenta(rventa.getId());
            ventaInformation.crear(venta);
            rventa.setLiquidacion(rventa.getLiquidacion() + venta.total());
            rventaInformation.actualizar(rventa);
        }
    }

    private void validarCarrito(Venta venta) {
        if (venta.getCliente() == null || venta.getLineaVentas().isEmpty()) {
            throw new IllegalArgumentException("Agrega al menos un producto a la venta.");
        }
        java.util.Map<Integer, Long> cantidades = new java.util.HashMap<>();
        for (LineaVenta linea : venta.getLineaVentas()) {
            if (linea.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
            }
            int id = linea.getProducto().getId();
            cantidades.merge(id, (long) linea.getCantidad(), Long::sum);
        }
        for (java.util.Map.Entry<Integer, Long> item : cantidades.entrySet()) {
            Producto producto = productoInformation.buscar(item.getKey());
            if (producto == null || item.getValue() > producto.getStock()) {
                throw new IllegalArgumentException("No hay stock suficiente para esta venta.");
            }
        }
    }
}
