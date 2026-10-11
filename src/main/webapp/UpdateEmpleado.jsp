<%@include file="components/auth.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<link rel="stylesheet" type="text/css" href="dist/css/styles.css"/>
<script type="text/javascript" src="dist/js/funtions.js"/></script>

<form class="miform" name="formReserva" action="${pageContext.request.contextPath}/SvUpdateEmpleado?accion=update" method="post" id="clearReserva">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
    <h1>ACTUALIZAR EMPLEADO</h1>

    <label for="empleado_name">ID</label>
    <input type="text" id="idE" name="idE" value="${empleado.id}" readonly>
    <br>
    <label for="producto_name">DNI</label>
    <input type="text" id="dniE" name="dniE" value ="<c:out value='${empleado.dni}'/>">
    <br>

    <label for="producto_name">Nombre</label>
    <input type="text" id="nombreE" name="nombreE" value ="<c:out value='${empleado.nombre}'/>">
    <br>

    <label for="pventa">Apellido</label>
    <input type="text" id="apellidoE" name="apellidoE" value ="<c:out value='${empleado.apellido}'/>">
    <br>

    <label for="pcompra">Usuario</label>
    <input type="text" id="usuarioE" name="usuarioE" value ="<c:out value='${empleado.username}'/>">
    <br>

    <label for="categoriaStatus">Clave</label>
    <input type="password" id="claveE" name="claveE" autocomplete="new-password">
    <br>

    <input type="submit" value="Actualizar">
    <button type="button" class="cancel" onclick="cancelUpdate()">Cancelar</button>
</form>

<%@include file="components/footer.jsp"%>
