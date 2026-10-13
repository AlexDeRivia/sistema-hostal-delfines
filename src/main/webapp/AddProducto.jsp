<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>
<header class="page-header"><div><span class="page-kicker">Inventario · Nuevo</span><h1>Nuevo producto</h1><p>Registra sus precios y categoría.</p></div><div class="page-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/SvProducto"><i class="fas fa-arrow-left"></i> Volver</a></div></header>
<c:if test="${not empty error}"><div class="notice notice-error" role="alert"><i class="fas fa-exclamation-circle"></i><span><c:out value="${error}"/></span></div></c:if>
<section class="form-surface" style="max-width:880px"><div class="kicker-line"></div><form action="${pageContext.request.contextPath}/SvAddProducto?accion=add" method="post"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><div class="form-grid"><div class="field"><label for="producto">Nombre</label><input id="producto" name="producto" type="text" required></div><div class="field"><label for="pventa">Precio de venta · S/</label><input id="pventa" name="pventa" type="number" min="0.01" step="0.01" required></div><div class="field"><label for="pcompra">Precio de compra · S/</label><input id="pcompra" name="pcompra" type="number" min="0.01" step="0.01" required></div><div class="field"><label for="categoria">Categoría</label><select id="categoria" name="categoria" required><option value="ALIMENTOS">Alimentos</option><option value="USO_PERSONAL">Uso personal</option></select></div></div><div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/SvProducto">Cancelar</a><button class="btn btn-primary" type="submit"><i class="fas fa-check"></i> Crear producto</button></div></form></section>
<%@include file="components/footer.jsp"%>
