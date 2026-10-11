package web;

import dominio.BusinessHabitacion;
import dominio.Habitacion;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SvElimHabitacion", urlPatterns = {"/SvElimHabitacion"})
public class SvElimHabitacion extends HttpServlet {


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

        // Crear una instancia de Habitacion con el ID proporcionado
        Habitacion habitacionEliminar = new Habitacion();
        habitacionEliminar.setId(id);

        // Instanciar BusinessHabitacion y llamar al método eliminar
        BusinessHabitacion businessHabitacion = new BusinessHabitacion();
        Habitacion habitacionEliminado = businessHabitacion.eliminar(habitacionEliminar);

        if (habitacionEliminado != null) {
            // Cliente eliminado exitosamente
            response.sendRedirect("agregarHabitacion.jsp");
        } else {
            // Ocurrió un error al eliminar el cliente
            response.sendRedirect("verHabitacion.jsp");          
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
