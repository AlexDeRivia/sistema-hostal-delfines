<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Control de Acceso</title>
        <!-- Custom fonts for this template-->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">

        <!-- Custom styles for this template-->
        <link href="css/sb-admin-2.min.css" rel="stylesheet">
        <style>
            body {
                display: flex;
                align-items: center;
                justify-content: center;
                height: 100vh;
            }
        </style>
    </head>
    <body>
        <div class="container text-center">
            <div class="card mb-3 bg-dark">
                <h3 class="text-white my-2">Control de Acceso</h3>
            </div>
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-body text-center">
                            <h5 class="card-title">Empleado</h5>
                            <h1 class="my-4">
                                <i class="fas fa-user"></i>
                            </h1>
                            <a href="loginEmpleado.jsp" class="btn btn-primary">Acceder como Empleado</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-body text-center">
                            <h5 class="card-title">Administrador</h5>
                            <h1 class="my-4">
                                <i class="fas fa-user-shield"></i>
                            </h1>
                            <a href="loginAdmin.jsp" class="btn btn-danger">Acceder como Administrador</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
