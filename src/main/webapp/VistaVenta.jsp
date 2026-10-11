<%@include file="components/auth.jsp"%>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dominio.Venta" %>
<%@ page import="dominio.LineaVenta" %>
<%
    Venta venta = (Venta) session.getAttribute("venta");
    double total = 0; // Inicializamos el total en 0
    Object totalAttribute = session.getAttribute("total"); // Obtenemos el atributo total de la sesi�n
    
    // Verificamos si el atributo total no es nulo y es un String
    if (totalAttribute != null && totalAttribute instanceof String) {
        try {
            // Intentamos convertir el atributo total a double
            total = Double.parseDouble((String) totalAttribute);
        } catch (NumberFormatException e) {
            // Manejo de errores en caso de que no se pueda convertir a double
            e.printStackTrace();
            // Aqu� puedes manejar el error de conversi�n como consideres apropiado
        }
    }
%>
<!DOCTYPE html>
<html>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MODULO VENTAS</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link rel="stylesheet" href="dist/css/stylemcompra.css">
    </head>
    <body>
        <div id="module-container">
            <div id="header">
                <h1 class="header-h1">MODULO DE VENTA</h1>
                <div class="contain-header-div">
                    <div class = "header-div">
                        <h6 class="header-div__h6">TURNO:</h6>
                        <input type="type" name="turno" readonly="true" value="${rventa.turno}">
                    </div>
                    <div class = "header-div">
                        <h6 class="header-div__h6">FECHA:</h6>
                        <input type="type" name="fecha" readonly="true" value="${rventa.fecha}">
                    </div>
                </div>
            </div>
        </div>
        <div class="container contain-grids">
            <div class="contain-grids__solicitud">
                <div class="contain-grids__title">
                    <h5>Realizar Venta </h5>
                </div>
                <div class="contain-grids__use">
                    <form action="${pageContext.request.contextPath}/SvVenta?accion=agregar" method="post" id="clearReserva">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                        <div id="proveedores-container">
                            <label for="cliente_name">Cliente:</label>
                            <select class="form-select" aria-label="Default select example" id="cliente_name" name="cliente_name">
                                <option selected>Selecciona un Cliente...</option>
                                <c:forEach var="cliente" items="${clientes}">
                                    
                                    <option value="<c:out value='${cliente.nombre}'/> <c:out value='${cliente.apellido}'/>"><c:out value="${cliente.nombre}"/> <c:out value="${cliente.apellido}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div id="productos-container">
                            <label for="producto_name">Producto:</label>
                            <select class="form-select" aria-label="Default select example" name="producto_name">
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
                        <h5>LISTA DE VENTA</h5>
                        
                    </div>
                    <div class="contain-grids__use table-responsive">
                        <table class="styled-table table-use">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <!--<th>Cliente</th>-->
                                    <th>Producto</th>
                                    <th>Cantidad</th>
                                    <th>P.Venta</th>
                                    <th>SubTotal</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="lv" items="${venta.lineaVentas}" varStatus="status">
                                    <tr>
                                        <td>${venta.id}</td>
                                        <!--<td>${venta.cliente.nombre} ${venta.cliente.apellido}</td>-->
                                        <td><c:out value="${lv.producto.nombre}"/></td>
                                        <td>${lv.cantidad}</td>
                                        <td>${lv.precioUnitario}</td>
                                        <td>${lv.cantidad * lv.precioUnitario}</td>
                                        <td>
                                            <a class="btn btn-success" href="${pageContext.request.contextPath}/SvVenta?accion=actualizar&idLineaVenta=${status.index}">Modificar</a>
                                            <form action="${pageContext.request.contextPath}/SvVenta?accion=eliminar" method="post" class="d-inline">
                                                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                                <input type="hidden" name="idLineaVenta" value="${status.index}">
                                                <button type="submit" class="btn btn-danger">Eliminar</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class = "header-div">
                            <h6 class="header-div__h6">TOTAL:</h6>
                            <input type="type" name="turno" readonly="true" value="${total}">
                        </div>
                    </div>
                    <div class="row form-buttons">
                        <div class="col-2"></div>
                        <div class="col-4">
                            <form action="${pageContext.request.contextPath}/SvVenta?accion=guardar" method="post">
                                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                <button class="btn btn-primary" type="submit" style="width: 100%;">Enviar</button>
                            </form>
                        </div>
                        
                        <div class="col-4">
                            <a class="btn btn-primary" href="index.jsp" style="width: 100%;">Regresar</a>
                        </div>
                        <div class="col-2"></div>
                    </div>
                    
            </div>
        </div>
        
    </body>
</html>

<%@include file="components/footer.jsp"%>
