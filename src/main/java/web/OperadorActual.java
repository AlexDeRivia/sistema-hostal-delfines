package web;

import dominio.Administrador;
import dominio.Empleado;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

final class OperadorActual {
    private OperadorActual() {
    }

    static Empleado desde(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("empleado") instanceof Empleado) {
            return (Empleado) session.getAttribute("empleado");
        }
        if (session != null && session.getAttribute("admin") instanceof Administrador) {
            return OperadorDemo.administrador();
        }
        throw new IllegalStateException("No hay un operador autenticado");
    }
}
