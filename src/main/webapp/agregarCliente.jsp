<%@include file="components/auth.jsp"%>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<div class="card">
    <div class="card-body">
        <form class="user" action="${pageContext.request.contextPath}/SvCliente" method="POST">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
            <div class="form-group col">
                <div class="col-sm-6 mb-3"> 
                    <h1>Agregar Clientes</h1>
                    <p>Modulo para agregar clientes al sistema</p>
                    <input type="text" class="form-control form-control-user" id="dni" name="dni"
                           placeholder="DNI" required>
                    <div class="invalid-feedback">Este campo es obligatorio</div>
                </div>
                <div class="col-sm-6 mb-3">
                    <input type="text" class="form-control form-control-user" id="nombre" name="nombre"
                           placeholder="Nombres" required>
                    <div class="invalid-feedback">Este campo es obligatorio</div>
                </div>
                <div class="col-sm-6 mb-3">
                    <input type="text" class="form-control form-control-user" id="apellido" name="apellido"
                           placeholder="Apellidos" required>
                    <div class="invalid-feedback">Este campo es obligatorio</div>
                </div>
            </div>
            <button class="btn btn-primary btn-user btn-block" type="submit">
                Agregar Cliente
            </button>
        </form>
    </div>
</div>

<%@include file="components/footer.jsp"%>
