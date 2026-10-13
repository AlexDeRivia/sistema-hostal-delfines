<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dominio.Cliente"%>
<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<% Cliente cliente = (Cliente) session.getAttribute("clienteEditar"); %>
<header class="page-header"><div><span class="page-kicker">Clientes · Edición</span><h1>Editar cliente</h1><p>Actualiza los datos de la persona seleccionada.</p></div><a class="btn btn-secondary" href="${pageContext.request.contextPath}/SvCliente"><i class="fas fa-arrow-left"></i> Volver</a></header>
<section class="form-surface" style="max-width:880px"><div class="kicker-line"></div><form action="${pageContext.request.contextPath}/SvEditCliente" method="post"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><div class="form-grid"><div class="field"><label for="dni">DNI</label><input id="dni" name="dni" type="text" value="<%=web.HtmlEscape.escape(cliente.getDni())%>" required></div><div class="field"><label for="nombre">Nombres</label><input id="nombre" name="nombre" type="text" value="<%=web.HtmlEscape.escape(cliente.getNombre())%>" required></div><div class="field"><label for="apellido">Apellidos</label><input id="apellido" name="apellido" type="text" value="<%=web.HtmlEscape.escape(cliente.getApellido())%>" required></div></div><div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/SvCliente">Cancelar</a><button class="btn btn-primary" type="submit"><i class="fas fa-check"></i> Guardar cambios</button></div></form></section>
<%@include file="components/footer.jsp"%>
