<%@page contentType="text/html" pageEncoding="UTF-8"%>

<body id="page-top">

    <!-- Page Wrapper -->
    <div id="wrapper">

    
        <!-- Sidebar -->
        <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
 
            <!-- Sidebar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="">
                <div class="sidebar-brand-icon rotate-n-15">
                    <i class="fas fa-solid fa-hotel"></i>
                </div>
                <div class="sidebar-brand-text mx-3">Hostal Los Delfines </div>
            </a>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Nav Item - Dashboard -->
            <li class="nav-item active">
                <a class="nav-link" href="${pageContext.request.contextPath}">
                    <i class="fas fa-solid fa-bars"></i>
                    <span>Menu</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Gestion
            </div>

            <!-- PARTES DEL MENU -->
             <!-- CLIENTES -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo"
                    aria-expanded="true" aria-controls="collapseTwo">
                    <i class="fas fa-solid fa-user"></i>
                    <span>Clientes</span>
                </a>
                <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Acciones:</h6>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/SvCliente">Ver clientes</a>
                         <a class="collapse-item" href="agregarCliente.jsp">Agregar clientes</a>     
                    </div>
                </div>
            </li>

            <!-- HABITACIONES -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUtilities"
                    aria-expanded="true" aria-controls="collapseUtilities">
                    <i class="fas fa-solid fa-bed"></i>
                    <span>Habitaciones</span>
                </a>
                <div id="collapseUtilities" class="collapse" aria-labelledby="headingUtilities"
                    data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Acciones:</h6>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/SvHabitacion">Ver habitaciones</a>
                        <a class="collapse-item" href="agregarHabitacion.jsp">Agregar habitaciones</a>
                        
                    </div>
                </div>
            </li>
            
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseCompra"
                    aria-expanded="true" aria-controls="collapseUtilities">
                    <i class="fas fa-solid fa-bed"></i>
                    <span>Compras</span>
                </a>
                <div id="collapseCompra" class="collapse" aria-labelledby="headingUtilities"
                    data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Acciones:</h6>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/SvCompra">Agregar Compra</a>
                        
                    </div>
                </div>
            </li>
            
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseVenta"
                    aria-expanded="true" aria-controls="collapseUtilities">
                    <i class="fas fa-solid fa-bed"></i>
                    <span>Ventas</span>
                </a>
                <div id="collapseVenta" class="collapse" aria-labelledby="headingUtilities"
                    data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Acciones:</h6>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/SvVenta">Agregar Venta</a>
                        
                    </div>
                </div>
            </li>
            
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseReserva"
                    aria-expanded="true" aria-controls="collapseUtilities">
                    <i class="fas fa-solid fa-bed"></i>
                    <span>Reservas</span>
                </a>
                <div id="collapseReserva" class="collapse" aria-labelledby="headingUtilities"
                    data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Acciones:</h6>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/ServletControladorReserva">Ver Reserva</a>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/SvAddReserva">Agregar Reserva</a>
                    </div>
                </div>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">       
        </ul>
        <!-- End of Sidebar -->

        
       
        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column">

            <!-- Main Content -->
            <div id="content">

                <!-- Topbar -->
                <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                    <!-- Sidebar Toggle (Topbar) -->
                    <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                        <i class="fa fa-bars"></i>
                    </button>

                    <!-- Topbar Navbar -->
                    <ul class="navbar-nav ml-auto">

                        <!-- Nav Item - Search Dropdown (Visible Only XS) -->
                        <li class="nav-item dropdown no-arrow d-sm-none">
                            <a class="nav-link dropdown-toggle" href="#" id="searchDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-search fa-fw"></i>
                            </a>
                            <!-- Dropdown - Messages -->
                            <div class="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in"
                                aria-labelledby="searchDropdown">
                                <form class="form-inline mr-auto w-100 navbar-search">
                                    <div class="input-group">
                                        <input type="text" class="form-control bg-light border-0 small"
                                            placeholder="Search for..." aria-label="Search"
                                            aria-describedby="basic-addon2">
                                        <div class="input-group-append">
                                            <button class="btn btn-primary" type="button">
                                                <i class="fas fa-search fa-sm"></i>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </li>      
                    </ul>
                </nav>
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="contanier-fluid">
                
            <!--Page Heading -->
            <div class="d-sm-flex align-items-center justify-content-between mb-4">
                <hi class="h3 mb-0 text-gray-800">Hostal Los Delfines</hi>     
            </div>
