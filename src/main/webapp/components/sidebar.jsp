<div class="app-shell" id="appShell">
    <aside class="app-sidebar" id="appSidebar" aria-label="Navegación principal">
        <a class="app-brand" href="${pageContext.request.contextPath}/index.jsp" aria-label="Hostal Delfines, ir al inicio">
            <span class="brand-monogram" aria-hidden="true">HD</span>
            <span class="brand-copy"><strong>Hostal Delfines</strong><small>Sistema de gestión</small></span>
        </a>
        <div class="sidebar-rule" aria-hidden="true"></div>
        <nav class="app-nav">
            <span class="app-nav-label">GENERAL</span>
            <a class="app-nav-link" data-nav="inicio" href="${pageContext.request.contextPath}/index.jsp"><i class="fas fa-compass" aria-hidden="true"></i><span>Inicio</span></a>
            <span class="app-nav-label">RECEPCIÓN</span>
            <a class="app-nav-link" data-nav="reservas" href="${pageContext.request.contextPath}/ServletControladorReserva"><i class="fas fa-calendar-check" aria-hidden="true"></i><span>Reservas</span></a>
            <% if (admin != null) { %>
            <a class="app-nav-link" data-nav="habitaciones" href="${pageContext.request.contextPath}/SvHabitacion"><i class="fas fa-door-open" aria-hidden="true"></i><span>Habitaciones</span></a>
            <% } %>
            <a class="app-nav-link" data-nav="clientes" href="${pageContext.request.contextPath}/SvCliente"><i class="fas fa-address-book" aria-hidden="true"></i><span>Clientes</span></a>
            <span class="app-nav-label">COMERCIAL</span>
            <a class="app-nav-link" data-nav="ventas" href="${pageContext.request.contextPath}/SvVenta"><i class="fas fa-receipt" aria-hidden="true"></i><span>Ventas</span></a>
            <a class="app-nav-link" data-nav="compras" href="${pageContext.request.contextPath}/SvCompra"><i class="fas fa-box-open" aria-hidden="true"></i><span>Compras</span></a>
            <a class="app-nav-link" data-nav="productos" href="${pageContext.request.contextPath}/SvProducto"><i class="fas fa-boxes" aria-hidden="true"></i><span>Productos</span></a>
            <a class="app-nav-link" data-nav="proveedores" href="${pageContext.request.contextPath}/SvProveedor"><i class="fas fa-truck" aria-hidden="true"></i><span>Proveedores</span></a>
            <a class="app-nav-link" data-nav="movimientos" href="${pageContext.request.contextPath}/SvMovimientos"><i class="fas fa-history" aria-hidden="true"></i><span>Movimientos</span></a>
            <% if (admin != null) { %>
            <span class="app-nav-label">ADMINISTRACIÓN</span>
            <a class="app-nav-link" data-nav="empleados" href="${pageContext.request.contextPath}/SvEmpleado"><i class="fas fa-users-cog" aria-hidden="true"></i><span>Empleados</span></a>
            <% } %>
        </nav>
        <div class="sidebar-foot"><span class="sidebar-foot-mark" aria-hidden="true"></span>Operación junto al mar</div>
    </aside>
    <button class="sidebar-backdrop" type="button" aria-label="Cerrar menú" data-close-sidebar></button>
