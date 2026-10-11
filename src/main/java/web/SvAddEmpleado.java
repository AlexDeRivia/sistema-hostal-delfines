
package web;

import dominio.BusinessEmpleado;
import dominio.Empleado;
import dominio.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SvAddEmpleado")
public class SvAddEmpleado extends HttpServlet{
   
    private BusinessEmpleado empleadoInformation = new BusinessEmpleado();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.accionDefault(request, response);
        //this.iniciarRegistroReserva();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        System.out.println("doPost");
        if (accion != null) {
            switch (accion) {
                case "add":
                    this.agregarEmpleado(request, response);
                    this.accionDefault(request, response);
                    System.out.println("Agregando");
                    break;
                default:
                    this.accionDefault(request, response);
            }
        }else{
            this.accionDefault(request, response);
        }
    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGET 1");
        request.getRequestDispatcher("AddEmpleado.jsp").forward(request, response);
    }

    private void agregarEmpleado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Recuperamos datos del formulario
        String nombre = request.getParameter("nombreE");
        String dni = request.getParameter("dniE");
        String apellido = request.getParameter("apellidoE");
        String usuario = request.getParameter("usuarioE");
        String clave = request.getParameter("claveE");
        if(!nombre.equals("") && !apellido.equals("") && !apellido.equals("") && !usuario.equals("") && !clave.equals("")){
            Empleado empleado = new Empleado();
                empleado.setDni(dni);
                empleado.setNombre(nombre);
                empleado.setApellido(apellido);
                empleado.setUsername(usuario);
                empleado.setClave(clave);
            empleadoInformation.crear(empleado);
            
        }
        System.out.println("SE GUARDO");
    }
}
