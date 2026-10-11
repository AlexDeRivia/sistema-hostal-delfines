<%@include file="components/auth.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<link rel="stylesheet" type="text/css" href="dist/css/styles.css"/>
<script type="text/javascript" src="dist/js/funtions.js"/></script>

<div id="module-container">
    <div id="header">
        <h1 class="header-h1">MODULO PRODUCTO</h1>
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-primary shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                Fecha</div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${fecha}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-calendar fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-info shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-info text-uppercase mb-1">Turno
                            </div>
                            <div class="row no-gutters align-items-center">
                                <div class="col-auto">
                                    <div class="h5 mb-0 mr-3 font-weight-bold text-gray-800">${turno}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="wrapper">
        <div id="content-wrapper" class="d-flex flex-column">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Formulario de Productos</h6>
                </div>

                <div class="card-body">
                    <a href="${pageContext.request.contextPath}/SvAddProducto" class="btn btn-primary btn-icon-split">
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
                                    <th>Nombre</th>
                                    <th>P.Compra</th>
                                    <th>P.Venta</th>
                                    <th>Stock</th>
                                    <th>Categoría</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="producto" items="${productos}">
                                    <tr>
                                        <td>${producto.id}</td> 
                                        <td><c:out value="${producto.nombre}"/></td>
                                        <td>${producto.precioCompra}</td>
                                        <td>${producto.precioVenta}</td>
                                        <td>${producto.stock}</td>
                                        <td>${producto.categoria}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/SvUpdateProducto?accion=actualizar&idProducto=${producto.id}" class="btn btn-secondary btn-icon-split">
                                                <span class="icon text-white-50">
                                                    <i class="fas fa-arrow-right"></i>
                                                </span>
                                                <span class="text">Actualizar</span>
                                            </a>
                                            <form action="${pageContext.request.contextPath}/SvDeleteProducto?accion=eliminar" method="post" class="d-inline">
                                                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                                <input type="hidden" name="idProducto" value="${producto.id}">
                                                <button type="submit" class="btn btn-danger btn-icon-split">
                                                <span class="icon text-white-50">
                                                    <i class="fas fa-trash"></i>
                                                </span>
                                                <span class="text">Eliminar</span>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card-body">
                    <div class="my-2"></div>
                    <a href="#" class="btn btn-success btn-icon-split">
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
