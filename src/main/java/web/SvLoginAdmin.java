package web;

import dominio.Administrador;
import dominio.BusinessAdministrador;
import dominio.Usuario;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SvLoginAdmin", urlPatterns = {"/SvLoginAdmin"})
public class SvLoginAdmin extends HttpServlet {

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

        BusinessAdministrador logica = new BusinessAdministrador();
        Administrador admin = logica.login(usuario, clave);

        if (admin != null) {
            HttpSession old = request.getSession(false);
            if (old != null) {
                old.invalidate();
            }
            HttpSession session = request.getSession();
            session.setAttribute("login", admin);
            session.setAttribute("admin", admin);
            response.sendRedirect("index.jsp");
        } else {

            String errorMessage = "Usuario o clave incorrectos";
            request.setAttribute("error", errorMessage);
            RequestDispatcher dispatcher = request.getRequestDispatcher("loginAdmin.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
