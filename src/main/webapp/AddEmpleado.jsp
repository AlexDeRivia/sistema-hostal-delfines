<%@include file="components/auth.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<script type="text/javascript" src="dist/js/funtions.js"/></script>

<div class="card">
    <div class="card-body">
        <form name="formProducto" action="${pageContext.request.contextPath}/SvAddEmpleado?accion=add" method="post" id="clearEmpleado">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
            <h1>AGREGAR EMPLEADO</h1>

            <label for="producto" class="form-label">DNI:</label>
            <input type="text" id="dniE" name="dniE" class="form-control" required>
            <br>
            <label for="producto">Nombre:</label>
            <input type="text" id="nombreE" name="nombreE" class="form-control" required>
            <br>

            <label for="pventa">Apellido:</label>
            <input type="text" id="apellidoE" name="apellidoE" class="form-control" required>
            <br>

            <label for="pcompra">Usuario:</label>
            <input type="text" id="usuarioE" name="usuarioE" class="form-control" required>
            <br>      

            <label for="pcompra">Clave:</label>
            <input type="password" id="claveE" name="claveE" class="form-control" autocomplete="new-password" required>
            <br> 
            <input class="btn btn-primary" type="submit" value="Enviar">
            <button class="btn btn-danger" type="button" class="cancel" onclick="cancelUpdate()">Cancelar</button>
        </form>
    </div>
</div>

<%@include file="components/footer.jsp"%>
