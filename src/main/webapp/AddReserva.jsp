<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>
<header class="page-header"><div><span class="page-kicker">Reservas · Nueva estancia</span><h1>Nueva reserva</h1><p>Registra la habitación, el huésped y el pago.</p></div><div class="page-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/ServletControladorReserva"><i class="fas fa-arrow-left"></i> Volver a reservas</a></div></header>
<c:if test="${not empty error}"><div class="notice notice-error" role="alert"><i class="fas fa-exclamation-circle"></i><span><c:out value="${error}"/></span></div></c:if>
<c:if test="${empty habitaciones or empty clientes}"><div class="notice notice-warning"><i class="fas fa-info-circle"></i><span>Para reservar, registra primero una habitación habilitada y un cliente.</span></div></c:if>
<form action="${pageContext.request.contextPath}/SvAddReserva?accion=add" method="post">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
<div class="form-layout"><section class="form-surface"><div class="kicker-line"></div><h2>Datos de la estancia</h2><div class="form-grid">
<div class="field"><label for="roomNumber">Habitación</label><select id="roomNumber" name="roomId" required><option value="">Selecciona una habitación</option><c:forEach var="habitacion" items="${habitaciones}"><option value="${habitacion.id}" data-ventana="${habitacion.ventanaAfuera}" data-camas="${habitacion.numeroCamas}">Habitación ${habitacion.numero} · ${habitacion.numeroCamas} cama(s)</option></c:forEach></select></div>
<div class="field"><label for="clientName">Cliente</label><select id="clientName" name="clientId" required><option value="">Selecciona un cliente</option><c:forEach var="cliente" items="${clientes}"><option value="${cliente.id}"><c:out value="${cliente.nombre}"/> <c:out value="${cliente.apellido}"/></option></c:forEach></select></div>
<div class="field"><label for="checkInDate">Fecha de ingreso</label><input type="date" id="checkInDate" name="checkInDate" value="${fechaHoy}" required></div>
<div class="field"><label for="checkInTime">Hora de ingreso</label><input type="text" id="checkInTime" name="checkInTime" pattern="([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]" placeholder="14:00:00" required><small class="field-hint">Formato de 24 horas: HH:MM:SS</small></div>
<div class="field"><label for="reservedHours">Duración</label><select id="reservedHours" name="reservedHours" required><option value="3">3 horas</option><option value="24">24 horas</option></select></div>
<div class="field"><label for="numberOfGuests">Número de huéspedes</label><input type="number" min="1" id="numberOfGuests" name="numberOfGuests" required></div>
</div><div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/ServletControladorReserva">Cancelar</a><button class="btn btn-primary" type="submit"><i class="fas fa-check"></i> Guardar reserva</button></div></section>
<aside class="summary-panel"><span class="page-kicker">Resumen de pago</span><h2>Costo estimado</h2><label class="summary-label" for="costResult">Estancia</label><input type="text" id="costResult" name="costResult" readonly aria-label="Costo estimado de la estancia"><button class="btn btn-secondary" type="button" onclick="calcularCosto()"><i class="fas fa-sync-alt"></i> Recalcular</button><div class="field" style="margin-top:24px"><label for="cashPayment">Efectivo · S/</label><input type="number" min="0" step="0.01" id="cashPayment" name="cashPayment" value="0"></div><div class="field"><label for="yapePayment">Yape · S/</label><input type="number" min="0" step="0.01" id="yapePayment" name="yapePayment" value="0"></div><small>El total y el pago se validan al guardar.</small></aside></div>
</form>
<%@include file="components/footer.jsp"%>
