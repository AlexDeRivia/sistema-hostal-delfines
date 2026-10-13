<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<header class="page-header"><div><span class="page-kicker">Clientes · Nuevo</span><h1>Nuevo cliente</h1><p>Registra los datos necesarios para una reserva o venta.</p></div><a class="btn btn-secondary" href="${pageContext.request.contextPath}/SvCliente"><i class="fas fa-arrow-left"></i> Volver</a></header>
<c:if test="${not empty error}"><div class="notice notice-error" role="alert"><i class="fas fa-exclamation-circle"></i><span><c:out value="${error}"/></span></div></c:if>
<section class="form-surface" style="max-width:880px"><div class="kicker-line"></div><form action="${pageContext.request.contextPath}/SvCliente" method="post"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><div class="form-grid"><div class="field"><label for="dni">DNI</label><input id="dni" name="dni" type="text" inputmode="numeric" required></div><div class="field"><label for="nombre">Nombres</label><input id="nombre" name="nombre" type="text" required></div><div class="field"><label for="apellido">Apellidos</label><input id="apellido" name="apellido" type="text" required></div></div><div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/SvCliente">Cancelar</a><button class="btn btn-primary" type="submit"><i class="fas fa-check"></i> Crear cliente</button></div></form></section>
<%@include file="components/footer.jsp"%>
