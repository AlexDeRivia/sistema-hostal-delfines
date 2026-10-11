<%@include file="components/auth.jsp"%>

<%@taglib prefix="e" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<script type="text/javascript" src="dist/js/funtions.js"/></script>

<div id="module-container">
    <div id="header">
        <h1 class="header-h1">MODULO EMPLEADOS</h1>
    </div>
    <div id="wrapper">
        <div id="content-wrapper" class="d-flex flex-column">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Formulario de Empleados</h6>
                </div>

                <div class="card-body">
                    <a href="${pageContext.request.contextPath}/SvAddEmpleado" class="btn btn-primary btn-icon-split">
                        <span class="icon text-white-50">
                            <i class="fas fa-flag"></i>
                        </span>
                        <span class="text">Nuevo</span>
                    </a>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>DNI</th>
                                    <th>Nombre</th>
                                    <th>Apellido</th>
                                    <th>Usuario</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <e:forEach var="empleado" items="${empleados}">
                                    <tr>
                                        <td>${empleado.id}</td> 
                                        <th><e:out value="${empleado.dni}"/></th>
                                        <td><e:out value="${empleado.nombre}"/></td>
                                        <td><e:out value="${empleado.apellido}"/></td>
                                        <td><e:out value="${empleado.username}"/></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/SvUpdateEmpleado?accion=actualizar&idEmpleado=${empleado.id}" class="btn btn-secondary btn-icon-split">
                                                <span class="icon text-white-50">
                                                    <i class="fas fa-arrow-right"></i>
                                                </span>
                                                <span class="text">Actualizar</span>
                                            </a>
                                            <form action="${pageContext.request.contextPath}/SvDeleteEmpleado?accion=eliminar" method="post" class="d-inline">
                                                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                                <input type="hidden" name="idEmpleado" value="${empleado.id}">
                                                <button type="submit" class="btn btn-danger btn-icon-split">
                                                <span class="icon text-white-50">
                                                    <i class="fas fa-trash"></i>
                                                </span>
                                                <span class="text">Eliminar</span>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </e:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card-body">
                    <div class="my-2"></div>
                    <a href="index.jsp" class="btn btn-success btn-icon-split">
                        <span class="icon text-white-50">
                            <i class="fas fa-check"></i>
                        </span>
                        <span class="text">Regresar</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="components/footer.jsp"%>
