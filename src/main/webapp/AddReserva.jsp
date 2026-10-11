<%@include file="components/auth.jsp"%>


<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="jakarta.servlet.http.*" %>
<%@ page import="dominio.RegistroReserva" %>
<% HttpSession sessionr = request.getSession(); %>
<% RegistroReserva srreserva = (RegistroReserva) sessionr.getAttribute("srreserva");%>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<script type="text/javascript" src="dist/js/funtions.js"/></script>
<link rel="stylesheet" href="dist/css/stylemcompra.css">
<link rel="stylesheet" href="dist/css/styles.css">

<form class="miform" name="formReserva" action="${pageContext.request.contextPath}/SvAddReserva?accion=add" method="post" id="clearReserva">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
    <h1>AGREGAR RESERVA</h1>

    <label for="roomNumber">N� Habitaci�n:</label>
    <select id="roomNumber" name="roomNumber" required>
        <c:forEach var="habitacion" items="${habitaciones}">
            <option value="${habitacion.numero}" data-ventana="${habitacion.ventanaAfuera}" data-camas="${habitacion.numeroCamas}">
                ${habitacion.numero}
            </option>
        </c:forEach>
    </select>
    <br>

    <label for="checkInTime">Hora de Ingreso:</label>
    <input type="text" id="checkInTime" name="checkInTime" pattern="([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]" required>
    <br>

    <label for="reservedHours">Horas Reservadas:</label>
    <input type="number" id="reservedHours" name="reservedHours" required>
    <br>

    <label for="calculateCost">Calcular Costo:</label>
    <button type="button" onclick="calcularCosto()" style="margin-bottom: 10px">
        Calcular
    </button>
    <input type="text" id="costResult" name="costResult" class="form-control" readonly>
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
            <option value="<c:out value='${cliente.nombre}'/> <c:out value='${cliente.apellido}'/>"><c:out value="${cliente.nombre}"/> <c:out value="${cliente.apellido}"/></option>
        </c:forEach>
    </select>
    <br>

    <label for="numberOfGuests">Nro Huespedes:</label>
    <input type="number" id="numberOfGuests" name="numberOfGuests" required>
    <br>

    <div class="buttons-options">
        <input class="btn btn-primary" type="submit" value="Enviar">
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



<script>
    var contextPath = '${pageContext.request.contextPath}';
</script>

<%@include file="components/footer.jsp"%>

