package web;

import dominio.BusinessEmpleado;
import dominio.Empleado;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SvLoginEmpleado", urlPatterns = {"/SvLoginEmpleado"})
public class SvLoginEmpleado extends HttpServlet {

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

        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        BusinessEmpleado logica = new BusinessEmpleado();
        Empleado empleado = logica.login(usuario, clave);

        if (empleado != null) {
            HttpSession old = request.getSession(false);
            if (old != null) {
                old.invalidate();
            }
            HttpSession session = request.getSession();
            session.setAttribute("login", empleado);
            session.setAttribute("empleado", empleado);
            response.sendRedirect("index.jsp");
        } else {
            String errorMessage = "Usuario o clave incorrectos";
            request.setAttribute("error", errorMessage);
            RequestDispatcher dispatcher = request.getRequestDispatcher("loginEmpleado.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
