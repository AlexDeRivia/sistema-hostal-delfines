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
        String nombreProducto = request.getParameter("producto_name");
        double precioV = Double.parseDouble(request.getParameter("pventa"));
        double precioC = Double.parseDouble(request.getParameter("pcompra"));
        String ctg = String.valueOf(request.getParameter("categoriaStatus"));
        Producto p = new Producto();
        p.setId(id);
        p.setNombre(nombreProducto);
        p.setPrecioVenta(precioV);
        p.setPrecioCompra(precioC);
        Categoria categoria;
        if(ctg.equals("ALIMENTOS")){
            categoria = Categoria.ALIMENTOS;
        }else{
            categoria = Categoria.USO_PERSONAL;
        }
        p.setCategoria(categoria);
        
        productoInformation.actualizar(p);
        System.out.println("modificando");

        this.accionDefault(request, response);
    }

}
