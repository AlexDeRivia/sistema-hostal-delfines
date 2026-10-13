<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>
<header class="page-header">
  <div><span class="page-kicker">Operación · Estancias</span><h1>Reservas</h1><p>Consulta llegadas, pagos y el estado de cada estancia.</p></div>
  <div class="page-actions"><span class="page-meta"><i class="far fa-clock"></i> Turno <strong><c:out value="${rreserva.turno}"/></strong></span><a class="btn btn-primary" href="${pageContext.request.contextPath}/SvAddReserva"><i class="fas fa-plus"></i> Nueva reserva</a></div>
</header>
<c:if test="${not empty param.ok}"><div class="notice notice-success" role="status"><i class="fas fa-check-circle"></i><span>Reserva <c:out value="${param.ok}"/> correctamente.</span></div></c:if>
<section class="surface">
  <div class="section-title"><h2>Agenda de reservas</h2><small><c:out value="${rreserva.fecha}"/></small></div>
  <form class="filter-bar" method="get" action="${pageContext.request.contextPath}/ServletControladorReserva">
    <div class="field"><label for="fechaFiltro">Fecha de ingreso</label><input id="fechaFiltro" type="date" name="fecha" value="<c:out value='${param.fecha}'/>"></div>
    <div class="field"><label for="estadoFiltro">Estado</label><select id="estadoFiltro" name="estado"><option value="">Todos</option><option value="PENDIENTE" ${param.estado eq 'PENDIENTE' ? 'selected' : ''}>Pendiente</option><option value="CONFIRMADO" ${param.estado eq 'CONFIRMADO' ? 'selected' : ''}>Confirmado</option><option value="FINALIZADO" ${param.estado eq 'FINALIZADO' ? 'selected' : ''}>Finalizado</option><option value="CANCELADO" ${param.estado eq 'CANCELADO' ? 'selected' : ''}>Cancelado</option></select></div>
    <button class="btn btn-secondary" type="submit"><i class="fas fa-filter"></i> Filtrar</button>
  </form>
  <c:choose><c:when test="${empty reservas}"><div class="empty-state"><i class="far fa-calendar"></i><h3>Sin reservas para este filtro</h3><p>Cambia la fecha o el estado, o registra una estancia nueva.</p><a class="btn btn-primary" href="${pageContext.request.contextPath}/SvAddReserva">Crear reserva</a></div></c:when><c:otherwise>
  <div class="table-responsive"><table><thead><tr><th>ID</th><th>Cliente</th><th>Habitación</th><th>Ingreso</th><th>Salida</th><th>Estado</th><th class="numeric">Pagado</th><th>Acciones</th></tr></thead><tbody>
  <c:forEach var="reserva" items="${reservas}"><tr><td class="data-code">#${reserva.id}</td><td><c:out value="${reserva.cliente.nombre}"/> <c:out value="${reserva.cliente.apellido}"/></td><td>${reserva.habitacion.numero}</td><td><c:out value="${reserva.fechaIngreso}"/><br><span class="muted"><c:out value="${reserva.hora_ingreso}"/></span></td><td><c:out value="${reserva.fechaSalida}"/><br><span class="muted"><c:out value="${reserva.hora_salida}"/></span></td><td><span class="status-pill status-${reserva.estadoReserva eq 'PENDIENTE' ? 'pending' : reserva.estadoReserva eq 'CONFIRMADO' ? 'confirmed' : reserva.estadoReserva eq 'FINALIZADO' ? 'finalized' : 'cancelled'}"><c:out value="${reserva.estadoReserva}"/></span></td><td class="numeric">S/ ${reserva.costoEfectivo + reserva.costoYape}</td><td class="actions-cell"><a href="${pageContext.request.contextPath}/SvUpdateReserva?accion=actualizar&amp;idReserva=${reserva.id}" class="btn btn-secondary btn-sm" aria-label="Editar reserva ${reserva.id}"><i class="fas fa-pen"></i> Editar</a><form action="${pageContext.request.contextPath}/SvDeleteReserva?accion=eliminar" method="post" data-confirm="¿Eliminar esta reserva? Esta acción no se puede deshacer."><input type="hidden" name="_csrf" value="${sessionScope.csrfToken}"><input type="hidden" name="idReserva" value="${reserva.id}"><button type="submit" class="btn btn-danger btn-sm" aria-label="Eliminar reserva ${reserva.id}"><i class="fas fa-trash"></i></button></form></td></tr></c:forEach>
  </tbody></table></div></c:otherwise></c:choose>
</section>
<%@include file="components/footer.jsp"%>
