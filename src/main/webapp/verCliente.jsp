<%@include file="components/auth.jsp"%>

<%@page import="dominio.Cliente"%>
<%@page import="java.util.List"%>

<%@include file="components/head.jsp"%>
<%@include file="components/sidebar.jsp"%>
<%@include file="components/navBar.jsp"%>

<!-- Begin Page Content -->
<div class="container-fluid">
    <h1 class="h3 mb-2 text-gray-800">Registros de Clientes</h1>
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Listado de Clientes</h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>DNI</th>
                            <th>Nombres</th>
                            <th>Apellidos</th>
                            <th style="width:210px">Accion</th>
                        </tr>
                    </thead>                                     
                    <tbody>      
                        <% 
                        List<Cliente> listaClientes = (List<Cliente>) request.getSession().getAttribute("listaClientes");
                        if (listaClientes != null && !listaClientes.isEmpty()) {
                            for (Cliente cliente : listaClientes) {  
                        %>
                        <tr>
                            <td id="id<%=cliente.getId()%>"><%=cliente.getId() %></td>
                            <td><%=web.HtmlEscape.escape(cliente.getDni()) %></td>
                            <td><%=web.HtmlEscape.escape(cliente.getNombre()) %></td>
                            <td><%=web.HtmlEscape.escape(cliente.getApellido()) %></td>
                            <td style="display: flex; width:230px;">
                                <form name="eliminar" action="SvElimCliente" method="POST">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                    <button type = "submit" class="btn btn-primary btn-user btn-block" style="background-color:red; margin-right:20px;">
                                        <i class="fas fa-trash-alt"></i>                                               
                                    </button>
                                    <input type="hidden" name="id" value="<%=cliente.getId()%>"> <!-- manda codigo al servlet-->
                                </form>
                                <form name="editar" action="SvEditCliente" method="GET"> <!-- manda codigo al servlet-->
                                    <button type="submit" class="btn btn-primary btn-user btn-block"; style="margin-left:20px;">
                                        <i class="fas fa-pencil-alt"></i> 
                                    </button>
                                    <input type="hidden" name="id" value="<%=cliente.getId()%>"> <!-- manda codigo al servlet-->
                                </form>                   
                            </td>                                         
                        </tr>                    
                        <% 
                            }
                        } else { 
                        %>
                        <tr>
                            <td colspan="3">No hay clientes registrados.</td>
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
