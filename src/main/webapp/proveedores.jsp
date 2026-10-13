<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>
<header class="page-header"><div><span class="page-kicker">Catálogos · Abastecimiento</span><h1>Proveedores</h1><p>Datos de las empresas que suministran productos al hostal.</p></div></header>
<c:if test="${not empty error}"><div class="notice notice-error" role="alert"><i class="fas fa-exclamation-circle"></i><span><c:out value="${error}"/></span></div></c:if>
<c:if test="${not empty param.ok}"><div class="notice notice-success" role="status"><i class="fas fa-check-circle"></i><span>Proveedor guardado correctamente.</span></div></c:if>
<div class="ledger-layout"><section class="form-surface"><div class="kicker-line"></div><h2>Nuevo proveedor</h2><form method="post" action="${pageContext.request.contextPath}/SvProveedor"><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><div class="field"><label for="razonSocial">Razón social</label><input id="razonSocial" name="razonSocial" required maxlength="100"></div><div class="field"><label for="ruc">RUC</label><input id="ruc" name="ruc" required pattern="[0-9]{11}" maxlength="11" inputmode="numeric"><small class="field-hint">11 dígitos</small></div><div class="form-actions"><button class="btn btn-primary" type="submit"><i class="fas fa-plus"></i> Agregar proveedor</button></div></form></section>
<section class="surface"><div class="section-title"><h2>Directorio</h2></div><c:choose><c:when test="${empty proveedores}"><div class="empty-state"><i class="far fa-building"></i><h3>Sin proveedores</h3><p>Agrega el primer proveedor para registrar compras.</p></div></c:when><c:otherwise><div class="table-responsive"><table><thead><tr><th>Razón social</th><th>RUC</th></tr></thead><tbody><c:forEach var="proveedor" items="${proveedores}"><tr><td><c:out value="${proveedor.razonSocial}"/></td><td class="data-code"><c:out value="${proveedor.ruc}"/></td></tr></c:forEach></tbody></table></div></c:otherwise></c:choose></section></div>
<%@include file="components/footer.jsp"%>
