<%@include file="components/auth.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="jakarta.servlet.http.*" %>
<%@ page import="dominio.RegistroReserva" %>
<% HttpSession sessionr = request.getSession(); %>
<% RegistroReserva srreserva = (RegistroReserva) sessionr.getAttribute("srreserva");%>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<link rel="stylesheet" type="text/css" href="dist/css/styles.css"/>
<script type="text/javascript" src="dist/js/funtions.js"/></script>
<link rel="stylesheet" href="dist/css/stylemcompra.css">

<form class="miform" name="formReserva" action="${pageContext.request.contextPath}/SvUpdateReserva?accion=update" method="post" id="clearReserva">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
    <h1>ACTUALIZAR RESERVA</h1>

    <label for="reservationId">ID:</label>
    <input type="text" id="reservationId" name="reservationId" required value="${reserva.id}">
    <br>

    <label for="roomNumber">N� Habitaci�n:</label>
    <select id="roomNumber" name="roomNumber" required >
        <c:forEach var="habitacion" items="${habitaciones}">
            <c:choose>
                <c:when test="${habitacion.numero eq reserva.habitacion.numero}">
                    <option value="${habitacion.numero}" data-ventana="${habitacion.ventanaAfuera}" data-camas="${habitacion.numeroCamas}" selected>${reserva.habitacion.numero}</option>
                </c:when>
                <c:otherwise>
                    <option value="${habitacion.numero}" data-ventana="${habitacion.ventanaAfuera}" data-camas="${habitacion.numeroCamas}">${habitacion.numero}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
    <br>

    <label for="checkInTime">Hora de Ingreso:</label>
    <input type="text" id="checkInTime" name="checkInTime" pattern="([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]" required value="<c:out value='${reserva.hora_ingreso}'/>">
    <br>

    <label for="reservedHours">Horas Reservadas:</label>
    <input type="number" id="reservedHours" name="reservedHours" required value="<c:out value='${reserva.horas_reservadas}'/>">
    <br>

    <label for="additionalHours">Horas Adicionales:</label>
    <input type="number" id="additionalHours" name="additionalHours" required>
    <br>

    <label for="calculateNewCost">Calcular Nuevo Costo:</label>
    <button type="button" class="calculate" onclick="calcularNuevoCosto()" style="margin-bottom: 10px">Calcular</button>
    <input type="text" id="newCostResult" name="newCostResult" class="form-control"  readonly>
    <br>

    <label for="cashPayment">S/. EFECTIVO:</label>
    <input type="text" id="cashPayment" name="cashPayment">
    <br>

    <label for="yapePayment">S/. YAPE:</label>
    <input type="text" id="yapePayment" name="yapePayment">
    <br>

    <label for="clientName">Cliente:</label>
    <select id="clientName" name="clientName" required>
        <c:forEach var="cliente" items="${clientes}">

            <c:choose>
                <c:when test="${cliente.nombre eq reserva.cliente.nombre}">
                    <option value="<c:out value='${cliente.nombre}'/> <c:out value='${cliente.apellido}'/>" selected><c:out value="${reserva.cliente.nombre}"/> <c:out value="${reserva.cliente.apellido}"/></option>
                </c:when>
                <c:otherwise>
                    <option value="<c:out value='${cliente.nombre}'/> <c:out value='${cliente.apellido}'/>"><c:out value="${cliente.nombre}"/> <c:out value="${cliente.apellido}"/></option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
    <br>

    <label for="numberOfGuests">Nro Huespedes:</label>
    <input type="number" id="numberOfGuests" name="numberOfGuests" required value="${reserva.numeroHuespedes}">
    <br>

    <label for="reservationStatus">Estado:</label>
    <select id="reservationStatus" name="reservationStatus" required>
        <c:forEach var="estado" items="${estados}">
            <c:choose>
                <c:when test="${estado eq reserva.estadoReserva}">
                    <option value="${estado}" selected>${reserva.estadoReserva}</option>
                </c:when>
                <c:otherwise>
                    <option value="${estado}">${estado}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
    <br>
    <div class="buttons-options">
        <input class="btn btn-primary" type="submit" value="Actualizar">
    </div>
    <div class="col-12">
        <a href="${pageContext.request.contextPath}/ServletControladorReserva" class="btn btn-danger" style="width: 100%; margin-top: 10px">
            <span class="icon text-white-50">
                <i class="fas fa-trash"></i>
            </span>
            <span class="text">Cancelar</span>
        </a>
    </div>
</form>
<%@include file="components/footer.jsp"%>
