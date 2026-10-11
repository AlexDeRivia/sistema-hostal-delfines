package web;

import dominio.BusinessCliente;
import dominio.Cliente;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SvElimCliente", urlPatterns = {"/SvElimCliente"})
public class SvElimCliente extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       int id = Integer.parseInt(request.getParameter("id"));

        // Crear una instancia de Cliente con el ID proporcionado
        Cliente clienteEliminar = new Cliente();
        clienteEliminar.setId(id);

        // Instanciar BusinessCliente y llamar al método eliminar
        BusinessCliente businessCliente = new BusinessCliente();
        Cliente clienteEliminado = businessCliente.eliminar(clienteEliminar);

        if (clienteEliminado != null) {
            // Cliente eliminado exitosamente
            response.sendRedirect("agregarCliente.jsp");
        } else {
            // Ocurrió un error al eliminar el cliente
            response.sendRedirect("verCliente.jsp");
            
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
