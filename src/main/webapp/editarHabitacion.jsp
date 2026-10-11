<%@page import="dominio.Habitacion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/header.jsp"%>
<%@include file="components/bodyprimeraparte.jsp"%>

<style>
    /* Estilos CSS para el selector */
    .form-select {
        width: 300px;
        height: 35px;
        border-radius: 20px;      
    }
</style>

<%
    // Obtener la habitacion a editar de la sesión
    Habitacion habitacion = (Habitacion) request.getSession().getAttribute("habitacionEditar");
%>

<form class="user" action="SvEditHabitacion" method="POST">
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
    <div class="form-group col">  
        <div class="col-sm-6 mb-3"> 
            <h1>Agregar Habitaciones</h1>
            <p>Modulo para agregar habitaciones al sistema</p>
            <label for="numero" class="form-label">N° Habitacion: </label>
            <input type="number" class="form-control form-control-user" id="numero" name="numero"
                placeholder="000" value="<%=habitacion.getNumero()%>" required>
             <div class="invalid-feedback">Este campo es obligatorio</div>
        </div>
        <div class="col-sm-6 mb-3">
             <label for="numero" class="form-label">N° Camas: </label>
            <input type="number" class="form-control form-control-user" id="numeroCamas" name="numeroCamas"
                placeholder="0" value="<%=habitacion.getNumeroCamas()%>" required>
             <div class="invalid-feedback">Este campo es obligatorio</div>
        </div>
        <div class="col-sm-6 mb-3">
            <label for="ventanaAfuera" class="form-label">Ventana Afuera: </label>
            <select class="form-select" aria-label="Default select example" id="ventanaAfuera" name="ventanaAfuera" required>
                 <option selected></option>                      
                 <option value="true" <%=habitacion.isVentanaAfuera()? "selected": ""%>>Si</option>
                  <option value="false" <%=!habitacion.isVentanaAfuera()? "selected": ""%>>No</option>                  
            </select>
             <div class="invalid-feedback">Este campo es obligatorio</div>
        </div>
        <div class="col-sm-6 mb-3">
            <label for="disponible" class="form-label">Disponible: </label>
            <select class="form-select" aria-label="Default select example" id="disponible" name="disponible" required>
                 <option selected></option>                      
                 <option value="true" <%=habitacion.isDisponible()? "selected": ""%>>Si</option>
                  <option value="false" <%=!habitacion.isDisponible()? "selected": ""%>>No</option>                  
            </select>
            <div class="invalid-feedback">Este campo es obligatorio</div>
        </div> 
    </div>
    <button class="btn btn-primary btn-user btn-block" type="submit">
        Guardar Cambios
    </button>
</form>


<%@include file="components/bodyfinal.jsp"%>
