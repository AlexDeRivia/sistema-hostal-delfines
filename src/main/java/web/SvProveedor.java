package web;

import dominio.BusinessProveedor;
import dominio.Proveedor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SvProveedor")
public class SvProveedor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("proveedores", new BusinessProveedor().listado());
        request.getRequestDispatcher("proveedores.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String razon;
        String ruc = request.getParameter("ruc");
        try {
            razon = InputRules.text(request.getParameter("razonSocial"), "La razón social");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            doGet(request, response);
            return;
        }
        if (ruc == null || !ruc.matches("[0-9]{11}")) {
            request.setAttribute("error", "Indica un RUC de 11 dígitos.");
            doGet(request, response);
            return;
        }
        BusinessProveedor proveedores = new BusinessProveedor();
        if (proveedores.listado().stream().anyMatch(item -> item.getRuc().equals(ruc))) {
            request.setAttribute("error", "Ya existe un proveedor con ese RUC.");
            doGet(request, response);
            return;
        }
        Proveedor proveedor = new Proveedor();
        proveedor.setRazonSocial(razon.trim());
        proveedor.setRuc(ruc);
        proveedores.crear(proveedor);
        response.sendRedirect(request.getContextPath() + "/SvProveedor?ok=1");
    }
}
