package web;

import dominio.BusinessCliente;
import dominio.BusinessHabitacion;
import dominio.BusinessRegistroReserva;
import dominio.BusinessReserva;
import dominio.Cliente;
import dominio.CostoReserva;
import dominio.Empleado;
import dominio.EstadoReserva;
import dominio.Habitacion;
import dominio.RegistroReserva;
import dominio.Reserva;
import dominio.ReservaService;
import dominio.Turno;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

@WebServlet("/SvUpdateReserva")
public class SvUpdateReserva extends HttpServlet {

    private BusinessRegistroReserva inforreserva = new BusinessRegistroReserva();
    private BusinessHabitacion habitacionInformation = new BusinessHabitacion();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "actualizar":
                    //this.iniciarRegistroReserva(); // Asegúrate de inicializar rreserva
                    this.ActualizarReserva(request, response);
                    System.out.println("Actualizando");
                    break;
                case "obtenerNuevoCosto":
                    this.calcularNuevoCosto(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Habitacion> habitaciones = new BusinessHabitacion().listado();
        List<Cliente> clientes = new BusinessCliente().listado();
        List<EstadoReserva> estados = Arrays.asList(EstadoReserva.values());
        request.setAttribute("habitaciones", habitaciones);
        request.setAttribute("clientes", clientes);
        request.setAttribute("estados", estados);
        request.getRequestDispatcher("index.jsp").forward(request, response);
        //response.sendRedirect("AddReserva.jsp");
        System.out.println("comenzando");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "update":
                    try {
                        this.modificarReserva(request, response);
                    } catch (IllegalArgumentException ex) {
                        request.setAttribute("error", ex.getMessage());
                        this.ActualizarReserva(request, response);
                    }
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private void calcularNuevoCosto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Habitacion habitacion = habitacionInformation.getHabitacionByNumero(Integer.parseInt(request.getParameter("roomNumber")));
        String horasReservadas = request.getParameter("reservedHours");
        int horas_adicional = Integer.parseInt(request.getParameter("additionalHours"));
        System.out.println("habitacion: " + habitacion.getNumero());
        System.out.println("horas: " + horasReservadas);
        CostoReserva crsv = new CostoReserva();
        double costo = crsv.calcularCostoAdicional(horas_adicional, horasReservadas, habitacion);
        System.out.println("costo: " + costo);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("costo", costo);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }

    private void ActualizarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestedId = request.getParameter("idReserva");
        int idReserva = Integer.parseInt(requestedId == null
                ? request.getParameter("reservationId") : requestedId);
        Reserva reservaid = new Reserva();
        reservaid.setId(idReserva);
        Reserva reserva = new BusinessReserva().buscar(reservaid.getId());
        if (reserva == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("reserva", reserva);
        List<Habitacion> habitaciones = new BusinessHabitacion().listado();
        List<Cliente> clientes = new BusinessCliente().listado();
        List<EstadoReserva> estados = Arrays.asList(EstadoReserva.values());
        request.setAttribute("habitaciones", habitaciones);
        request.setAttribute("clientes", clientes);
        request.setAttribute("estados", estados);
        String jspUpdate = "UpdateReserva.jsp";
        request.getRequestDispatcher(jspUpdate).forward(request, response);
    }

    private void modificarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Reserva reserva = new Reserva();
        reserva.setId(Integer.parseInt(request.getParameter("reservationId")));
        reserva.setNumeroHuespedes(Integer.parseInt(request.getParameter("numberOfGuests")));
        reserva.setCostoEfectivo(pago(request.getParameter("cashPayment")));
        reserva.setCostoYape(pago(request.getParameter("yapePayment")));
        reserva.setFechaIngreso(request.getParameter("checkInDate"));
        reserva.setHora_ingreso(request.getParameter("checkInTime"));
        reserva.setHoras_reservadas(request.getParameter("reservedHours"));
        String estado = request.getParameter("reservationStatus");
        if (estado == null) {
            throw new IllegalArgumentException("Selecciona un estado válido.");
        }
        reserva.setEstadoReserva(EstadoReserva.valueOf(estado));
        reserva.setCliente(new BusinessCliente().buscar(Integer.parseInt(request.getParameter("clientId"))));
        reserva.setHabitacion(new BusinessHabitacion().buscar(Integer.parseInt(request.getParameter("roomId"))));
        ReservaService.actualizar(reserva);
        response.sendRedirect(request.getContextPath() + "/ServletControladorReserva?ok=actualizada");
    }

    private static double pago(String valor) {
        return valor == null || valor.isBlank() ? 0 : Double.parseDouble(valor);
    }

}
