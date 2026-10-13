package web;

import dominio.BusinessCliente;
import dominio.Cliente;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet(name = "SvEditCliente", urlPatterns = {"/SvEditCliente"})
public class SvEditCliente extends HttpServlet {
 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {         
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        /*Recibir el id mediante la solicitud del boton Editar*/
         int id = Integer.parseInt(request.getParameter("id"));

        BusinessCliente businessCliente = new BusinessCliente();
        Cliente clienteEditar = businessCliente.buscar(id);
        
        // Almacenar el cliente a editar en la sesión
        HttpSession session = request.getSession();
        session.setAttribute("clienteEditar", clienteEditar);
        
        // Redireccionar a la página de edición
        response.sendRedirect("editarCliente.jsp");
                
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*Recibir los datos de cliente*/
        String dniCliente;
        String nombreCliente;
        String apellidoCliente;
        try {
            dniCliente = InputRules.text(request.getParameter("dni"), "El documento");
            nombreCliente = InputRules.text(request.getParameter("nombre"), "El nombre");
            apellidoCliente = InputRules.text(request.getParameter("apellido"), "El apellido");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("editarCliente.jsp").forward(request, response);
            return;
        }
        
     
        HttpSession session = request.getSession();
        Cliente clienteEditar = (Cliente) session.getAttribute("clienteEditar");
        if (clienteEditar == null || new BusinessCliente().listado().stream()
                .anyMatch(c -> c.getId() != clienteEditar.getId() && c.getDni().equalsIgnoreCase(dniCliente))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Documento repetido o cliente inexistente.");
            return;
        }
        
        // Modificar los valores del cliente con los datos recibidos del formulario
        clienteEditar.setDni(dniCliente);
        clienteEditar.setNombre(nombreCliente);
        clienteEditar.setApellido(apellidoCliente);
        
        // Guardar los cambios en la base de datos
        BusinessCliente businessCliente = new BusinessCliente();
        Cliente clienteActualizado = businessCliente.actualizar(clienteEditar);
        
        if (clienteActualizado != null) {
            // Redirigir a alguna página de éxito o mostrar un mensaje de éxito
            response.sendRedirect("SvCliente");
        } else {
            // Redirigir a alguna página de error o mostrar un mensaje de error
            response.sendRedirect("index.jsp");
        }      
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
