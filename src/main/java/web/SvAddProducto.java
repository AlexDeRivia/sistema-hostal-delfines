package web;

import dominio.BusinessProducto;
import dominio.Categoria;
import dominio.Producto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SvAddProducto")
public class SvAddProducto extends HttpServlet {

    private BusinessProducto productoInformation = new BusinessProducto();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.accionDefault(request, response);
        //this.iniciarRegistroReserva();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        System.out.println("doPost");
        if (accion != null) {
            switch (accion) {
                case "add":
                    try {
                        this.agregarProducto(request, response);
                        response.sendRedirect(request.getContextPath() + "/SvProducto?ok=creado");
                    } catch (IllegalArgumentException ex) {
                        request.setAttribute("error", ex.getMessage());
                        this.accionDefault(request, response);
                    }
                    break;
                default:
                    this.accionDefault(request, response);
            }
        }else{
            this.accionDefault(request, response);
        }
    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGET 1");
        request.getRequestDispatcher("AddProducto.jsp").forward(request, response);
    }

    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Recuperamos datos del formulario
        String nombreProducto = InputRules.text(request.getParameter("producto"), "El producto");
        double precioV = InputRules.positiveMoney(request.getParameter("pventa"));
        double precioC = InputRules.positiveMoney(request.getParameter("pcompra"));
        Categoria categoria;
        try {
            categoria = Categoria.valueOf(request.getParameter("categoria"));
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalArgumentException("Selecciona una categoría válida.");
        }
        if (productoInformation.listado().stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(nombreProducto))) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
        }
        productoInformation.crear(new Producto(nombreProducto, 0, precioV, precioC, categoria));
    }
}
