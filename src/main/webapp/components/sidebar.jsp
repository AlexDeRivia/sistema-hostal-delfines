
<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- Sidebar - Brand -->
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.jsp">
            <div class="sidebar-brand-icon rotate-n-15">
                <i class="fas fa-solid fa-hotel"></i>
            </div>
            <div class="sidebar-brand-text mx-3">Hostal los Delfines</div>
        </a>

        <!-- Divider -->
        <hr class="sidebar-divider my-0">

        <!-- Nav Item - Dashboard -->
        <li class="nav-item active">
            <a class="nav-link" href="${pageContext.request.contextPath}">
                <i class="fas fa-fw fa-home"></i>
                <span>Inicio</span></a>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">
            Opciones
        </div>

        <% if (admin != null) { %>
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse0"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-user-tie"></i>
                <span>Empleados</span>
            </a>
            <div id="collapse0" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <a class="collapse-item" href="${pageContext.request.contextPath}/SvEmpleado">Ver empleados</a>
                    <a class="collapse-item" href="AddEmpleado.jsp">Agregar empleado</a>
                </div>
            </div>
        </li>
        <%}%>

        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse1"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-user"></i>
                <span>Clientes</span>
            </a>
            <div id="collapse1" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <a class="collapse-item" href="${pageContext.request.contextPath}/SvCliente">Ver clientes</a>
                    <a class="collapse-item" href="agregarCliente.jsp">Agregar cliente</a>
                </div>
            </div>
        </li>

        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse2"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-box"></i>
                <span>Productos</span>
            </a>
            <div id="collapse2" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <a class="collapse-item" href="${pageContext.request.contextPath}/SvProducto">Ver productos</a>
                    <a class="collapse-item" href="AddProducto.jsp">Agregar producto</a>
                </div>
            </div>
        </li>

        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse3"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-book"></i>
                <span>Reservas</span>
            </a>
            <div id="collapse3" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <a class="collapse-item" href="${pageContext.request.contextPath}/ServletControladorReserva">Ver Reserva</a>
                    <a class="collapse-item" href="${pageContext.request.contextPath}/SvAddReserva">Agregar Reserva</a>
                </div>
            </div>
        </li>

        <% if (admin != null) { %>
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse4"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-bed"></i>
                <span>Habitaciones</span>
            </a>
            <div id="collapse4" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <a class="collapse-item" href="${pageContext.request.contextPath}/SvHabitacion">Ver habitaciones</a>
                    <a class="collapse-item" href="agregarHabitacion.jsp">Agregar habitaciones</a>
                </div>
            </div>
        </li>
        <%}%>

        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse5"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-store"></i>
                <span>Compras</span>
            </a>
            <div id="collapse5" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <a class="collapse-item" href="${pageContext.request.contextPath}/SvCompra">Agregar Compra</a>
                </div>
            </div>
        </li>

        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse6"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-cash-register"></i>
                <span>Ventas</span>
            </a>
            <div id="collapse6" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <a class="collapse-item" href="${pageContext.request.contextPath}/SvVenta">Agregar Venta</a>
                </div>
            </div>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider d-none d-md-block">

        <!-- Sidebar Toggler (Sidebar) -->
        <div class="text-center d-none d-md-inline">
            <button class="rounded-circle border-0" id="sidebarToggle"></button>
        </div>

    </ul>
    <!-- End of Sidebar -->