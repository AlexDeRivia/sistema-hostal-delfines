<%@include file="components/auth.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<div class="card">
    <div class="card-body">
        <form name="formProducto" action="${pageContext.request.contextPath}/SvAddProducto?accion=add" method="post" id="clearProducto">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
            <h1>AGREGAR PRODUCTO</h1>

            <label for="producto">Producto:</label>
            <input type="text" id="producto" name="producto" class="form-control" required>
            <br>

            <label for="pventa">Precio Venta:</label>
            <input type="text" id="pventa" name="pventa" class="form-control" required>
            <br>

            <label for="pcompra">Precio Compra:</label>
            <input type="text" id="pcompra" name="pcompra" class="form-control" required>
            <br>

            <label for="categoria">Categoria:</label>
            <select id="categoria" name="categoria" class="form-control" required>
                <option value="ALIMENTOS">ALIMENTOS</option>
                <option value="USO_PERSONAL">USO_PERSONAL</option>
            </select>
            <br>

            <input class="btn btn-primary" type="submit" value="Enviar">
        </form>
    </div>
</div>

<%@include file="components/footer.jsp"%>
