
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
                    try {
                        this.agregarEmpleado(request, response);
                        response.sendRedirect(request.getContextPath() + "/SvEmpleado?ok=creado");
                    } catch (IllegalArgumentException ex) {
                        request.setAttribute("error", ex.getMessage());
                        this.accionDefault(request, response);
                    }
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
        String nombre = InputRules.text(request.getParameter("nombreE"), "El nombre");
        String dni = InputRules.text(request.getParameter("dniE"), "El documento");
        String apellido = InputRules.text(request.getParameter("apellidoE"), "El apellido");
        String usuario = InputRules.text(request.getParameter("usuarioE"), "El usuario");
        String clave = request.getParameter("claveE");
        if (clave == null || clave.length() < 8) {
            throw new IllegalArgumentException("La clave debe tener al menos 8 caracteres.");
        }
        if (empleadoInformation.listado().stream().anyMatch(e -> e.getDni().equalsIgnoreCase(dni)
                || e.getUsername().equalsIgnoreCase(usuario))) {
            throw new IllegalArgumentException("Ya existe un empleado con ese documento o usuario.");
        }
        Empleado empleado = new Empleado();
        empleado.setDni(dni);
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setUsername(usuario);
        empleado.setClave(clave);
        empleadoInformation.crear(empleado);
    }
}
