<%@include file="components/auth.jsp"%>
<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dominio.Reserva" %>
<%@ page import="jakarta.servlet.http.*" %>
<%@ page import="dominio.RegistroReserva" %>
<% HttpSession sessionr = request.getSession(); %>
<% RegistroReserva srreserva = (RegistroReserva) sessionr.getAttribute("srreserva");%>
<% Reserva reserva = (Reserva) session.getAttribute("reserva"); %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MODULO RESERVAS</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link rel="stylesheet" href="dist/css/stylemcompra.css">

    </head>
    <body>
        <div id="module-container">
            <div id="header">
                <h1 class="header-h1">MODULO DE RESERVA</h1>
                <div class="contain-header-div">
                    <div class = "header-div">
                        <h6 class="header-div__h6">TURNO:</h6>
                        <input type="type" name="turno" readonly="true" value="${rreserva.turno}">
                    </div>
                    <div class = "header-div">
                        <h6 class="header-div__h6">FECHA:</h6>
                        <input type="type" name="fecha" readonly="true" value="${rreserva.fecha}">
                    </div>
                </div>
            </div>
        </div>
        <div class="row form-buttons" style="padding-bottom: 30px">
            <div class="col-1"></div>
            <div class="col-3">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/SvAddReserva" style="width: 100%;">Nuevo</a>
            </div>
             <div class="col-4"></div>
            <div class="col-3">
                <a class="btn btn-secondary" href="index.jsp" style="width: 100%;">Regresar</a>
            </div>
        </div>

        <div class="container " style="width: 100%">
            <div class="contain-grids__use table-responsive">
                <div class="contain-grids__title">
                    <h5>LISTA DE RESERVA</h5>
                </div>
                <table class="styled-table table-use">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Cliente</th>
                            <th>N° Habitación</th>
                            <th>Hora Ingreso</th>
                            <th>Hora Salida</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="reserva" items="${reservas}">
                            <tr>
                                <td>${reserva.id}</td> 
                                <td><c:out value="${reserva.cliente.nombre}"/> <c:out value="${reserva.cliente.apellido}"/></td>
                                <td>${reserva.habitacion.numero}</td>
                                <td><c:out value="${reserva.hora_ingreso}"/></td>
                                <td><c:out value="${reserva.hora_salida}"/></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/SvUpdateReserva?accion=actualizar&idReserva=${reserva.id}" class="btn btn-secondary btn-icon-split">
                                        <span class="icon text-white-50">
                                            <i class="fas fa-arrow-right"></i>
                                        </span>
                                        <span class="text">Actualizar</span>
                                    </a>
                                    <form action="${pageContext.request.contextPath}/SvDeleteReserva?accion=eliminar" method="post" class="d-inline">
                                        <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                        <input type="hidden" name="idReserva" value="${reserva.id}">
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

    </body>
</html>
<%@include file="components/footer.jsp"%>
