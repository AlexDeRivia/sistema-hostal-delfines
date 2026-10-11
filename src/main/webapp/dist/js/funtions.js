function calculateCost() {
    var roomNumber = $("#roomNumber").val();
    var reservedHours = $("#reservedHours").val();

    $.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/SvAddReserva",
        data: {
            accion: "obtenerCosto",
            roomNumber: roomNumber,
            reservedHours: reservedHours
        },
        success: function(data) {
            console.log(data);
            var costoString = data.costo.toFixed(2);
            $("#costResult").val(costoString);
        },
        error: function(xhr, status, error) {
            console.error("Error al calcular el costo:", error);
        }
    });
}

function calcularCosto() {
    // Obtener el valor seleccionado en el campo de horas reservadas
    var horasReservadas = document.getElementById("reservedHours").value;

    // Obtener la opción seleccionada
    var roomSelect = document.getElementById("roomNumber");
    var habitacionOption = roomSelect.options[roomSelect.selectedIndex];

    // Obtener los atributos data de la opción seleccionada
    var ventanaAfuera = habitacionOption.getAttribute("data-ventana") === "true";
    var cantCamas = parseInt(habitacionOption.getAttribute("data-camas"));

    // Realizar el cálculo del costo
    var costo = 0;

    if (horasReservadas === "3") {
        if (ventanaAfuera) {
            costo += 25;
        } else {
            costo += 20;
        }
    } else if (horasReservadas === "24") {
        if (cantCamas === 2) {
            costo += 70;
        } else if (ventanaAfuera) {
            costo += 50;
        } else {
            costo += 40;
        }
    }

    // Mostrar el resultado en el input costResult
    document.getElementById("costResult").value = costo;
}

function calcularNuevoCosto() {
    // Obtener el valor de las horas reservadas y adicionales
    var horasReservadas = document.getElementById("reservedHours").value;
    var horasAdicionales = document.getElementById("additionalHours").value;

    // Obtener la opción de habitación seleccionada
    var roomSelect = document.getElementById("roomNumber");
    var habitacionOption = roomSelect.options[roomSelect.selectedIndex];

    // Obtener los atributos data de la opción seleccionada
    var ventanaAfuera = habitacionOption.getAttribute("data-ventana") === "true";
    var cantCamas = parseInt(habitacionOption.getAttribute("data-camas"));

    // Realizar el cálculo del nuevo costo
    var costoAdicional = 0;
    var horas = parseInt(horasReservadas);

    if (horasAdicionales > 0 && horasAdicionales <= 2 * horas) {
        costoAdicional += horasAdicionales * 10;
    }

    if (horasAdicionales > 2 * horas) {
        // Llamar a la función que calcula el costo base
        var costoBase = calcularCosto(horasReservadas, ventanaAfuera, cantCamas);

        costoAdicional += costoBase;
    }

    // Mostrar el resultado en el input newCostResult
    document.getElementById("newCostResult").value = costoAdicional;
}

function limpiarFormulario() {
    const formulario = document.getElementById("clearReserva");
    for (let i = 0; i < formulario.elements.length; i++) {
        const elemento = formulario.elements[i];
        elemento.value = null;
    }
}

function calculateNewCost() {
    // Implement your new cost calculation logic here
    // Update the "newCostResult" input field with the calculated result
    document.getElementById("newCostResult").value = "Calculated Value"; // Replace with your calculation
}

function updateReservation() {
    // Implement your logic to update the reservation
    alert("Reservation Updated"); // Placeholder, replace with your logic
}

function cancelUpdate() {
    // Implement your logic to cancel the update
    alert("Update Canceled"); // Placeholder, replace with your logic
}



