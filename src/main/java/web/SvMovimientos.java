package web;

import dominio.BusinessRegistroCompra;
import dominio.BusinessRegistroVenta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SvMovimientos")
public class SvMovimientos extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("registrosCompra", new BusinessRegistroCompra().listado());
        request.setAttribute("registrosVenta", new BusinessRegistroVenta().listado());
        request.getRequestDispatcher("movimientos.jsp").forward(request, response);
    }
}
