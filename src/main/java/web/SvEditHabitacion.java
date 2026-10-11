package web;

import dominio.BusinessHabitacion;
import dominio.Habitacion;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SvEditHabitacion", urlPatterns = {"/SvEditHabitacion"})
public class SvEditHabitacion extends HttpServlet {

  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
           /*Recibir el id mediante la solicitud del boton Editar*/
         int id = Integer.parseInt(request.getParameter("id"));

        BusinessHabitacion businessHabitacion = new BusinessHabitacion();
        Habitacion habitacionEditar = businessHabitacion.buscar(id);
        
        // Almacenar la habitacion a editar en la sesión
        HttpSession session = request.getSession();
        session.setAttribute("habitacionEditar", habitacionEditar);
        
        // Redireccionar a la página de edición
        response.sendRedirect("editarHabitacion.jsp");
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        /*Recibir los datos de habitacion*/
        int numeroHabitacion = Integer.parseInt(request.getParameter("numero"));
        int numeroCamasHabitacion = Integer.parseInt(request.getParameter("numeroCamas"));
       
        String ventanaAfueraStr = request.getParameter("ventanaAfuera");
        boolean ventanaAfueraHabitacion = ventanaAfueraStr.equals("true");
        String disponibleStr = request.getParameter("disponible");
        boolean disponibleHabitacion = disponibleStr.equals("true");
        
     
        HttpSession session = request.getSession();
        Habitacion habitacionEditar = (Habitacion) session.getAttribute("habitacionEditar");
        
        // Modificar los valores de habitacion con los datos recibidos del formulario
        habitacionEditar.setNumero(numeroHabitacion);
        habitacionEditar.setNumeroCamas(numeroCamasHabitacion);
        habitacionEditar.setVentanaAfuera(ventanaAfueraHabitacion);
        habitacionEditar.setDisponible(disponibleHabitacion);
        
        
        // Guardar los cambios en la base de datos
        BusinessHabitacion businessHabitacion = new BusinessHabitacion();
        Habitacion habitacionActualizado = businessHabitacion.actualizar(habitacionEditar);
        
        if (habitacionActualizado != null) {
            // Redirigir a alguna página de éxito o mostrar un mensaje de éxito
            response.sendRedirect("SvHabitacion");
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
