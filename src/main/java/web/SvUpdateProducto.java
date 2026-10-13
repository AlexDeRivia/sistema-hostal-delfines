package web;

import dominio.BusinessCliente;
import dominio.BusinessHabitacion;
import dominio.BusinessProducto;
import dominio.Categoria;
import dominio.Cliente;
import dominio.EstadoReserva;
import dominio.Habitacion;
import dominio.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/SvUpdateProducto")
public class SvUpdateProducto extends HttpServlet{
    
    private BusinessProducto productoInformation = new BusinessProducto();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "actualizar":
                    this.actualizarProducto(request, response);
                    System.out.println("Actualizando");
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }
    
    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Categoria> categorias = Arrays.asList(Categoria.values());
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("index.jsp").forward(request, response);
        System.out.println("comenzando");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "update":
                    this.modificarProducto(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int producto_id = Integer.parseInt(request.getParameter("idProducto"));
        Producto producto = productoInformation.buscar(producto_id);
        
        request.setAttribute("producto", producto);
        
        List<Categoria> categorias = Arrays.asList(Categoria.values());
        request.setAttribute("categorias", categorias);
        String jspUpdate = "UpdateProducto.jsp";
        request.getRequestDispatcher(jspUpdate).forward(request, response);
    }
    
    private void modificarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Recuperamos datos del formulario
        Integer id = Integer.valueOf(request.getParameter("producto_id"));
        Producto p = productoInformation.buscar(id);
        if (p == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String nombreProducto;
        double precioV;
        double precioC;
        try {
            nombreProducto = InputRules.text(request.getParameter("producto_name"), "El producto");
            precioV = InputRules.positiveMoney(request.getParameter("pventa"));
            precioC = InputRules.positiveMoney(request.getParameter("pcompra"));
            if (productoInformation.listado().stream().anyMatch(other -> other.getId() != id
                    && other.getNombre().equalsIgnoreCase(nombreProducto))) {
                throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
            }
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("producto", p);
            request.setAttribute("categorias", Arrays.asList(Categoria.values()));
            request.getRequestDispatcher("UpdateProducto.jsp").forward(request, response);
            return;
        }
        Categoria categoria;
        try {
            categoria = Categoria.valueOf(request.getParameter("categoriaStatus"));
        } catch (IllegalArgumentException | NullPointerException ex) {
            request.setAttribute("error", "Selecciona una categoría válida.");
            request.setAttribute("producto", p);
            request.setAttribute("categorias", Arrays.asList(Categoria.values()));
            request.getRequestDispatcher("UpdateProducto.jsp").forward(request, response);
            return;
        }
        p.setNombre(nombreProducto);
        p.setPrecioVenta(precioV);
        p.setPrecioCompra(precioC);
        p.setCategoria(categoria);
        
        productoInformation.actualizar(p);
        System.out.println("modificando");

        response.sendRedirect(request.getContextPath() + "/SvProducto?ok=actualizado");
    }

}
