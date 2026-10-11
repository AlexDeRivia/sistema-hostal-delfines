<%@page import="dominio.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/header.jsp"%>
<%@include file="components/bodyprimeraparte.jsp"%>

<%
    // Obtener el cliente a editar de la sesión
    Cliente cliente = (Cliente) request.getSession().getAttribute("clienteEditar");
%>

<form class="user" action="SvEditCliente" method="POST">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
    <div class="form-group col">
        <div class="col-sm-6 mb-3">
            <h1>Editar Clientes</h1>
            <p>Modulo para editar clientes del sistema</p>
            <input type="text" class="form-control form-control-user" id="dni" name="dni"
                placeholder="DNI" value="<%=web.HtmlEscape.escape(cliente.getDni())%>" required>
            <div class="invalid-feedback">Este campo es obligatorio</div>
        </div>
        <div class="col-sm-6 mb-3">
            <input type="text" class="form-control form-control-user" id="nombre" name="nombre"
                placeholder="Nombres" value="<%=web.HtmlEscape.escape(cliente.getNombre())%>" required>
            <div class="invalid-feedback">Este campo es obligatorio</div>
        </div>
        <div class="col-sm-6 mb-3">
            <input type="text" class="form-control form-control-user" id="apellido" name="apellido"
                placeholder="Apellidos" value="<%=web.HtmlEscape.escape(cliente.getApellido())%>" required>
            <div class="invalid-feedback">Este campo es obligatorio</div>
        </div>
    </div>     
    <button class="btn btn-primary btn-user btn-block" type="submit">
        Guardar Cambios
    </button>
</form>

                
<%@include file="components/bodyfinal.jsp"%>
