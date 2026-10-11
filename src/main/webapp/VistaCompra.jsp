<%@include file="components/auth.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dominio.Compra" %>
<%
    Compra compra = (Compra) session.getAttribute("compra");
%>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>


<link rel="stylesheet" href="dist/css/stylemcompra.css">


<div id="module-container">
    <div id="header">
        <h1 class="header-h1 text-white">MODULO DE COMPRA</h1>
        <div class="contain-header-div">
            <div class = "header-div">
                <h6 class="header-div__h6 text-white">TURNO:</h6>
                <input type="type" name="turno" readonly="true" value="${rcompra.turno}">
            </div>
            <div class = "header-div">
                <h6 class="header-div__h6 text-white">FECHA:</h6>
                <input type="type" name="fecha" readonly="true" value="${rcompra.fecha}">
            </div>
        </div>
    </div>
</div>
<div class="container contain-grids">
    <div class="contain-grids__solicitud">
        <div class="contain-grids__title">
            <h5>SOLICITUD DEL PRODUCTO </h5>
        </div>
        <div class="contain-grids__use">
            <form action="${pageContext.request.contextPath}/SvCompra?accion=agregar" method="post" id="clearReserva">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                <div id="proveedores-container">
                    <label for="proveedor_name">Proveedor:</label>
                    <select class="form-select form-control" aria-label="Default select example" id="proveedor_name" name="proveedor_name">
                        <option selected>Selecciona un proveedor...</option>
                        <c:forEach var="proveedor" items="${proveedores}">
                            <option value="<c:out value='${proveedor.razonSocial}'/>"><c:out value="${proveedor.razonSocial}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div id="productos-container">
                    <label for="producto_name">Producto:</label>
                    <select class="form-select form-control" aria-label="Default select example" name="producto_name">
                        <option selected>Selecciona un producto...</option>
                        <c:forEach var="producto" items="${productos}">
                            <option value="<c:out value='${producto.nombre}'/>"><c:out value="${producto.nombre}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="input-group flex-nowrap">
                    <span class="input-group-text" id="addon-wrapping">Cantidad</span>
                    <input type="text" class="form-control" placeholder="Ejemplo: 5" aria-label="Username" aria-describedby="addon-wrapping" name="cantidad">
                </div>

                <div class="buttons-options">
                    <input class="btn btn-primary" type="submit" value="Agregar">
                </div>
            </form>
        </div>
    </div>
    <div class="contain-grids__tabla">
            <div class="contain-grids__title">
                <h5>LISTA DE COMPRA</h5>
            </div>
            <div class="contain-grids__use table-responsive">
                <table class="styled-table table-use">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Producto</th>
                            <th>P.Compra</th>
                            <th>Cantidad</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="lc" items="${compra.lineaCompras}" varStatus="status">
                            <tr>
                                <td>${compra.id}</td> 
                                <td><c:out value="${lc.producto.nombre}"/></td>
                                <td>${lc.producto.precioCompra}</td>
                                <td>${lc.cantidad}</td>
                                <td>
                                    <a class="btn btn-success" href="${pageContext.request.contextPath}/SvCompra?accion=actualizar&idLineaCompra=${status.index}">Modificar</a>
                                    <form action="${pageContext.request.contextPath}/SvCompra?accion=eliminar" method="post" class="d-inline">
                                        <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                        <input type="hidden" name="idLineaCompra" value="${status.index}">
                                        <button type="submit" class="btn btn-danger">Eliminar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row form-buttons">
                <div class="col-2"></div>
                <div class="col-4">
                    <form action="${pageContext.request.contextPath}/SvCompra?accion=guardar" method="post">
                        <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                        <button class="btn btn-primary" type="submit" style="width: 100%;">Enviar</button>
                    </form>
                </div>

                <div class="col-4">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/" style="width: 100%;">Regresar</a>
                </div>
                <div class="col-2"></div>
            </div>
    </div>
</div>

<%@include file="components/footer.jsp"%>
