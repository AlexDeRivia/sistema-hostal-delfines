<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<header class="page-header"><div><span class="page-kicker">Habitaciones · Nueva</span><h1>Nueva habitación</h1><p>Define su capacidad y disponibilidad para reservas.</p></div><a class="btn btn-secondary" href="${pageContext.request.contextPath}/SvHabitacion"><i class="fas fa-arrow-left"></i> Volver</a></header>
<c:if test="${not empty error}"><div class="notice notice-error" role="alert"><i class="fas fa-exclamation-circle"></i><span><c:out value="${error}"/></span></div></c:if>
<section class="form-surface" style="max-width:880px"><div class="kicker-line"></div><form action="${pageContext.request.contextPath}/SvHabitacion" method="post"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><div class="form-grid"><div class="field"><label for="numero">Número de habitación</label><input id="numero" name="numero" type="number" min="1" required></div><div class="field"><label for="numeroCamas">Número de camas</label><input id="numeroCamas" name="numeroCamas" type="number" min="1" required></div><div class="field"><label for="ventanaAfuera">Ventana exterior</label><select id="ventanaAfuera" name="ventanaAfuera" required><option value="">Seleccionar</option><option value="true">Sí</option><option value="false">No</option></select></div><div class="field"><label for="disponible">Habilitada para reservas</label><select id="disponible" name="disponible" required><option value="">Seleccionar</option><option value="true">Sí</option><option value="false">No</option></select></div></div><div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/SvHabitacion">Cancelar</a><button class="btn btn-primary" type="submit"><i class="fas fa-check"></i> Crear habitación</button></div></form></section>
<%@include file="components/footer.jsp"%>
