package web;

import dominio.BusinessCliente;
import dominio.BusinessHabitacion;
import dominio.BusinessRegistroReserva;
import dominio.BusinessReserva;
import dominio.Cliente;
import dominio.Empleado;
import dominio.EstadoReserva;
import dominio.Habitacion;
import dominio.RegistroReserva;
import dominio.Reserva;
import dominio.ReservaService;
import dominio.Turno;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SvDeleteReserva")
public class SvDeleteReserva extends HttpServlet{
    
    private BusinessRegistroReserva inforreserva = new BusinessRegistroReserva();
    private RegistroReserva rreserva;
    
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
                    this.eliminarReserva(request, response);
                    System.out.println("Eliminando");
                    break;
                default:
                    
            }
        } else {
            //this.accionDefault(request, response);
            System.out.println("No se pudo acceder a la opcion de eliminar");
        }
    }
    
    private void eliminarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Recuperamos datos del formulario
        try {
            ReservaService.eliminar(Integer.parseInt(request.getParameter("idReserva")));
            response.sendRedirect(request.getContextPath() + "/ServletControladorReserva?ok=eliminada");
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }
    
}
