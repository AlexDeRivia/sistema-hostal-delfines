<%@include file="components/auth.jsp"%>

<%@page import="dominio.Habitacion"%>
<%@page import="java.util.List"%>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<!-- Begin Page Content -->
<div class="container-fluid">
    <h1 class="h3 mb-2 text-gray-800">Registros de Habitaciones</h1>
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Listado de Habitaciones</h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>N� Habitacion</th>
                            <th>N� Camas</th>
                            <th>Disponibilidad</th>
                            <th>Ventana</th>
                            <th style="width:210px">Accion</th>
                        </tr>
                    </thead>                                     
                    <tbody>      
                        <% 
                        List<Habitacion> listaHabitaciones = (List<Habitacion>)request.getSession().getAttribute("listaHabitaciones");
                        if (listaHabitaciones != null && !listaHabitaciones.isEmpty()) {
                            for (Habitacion habitacion : listaHabitaciones) {  
                        %>
                        <tr>
                            <td id="id<%=habitacion.getId()%>"><%=habitacion.getId() %></td>
                            <td><%=habitacion.getNumero() %></td>
                            <td><%=habitacion.getNumeroCamas() %></td>
                            <td><%=habitacion.isDisponible() ? "Disponible":"No disponible"%></td>
                            <td><%=habitacion.isVentanaAfuera()? "Si":"No" %></td>

                            <td style="display: flex; width:230px;">
                                <form name="eliminar" action="SvElimHabitacion" method="POST">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                    <button type = "submit" class="btn btn-primary btn-user btn-block" style="background-color:red; margin-right:20px;">
                                        <i class="fas fa-trash-alt"></i>                                               
                                    </button>
                                    <input type="hidden" name="id" value="<%=habitacion.getId()%>"> <!-- manda codigo al servlet-->
                                </form>
                                <form name="editar" action="SvEditHabitacion" method="GET"> <!-- manda codigo al servlet-->
                                    <button type="submit" class="btn btn-primary btn-user btn-block"; style="margin-left:20px;">
                                        <i class="fas fa-pencil-alt"></i> 
                                    </button>
                                    <input type="hidden" name="id" value="<%=habitacion.getId()%>"> <!-- manda codigo al servlet-->
                                </form>                   
                            </td>                                              
                        </tr>                    
                        <% 
                            }
                        } else { 
                        %>
                        <tr>
                            <td colspan="6">No hay habitaciones registradas.</td>
                        </tr>
                        <% 
                        }
                        %>                        
                    </tbody>      
                </table>
            </div>
        </div>
    </div>
</div>

<%@include file="components/footer.jsp"%>