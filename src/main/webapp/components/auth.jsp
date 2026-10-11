<%@page import="dominio.Empleado"%>
<%@page import="dominio.Administrador"%>

<%
    Object login = request.getSession().getAttribute("login");
    Empleado empleado = (Empleado) request.getSession().getAttribute("empleado");
    Administrador admin = (Administrador) request.getSession().getAttribute("admin");
    
    if (login == null ) {
        response.sendRedirect("acceso.jsp");
        return;
    }
%>
