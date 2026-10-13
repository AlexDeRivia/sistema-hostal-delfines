    <div class="app-workspace">
        <header class="app-topbar">
            <button class="app-menu-toggle" type="button" aria-controls="appSidebar" aria-expanded="false" aria-label="Abrir menú" data-toggle-sidebar><i class="fas fa-bars" aria-hidden="true"></i></button>
            <div class="topbar-context"><span class="topbar-dot" aria-hidden="true"></span><span>Recepción · Hostal Delfines</span></div>
            <div class="topbar-end">
                <span class="topbar-role"><%=admin != null ? "Administrador" : "Empleado"%></span>
                <strong class="topbar-name"><%=web.HtmlEscape.escape(empleado != null ? empleado.getNombre() + " " + empleado.getApellido() : admin.getNombre() + " " + admin.getApellido())%></strong>
                <form action="${pageContext.request.contextPath}/SvLogout" method="post" class="topbar-logout">
                    <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                    <button type="submit" aria-label="Cerrar sesión" title="Cerrar sesión"><i class="fas fa-sign-out-alt" aria-hidden="true"></i><span>Salir</span></button>
                </form>
            </div>
        </header>
        <main class="app-main" id="main-content" tabindex="-1">
