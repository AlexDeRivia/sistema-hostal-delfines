package web;


import dominio.BusinessHabitacion;
import dominio.Habitacion;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet(name = "SvHabitacion", urlPatterns = {"/SvHabitacion"})
public class SvHabitacion extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        BusinessHabitacion businessHabitacion = new BusinessHabitacion();
        List<Habitacion> listaHabitaciones = businessHabitacion.listado();
       
        // Almacenar la lista de habitaciones en un atributo de sesión
        HttpSession session = request.getSession();
        session.setAttribute("listaHabitaciones", listaHabitaciones);
    
        // Redirigir al usuario a la página verHabitacion.jsp
        response.sendRedirect("verHabitacion.jsp");
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int numero = Integer.parseInt(request.getParameter("numero"));
        int numeroCamas = Integer.parseInt(request.getParameter("numeroCamas"));
        String ventanaAfueraStr = request.getParameter("ventanaAfuera");
        boolean ventanaAfuera = ventanaAfueraStr.equals("true");
        String disponibleStr = request.getParameter("disponible");
        boolean disponible = disponibleStr.equals("true");
    
          // Crear una instancia de Habitacion con los parámetros recibidos
        Habitacion nuevaHabitacion = new Habitacion(numero, numeroCamas, ventanaAfuera,disponible);

        // Llamar al método crear en BusinessHabitacion para agregar la nueva habitacion
        BusinessHabitacion businessHabitacion = new BusinessHabitacion();
        Habitacion habitacionCreado = businessHabitacion.crear(nuevaHabitacion);

        // Manejar el resultado de la operación de creación de la habitacion
        if (habitacionCreado != null) {
            // Habitacion creado exitosamente
            response.sendRedirect("agregarHabitacion.jsp"); // Redirigir a la página principal
        } else {
            // Ocurrió un error al crear la habitacion
            String errorMessage = "Error al crear la habitacion";
            request.setAttribute("error", errorMessage);
            // Redirigir de vuelta al formulario de creación de habitacion
            request.getRequestDispatcher("agregarHabitacion.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
