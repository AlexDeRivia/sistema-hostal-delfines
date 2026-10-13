package web;

import dominio.BusinessCompra;
import dominio.BusinessProducto;
import dominio.BusinessProveedor;
import dominio.BusinessRegistroCompra;
import dominio.Compra;
import dominio.Empleado;
import dominio.LineaCompra;
import dominio.Producto;
import dominio.Proveedor;
import dominio.RegistroCompra;
import dominio.Turno;
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



@WebServlet(name = "SvCompra", urlPatterns = {"/SvCompra"})
public class SvCompra extends HttpServlet {

    private BusinessProveedor proveedorInformation;
    private BusinessProducto productoInformation;
    private BusinessCompra compraInformation;
    private BusinessRegistroCompra rcompraInformation;

    public SvCompra() {
        proveedorInformation = new BusinessProveedor();
        productoInformation = new BusinessProducto();
        compraInformation = new BusinessCompra();
        rcompraInformation = new BusinessRegistroCompra();
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
        RegistroCompra rcompra = this.iniciarModuloCompra(false, request);
        Compra compra = getCompra(request);
        List<Producto> productos = new BusinessProducto().listado();
        List<Proveedor> proveedores = proveedorInformation.listado();
        request.setAttribute("productos", productos);
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("rcompra", rcompra);
        request.setAttribute("totalCompra", compra.total());
        
        if (accion != null) {
            switch (accion) {
                case "actualizar":
                    this.actualizarCompra(request, response);
                    return;
                default:
            }
            response.sendRedirect(request.getContextPath() + "/SvCompra");
        }else{
            request.getRequestDispatcher("VistaCompra.jsp").forward(request, response);
        }
        
    }
    
    public String obtenerTurno(){
        LocalTime hora = LocalTime.now();
        if (hora.isAfter(LocalTime.of(6, 0)) && hora.isBefore(LocalTime.of(12, 1))) {
            return Turno.MANANA.name();
        } else if (hora.isAfter(LocalTime.of(12, 0)) && hora.isBefore(LocalTime.of(18, 1))) {
            return Turno.TARDE.name();
        } else {
            return Turno.NOCHE.name();
        }
    }
    
    public String obtenerFecha(){
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
                    session.removeAttribute("compra");
                    response.sendRedirect(request.getContextPath() + "/SvCompra?ok=guardada");
                    return;
                default:
                    
                }
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
            request.getSession().setAttribute("errorCompra", ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/SvCompra");
    }
    
    private RegistroCompra iniciarModuloCompra(boolean guardar, HttpServletRequest request){
        //CREACION DEL REGISTRO DE COMPRA DIARIO
        RegistroCompra rcTemp = null;
        Turno turno = Turno.valueOf(this.obtenerTurno());
        Empleado operador = OperadorActual.desde(request);
        rcTemp = rcompraInformation.registroCompraDiario(turno, operador.getId());
        if(rcTemp != null){
            return rcTemp;
        }else{
            rcTemp = new RegistroCompra();
            rcTemp.setFecha(this.obtenerFecha());
            rcTemp.setTurno(turno);
            rcTemp.setEmpleado(operador);
            return guardar ? rcompraInformation.crear(rcTemp) : rcTemp;
        }
    }

    private Compra getCompra(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Compra compra = (Compra) session.getAttribute("compra");
        if (compra == null) {
            compra = new Compra();
            session.setAttribute("compra", compra);
        }
        return compra;
    }
    
    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Compra compra = getCompra(request);
        //Recuperamos datos del formulario
        Proveedor pv = new datos.ProveedorDaoAT().buscar(Integer.parseInt(request.getParameter("proveedorId")));
        Producto p = productoInformation.buscar(Integer.parseInt(request.getParameter("productoId")));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        if (pv == null || p == null || cantidad <= 0) {
            throw new IllegalArgumentException("Selecciona proveedor, producto y cantidad válida.");
        }
        if (compra.getProveedor() != null && compra.getProveedor().getId() != pv.getId()) {
            throw new IllegalArgumentException("Una compra solo puede tener un proveedor.");
        }
        compra.setProveedor(pv);
        compra.agregarLineaCompra(new LineaCompra(cantidad, p, p.getPrecioCompra()));
        
        HttpSession session = request.getSession();
        session.setAttribute("compra", compra);
    }
    
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Compra compra = getCompra(request);
        int idLineaCompra = Integer.parseInt(request.getParameter("idLineaCompra"));
        compra.removerLineaCompra(compra.getLineaCompras().get(idLineaCompra));
        HttpSession session = request.getSession();
        session.setAttribute("compra", compra);
    }
    
    private void actualizarCompra(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Compra compra = getCompra(request);
        int lineaCompra_id = Integer.parseInt(request.getParameter("idLineaCompra"));
        
        request.setAttribute("posicion", lineaCompra_id);
        request.setAttribute("lineaCompra", compra.getLineaCompras().get(lineaCompra_id));
        
        String jspUpdate = "UpdateLineaCompra.jsp";
        request.getRequestDispatcher(jspUpdate).forward(request, response);
    }
    
    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Compra compra = getCompra(request);
        int lineaCompra_id = Integer.parseInt(request.getParameter("posicion"));
        int nuevaCantidad = Integer.parseInt(request.getParameter("cantidad"));
        if (nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        compra.getLineaCompras().get(lineaCompra_id).setCantidad(nuevaCantidad);
    }
    
    private void guardar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Compra compra = getCompra(request);
        if (compra.getLineaCompras().isEmpty() || compra.getProveedor() == null) {
            throw new IllegalArgumentException("Agrega al menos un producto a la compra.");
        }
        synchronized (Producto.class) {
            java.util.Map<Integer, Long> cantidades = new java.util.HashMap<>();
            for (LineaCompra linea : compra.getLineaCompras()) {
                if (linea.getCantidad() <= 0 || linea.getProducto() == null) {
                    throw new IllegalArgumentException("La compra contiene una cantidad o producto inválido.");
                }
                cantidades.merge(linea.getProducto().getId(), (long) linea.getCantidad(), Long::sum);
            }
            for (java.util.Map.Entry<Integer, Long> item : cantidades.entrySet()) {
                Producto actual = productoInformation.buscar(item.getKey());
                if (actual == null || (long) actual.getStock() + item.getValue() > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("La compra contiene una cantidad o producto inválido.");
                }
            }
            RegistroCompra rcompra = iniciarModuloCompra(true, request);
            compra.setId_registroCompra(rcompra.getId());
            compraInformation.crear(compra);
        }
    }
}
