<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>
<header class="page-header"><div><span class="page-kicker">Reservas · Edición</span><h1>Editar reserva <span class="muted">#${reserva.id}</span></h1><p>Actualiza los datos de la estancia y su estado.</p></div><div class="page-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/ServletControladorReserva"><i class="fas fa-arrow-left"></i> Volver a reservas</a></div></header>
<c:if test="${not empty error}"><div class="notice notice-error" role="alert"><i class="fas fa-exclamation-circle"></i><span><c:out value="${error}"/></span></div></c:if>
<form action="${pageContext.request.contextPath}/SvUpdateReserva?accion=update" method="post">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><input type="hidden" id="reservationId" name="reservationId" value="${reserva.id}">
<div class="form-layout"><section class="form-surface"><div class="kicker-line"></div><h2>Datos de la estancia</h2><div class="form-grid">
<div class="field"><label for="roomNumber">Habitación</label><select id="roomNumber" name="roomId" required><c:forEach var="habitacion" items="${habitaciones}"><option value="${habitacion.id}" data-ventana="${habitacion.ventanaAfuera}" data-camas="${habitacion.numeroCamas}" ${habitacion.id eq reserva.habitacion.id ? 'selected' : ''}>Habitación ${habitacion.numero}</option></c:forEach></select></div>
<div class="field"><label for="clientName">Cliente</label><select id="clientName" name="clientId" required><c:forEach var="cliente" items="${clientes}"><option value="${cliente.id}" ${cliente.id eq reserva.cliente.id ? 'selected' : ''}><c:out value="${cliente.nombre}"/> <c:out value="${cliente.apellido}"/></option></c:forEach></select></div>
<div class="field"><label for="checkInDate">Fecha de ingreso</label><input type="date" id="checkInDate" name="checkInDate" value="${reserva.fechaIngreso}" required></div>
<div class="field"><label for="checkInTime">Hora de ingreso</label><input type="text" id="checkInTime" name="checkInTime" pattern="([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]" value="<c:out value='${reserva.hora_ingreso}'/>" required></div>
<div class="field"><label for="reservedHours">Duración</label><select id="reservedHours" name="reservedHours" required><option value="3" ${reserva.horas_reservadas eq '3' ? 'selected' : ''}>3 horas</option><option value="24" ${reserva.horas_reservadas eq '24' ? 'selected' : ''}>24 horas</option></select></div>
<div class="field"><label for="numberOfGuests">Número de huéspedes</label><input type="number" min="1" id="numberOfGuests" name="numberOfGuests" value="${reserva.numeroHuespedes}" required></div>
<div class="field span-2"><label for="reservationStatus">Estado</label><select id="reservationStatus" name="reservationStatus" required><c:forEach var="estado" items="${estados}"><option value="${estado}" ${estado eq reserva.estadoReserva ? 'selected' : ''}>${estado}</option></c:forEach></select></div>
</div><div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/ServletControladorReserva">Cancelar</a><button class="btn btn-primary" type="submit"><i class="fas fa-check"></i> Guardar cambios</button></div></section>
<aside class="summary-panel"><span class="page-kicker">Resumen de pago</span><h2>Costo de la estancia</h2><label class="summary-label" for="costResult">Estancia</label><input type="text" id="costResult" name="costResult" readonly><button class="btn btn-secondary" type="button" onclick="calcularCosto()"><i class="fas fa-sync-alt"></i> Recalcular</button><div class="field" style="margin-top:24px"><label for="cashPayment">Efectivo · S/</label><input type="number" min="0" step="0.01" id="cashPayment" name="cashPayment" value="${reserva.costoEfectivo}"></div><div class="field"><label for="yapePayment">Yape · S/</label><input type="number" min="0" step="0.01" id="yapePayment" name="yapePayment" value="${reserva.costoYape}"></div></aside></div>
</form>
<%@include file="components/footer.jsp"%>
