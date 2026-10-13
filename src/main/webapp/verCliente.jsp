<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@page import="dominio.Cliente,java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>
<header class="page-header"><div><span class="page-kicker">Catálogos · Huéspedes</span><h1>Clientes</h1><p>Datos de las personas registradas para reservas y ventas.</p></div><div class="page-actions"><a class="btn btn-primary" href="${pageContext.request.contextPath}/agregarCliente.jsp"><i class="fas fa-plus"></i> Nuevo cliente</a></div></header>
<c:if test="${not empty sessionScope.errorCliente}"><div class="notice notice-error" role="alert"><i class="fas fa-exclamation-circle"></i><span><c:out value="${sessionScope.errorCliente}"/></span></div><c:remove var="errorCliente" scope="session"/></c:if>
<section class="surface"><div class="section-title"><h2>Directorio de clientes</h2></div>
<% List<Cliente> listaClientes = (List<Cliente>) session.getAttribute("listaClientes"); if (listaClientes != null && !listaClientes.isEmpty()) { %>
<div class="table-responsive"><table><thead><tr><th>ID</th><th>DNI</th><th>Nombres</th><th>Apellidos</th><th>Acciones</th></tr></thead><tbody><% for (Cliente cliente : listaClientes) { %><tr><td class="data-code">#<%=cliente.getId()%></td><td><%=web.HtmlEscape.escape(cliente.getDni())%></td><td><%=web.HtmlEscape.escape(cliente.getNombre())%></td><td><%=web.HtmlEscape.escape(cliente.getApellido())%></td><td class="actions-cell"><form action="${pageContext.request.contextPath}/SvEditCliente" method="get"><input type="hidden" name="id" value="<%=cliente.getId()%>"><button class="btn btn-secondary btn-sm" type="submit">Editar</button></form><form action="${pageContext.request.contextPath}/SvElimCliente" method="post" data-confirm="¿Eliminar este cliente?"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><input type="hidden" name="id" value="<%=cliente.getId()%>"><button class="btn btn-danger btn-sm" type="submit">Eliminar</button></form></td></tr><% } %></tbody></table></div>
<% } else { %><div class="empty-state"><i class="fas fa-users"></i><h3>Sin clientes</h3><p>Agrega el primer cliente para comenzar a gestionar reservas.</p><a class="btn btn-primary" href="${pageContext.request.contextPath}/agregarCliente.jsp">Crear cliente</a></div><% } %></section>
<%@include file="components/footer.jsp"%>
