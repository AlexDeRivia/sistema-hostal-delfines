
package web;

import dominio.BusinessEmpleado;
import dominio.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SvUpdateEmpleado")
public class SvUpdateEmpleado extends HttpServlet{
    
   private BusinessEmpleado empleadoInformation = new BusinessEmpleado();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "actualizar":
                    this.actualizarEmpleado(request, response);
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
                    this.modificarEmpleado(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private void actualizarEmpleado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int empleado_id = Integer.parseInt(request.getParameter("idEmpleado"));
        Empleado empleado = empleadoInformation.buscar(empleado_id);
        
        request.setAttribute("empleado", empleado);
        String jspUpdate = "UpdateEmpleado.jsp";
        request.getRequestDispatcher(jspUpdate).forward(request, response);
    }
    
    private void modificarEmpleado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Recuperamos datos del formulario
        Integer id = Integer.valueOf(request.getParameter("idE"));
        String dni = request.getParameter("dniE");
        String nombre = request.getParameter("nombreE");
        String apellido = request.getParameter("apellidoE");
        String usuario = request.getParameter("usuarioE");
        String clave = request.getParameter("claveE");
        Empleado empleado = new Empleado();
        empleado.setId(id);
        empleado.setDni(dni);
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setUsername(usuario);
        empleado.setClave(clave);
 
        empleadoInformation.actualizar(empleado);
        System.out.println("modificando");
        this.accionDefault(request, response);
    }

}
