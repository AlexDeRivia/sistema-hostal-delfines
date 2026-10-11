<%@include file="components/auth.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<link rel="stylesheet" type="text/css" href="dist/css/styles.css"/>
<script type="text/javascript" src="dist/js/funtions.js"/></script>

<form class="miform" name="formReserva" action="${pageContext.request.contextPath}/SvVenta?accion=update" method="post" id="clearReserva">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
    <h1>ACTUALIZAR LINEA DE COMPRA</h1>

    <label for="posicion">ID:</label>
    <input type="text" id="posicion" name="posicion" required value="${posicion}">
    <br>

    <label for="producto_name">Producto:</label>
    <input type="text" id="producto_name" name="producto_name" value ="<c:out value='${lineaVenta.producto.nombre}'/>" readonly="true">
    <br>

    <label for="pcompra">Precio Venta:</label>
    <input type="text" id="pcompra" name="pcompra" value ="${lineaVenta.precioUnitario}" readonly="true">
    <br>

    <div class="input-group flex-nowrap">
        <span class="input-group-text" id="addon-wrapping">Cantidad</span>
        <input type="text" class="form-control" placeholder="Ejemplo: 5" aria-label="Username" aria-describedby="addon-wrapping" name="cantidad" value ="${lineaVenta.cantidad}" >
    </div>

    <input type="submit" value="Actualizar">
    <button type="button" class="cancel" onclick="cancelUpdate()">Cancelar</button>
</form>
<%@include file="components/footer.jsp"%>
