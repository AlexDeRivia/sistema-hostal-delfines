<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@page import="dominio.Habitacion,java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>
<header class="page-header"><div><span class="page-kicker">Catálogos · Alojamiento</span><h1>Habitaciones</h1><p>Capacidad y disponibilidad de cada habitación.</p></div><div class="page-actions"><a class="btn btn-primary" href="${pageContext.request.contextPath}/agregarHabitacion.jsp"><i class="fas fa-plus"></i> Nueva habitación</a></div></header>
<c:if test="${not empty sessionScope.errorHabitacion}"><div class="notice notice-error" role="alert"><i class="fas fa-exclamation-circle"></i><span><c:out value="${sessionScope.errorHabitacion}"/></span></div><c:remove var="errorHabitacion" scope="session"/></c:if>
<section class="surface"><div class="section-title"><h2>Habitaciones registradas</h2></div>
<% List<Habitacion> listaHabitaciones = (List<Habitacion>) session.getAttribute("listaHabitaciones"); if (listaHabitaciones != null && !listaHabitaciones.isEmpty()) { %>
<div class="table-responsive"><table><thead><tr><th>ID</th><th>Habitación</th><th>Camas</th><th>Estado</th><th>Ventana exterior</th><th>Acciones</th></tr></thead><tbody><% for (Habitacion habitacion : listaHabitaciones) { %><tr><td class="data-code">#<%=habitacion.getId()%></td><td class="data-code"><%=habitacion.getNumero()%></td><td><%=habitacion.getNumeroCamas()%></td><td><span class="status-pill <%=habitacion.isDisponible() ? "status-enabled" : "status-disabled"%>"><%=habitacion.isDisponible() ? "Habilitada" : "No habilitada"%></span></td><td><%=habitacion.isVentanaAfuera() ? "Sí" : "No"%></td><td class="actions-cell"><form action="${pageContext.request.contextPath}/SvEditHabitacion" method="get"><input type="hidden" name="id" value="<%=habitacion.getId()%>"><button class="btn btn-secondary btn-sm" type="submit">Editar</button></form><form action="${pageContext.request.contextPath}/SvElimHabitacion" method="post" data-confirm="¿Eliminar esta habitación?"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><input type="hidden" name="id" value="<%=habitacion.getId()%>"><button class="btn btn-danger btn-sm" type="submit">Eliminar</button></form></td></tr><% } %></tbody></table></div>
<% } else { %><div class="empty-state"><i class="fas fa-door-open"></i><h3>Sin habitaciones</h3><p>Agrega la primera habitación para habilitar reservas.</p><a class="btn btn-primary" href="${pageContext.request.contextPath}/agregarHabitacion.jsp">Crear habitación</a></div><% } %></section>
<%@include file="components/footer.jsp"%>
