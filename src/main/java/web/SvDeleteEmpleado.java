package web;

import dominio.BusinessEmpleado;
import dominio.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SvDeleteEmpleado")
public class SvDeleteEmpleado extends HttpServlet{

    private BusinessEmpleado empleadoInformation = new BusinessEmpleado();
    
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
                    this.eliminarEmpleado(request, response);
                    System.out.println("Eliminando");
                    break;
                default:
                    
            }
        } else {
            //this.accionDefault(request, response);
            System.out.println("No se pudo acceder a la opcion de eliminar");
        }
    }
    
    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Recuperamos datos del formulario
        int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        Empleado empleado = empleadoInformation.buscar(idEmpleado);
        
        if (empleado != null) {
            empleadoInformation.eliminar(empleado);
            System.out.println("eliminando");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            System.out.println("Algo salio mal al eliminar");
        }
    }
    
}
