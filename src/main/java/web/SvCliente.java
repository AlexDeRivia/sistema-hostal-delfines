package web;

import dominio.BusinessCliente;
import dominio.Cliente;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet(name = "SvCliente", urlPatterns = {"/SvCliente"})
public class SvCliente extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
        BusinessCliente businessCliente = new BusinessCliente();
        List<Cliente> listaClientes = businessCliente.listado();
    
        // Almacenar la lista de clientes en un atributo de sesión
        HttpSession session = request.getSession();
        session.setAttribute("listaClientes", listaClientes);
    
        // Redirigir al usuario a la página verCliente.jsp
        response.sendRedirect("verCliente.jsp");
     
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String dni;
        String nombre;
        String apellido;
        try {
            dni = InputRules.text(request.getParameter("dni"), "El documento");
            nombre = InputRules.text(request.getParameter("nombre"), "El nombre");
            apellido = InputRules.text(request.getParameter("apellido"), "El apellido");
            if (new BusinessCliente().listado().stream().anyMatch(c -> c.getDni().equalsIgnoreCase(dni))) {
                throw new IllegalArgumentException("Ya existe un cliente con ese documento.");
            }
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("agregarCliente.jsp").forward(request, response);
            return;
        }
    
          // Crear una instancia de Cliente con los parámetros recibidos
        Cliente nuevoCliente = new Cliente(dni, nombre, apellido);

        // Llamar al método crear en BusinessCliente para agregar el nuevo cliente
        BusinessCliente businessCliente = new BusinessCliente();
        Cliente clienteCreado = businessCliente.crear(nuevoCliente);

        // Manejar el resultado de la operación de creación del cliente
        if (clienteCreado != null) {
            // Cliente creado exitosamente
            response.sendRedirect("SvCliente");
        } else {
            // Ocurrió un error al crear el cliente
            String errorMessage = "Error al crear el cliente";
            request.setAttribute("error", errorMessage);
            // Redirigir de vuelta al formulario de creación de cliente
            request.getRequestDispatcher("agregarCliente.jsp").forward(request, response);
        }      
     }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
