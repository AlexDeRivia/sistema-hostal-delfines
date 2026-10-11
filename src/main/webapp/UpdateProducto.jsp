<%@include file="components/auth.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<link rel="stylesheet" type="text/css" href="dist/css/styles.css"/>
<script type="text/javascript" src="dist/js/funtions.js"/></script>

<form class="miform" name="formReserva" action="${pageContext.request.contextPath}/SvUpdateProducto?accion=update" method="post" id="clearReserva">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
    <h1>ACTUALIZAR PRODUCTO</h1>

    <label for="producto_id">ID:</label>
    <input type="text" id="producto_id" name="producto_id" required value="${producto.id}">
    <br>

    <label for="producto_name">Producto:</label>
    <input type="text" id="producto_name" name="producto_name" value ="<c:out value='${producto.nombre}'/>">
    <br>

    <label for="pventa">Precio Venta:</label>
    <input type="text" id="pventa" name="pventa" value ="${producto.precioVenta}">
    <br>

    <label for="pcompra">Precio Compra:</label>
    <input type="text" id="pcompra" name="pcompra" value ="${producto.precioCompra}">
    <br>

    <label for="categoriaStatus">Categoria:</label>
    <select id="categoriaStatus" name="categoriaStatus" required>
        <c:forEach var="categoria" items="${categorias}">
            <c:choose>
                <c:when test="${categoria eq producto.categoria}">
                    <option value="${categoria}" selected>${producto.categoria}</option>
                </c:when>
                <c:otherwise>
                    <option value="${categoria}">${categoria}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
    <br>

    <input type="submit" value="Actualizar">
    <button type="button" class="cancel" onclick="cancelUpdate()">Cancelar</button>
</form>
<%@include file="components/footer.jsp"%>
