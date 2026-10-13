package web;

import dominio.BusinessProducto;
import dominio.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SvDeleteProducto")
public class SvDeleteProducto extends HttpServlet{
    
    private BusinessProducto productoInformation = new BusinessProducto();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "eliminar":
                    this.eliminarProducto(request, response);
                    System.out.println("Eliminando");
                    break;
                default:
                    
            }
        } else {
            //this.accionDefault(request, response);
            System.out.println("No se pudo acceder a la opcion de eliminar");
        }
    }
    
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Recuperamos datos del formulario
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        Producto producto = productoInformation.buscar(idProducto);
        
        if (producto != null) {
            boolean usado = new datos.LineaCompraDaoAT().listado().stream()
                    .anyMatch(linea -> linea.getProducto().getId() == idProducto)
                    || new datos.LineaVentaDaoAT().listado().stream()
                    .anyMatch(linea -> linea.getProducto().getId() == idProducto);
            if (usado) {
                response.sendRedirect(request.getContextPath() + "/SvProducto?error=referenciado");
                return;
            }
            productoInformation.eliminar(producto);
            response.sendRedirect(request.getContextPath() + "/SvProducto?ok=eliminado");
        } else {
            System.out.println("Algo salio mal al eliminar");
        }
    }
    
}
