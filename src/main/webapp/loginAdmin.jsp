<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hostal delfines</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link href="css/sb-admin-2.min.css" rel="stylesheet">
    </head>

    <body>

        <div class="container py-5 my-3">

            <div class="row">

                <div class="col-lg-6 offset-lg-3">

                    <div class="card shadow-sm">

                        <div class="card-header bg-danger">
                            <a href="index.jsp" class="text-decoration-none">
                                <img src="img/logo/white.png" alt="Logo" height="40" class="my-3">
                            </a>
                            <span class="text-white fs-5 ms-3">Login del Administrador</span>
                        </div>

                        <form action="SvLoginAdmin" method="POST" id="clearForm" class="needs-validation" novalidate>
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">

                            <% if (request.getAttribute("error") != null) { %>
                            <div class="card-header">
                                <div class="alert alert-danger mb-0">
                                    <%= request.getAttribute("error") %>
                                </div>
                            </div>
                            <% } %>

                            <div class="card-body">

                                <div class="form-floating mb-3">
                                    <input type="text" class="form-control" placeholder="Usuario" id="email" name="usuario" required>
                                    <label for="email" class="form-label">Usuario</label>
                                    <div class="invalid-feedback">Ingrese un nombre de usuario válido</div>
                                </div>

                                <div class="form-floating pass-show">
                                    <input type="password" class="form-control" placeholder="Clave" id="contrasena" name="clave" required>
                                    <span><i class="text-secondary bi bi-eye-fill" id="eye"></i></span>
                                    <label for="contrasena" class="form-label">Clave</label>
                                    <div class="invalid-feedback">Ingrese su contraseña</div>
                                </div>
                            </div>
                            <div class="card-footer text-end">
                                <div class="row my-1">
                                    <div class="col">
                                        <a type="button" class="btn btn-secondary ms-2" onclick="limpiarFormulario()">
                                            Limpiar
                                        </a>
                                        <button type="submit" class="btn btn-primary ms-2">
                                            Ingresar
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer text-end">
                                <a href="loginEmpleado.jsp">Acceder como empleado</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>

    <style>
        #eye{
            font-size: 30px;
        }
        .pass-show {
            position: relative;
        }
        .pass-show span {
            position: absolute;
            right: 32px;
            top: 8px;
            text-transform: uppercase;
            cursor: pointer;
        }
        .pass-show input.form-control {
            padding-right: 70px !important;
        }
        .fn-danger {
            background: #4e73df;
        }
    </style>

    <script>
        document.querySelector('.pass-show span').addEventListener('click', e => {
            const passwordInput = document.querySelector('#contrasena');
            var eye = document.getElementById('eye');
            if (e.target.classList.contains('show')) {
                e.target.classList.remove('show');
                eye.classList.remove('bi-eye-fill');
                eye.classList.add('bi-eye-slash-fill');
                passwordInput.type = 'text';
            } else {
                e.target.classList.add('show');
                eye.classList.remove('bi-eye-slash-fill');
                eye.classList.add('bi-eye-fill');
                passwordInput.type = 'password';
            }
        });
        document.getElementById('eye').classList.add('show');
        function limpiarFormulario() {
            const formulario = document.getElementById("clearForm");
            for (let i = 0; i < formulario.elements.length; i++) {
                const elemento = formulario.elements[i];
                elemento.value = null;
            }
        }
    </script>
</html>
