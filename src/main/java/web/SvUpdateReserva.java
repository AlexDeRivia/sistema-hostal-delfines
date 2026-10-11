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
                    this.modificarReserva(request, response);
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
        int idReserva = Integer.parseInt(request.getParameter("idReserva"));
        Reserva reservaid = new Reserva();
        reservaid.setId(idReserva);
        Reserva reserva = new BusinessReserva().buscar(reservaid.getId());
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
        //Recuperamos datos del formulario
        Integer id = Integer.valueOf(request.getParameter("reservationId"));
        Reserva existing = new BusinessReserva().buscar(id);
        if (existing == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Integer nHabitacion = Integer.valueOf(request.getParameter("roomNumber"));
        System.out.println(nHabitacion);
        // Intenta analizar el texto del campo de entrada de tiempo en un objeto LocalTime
        //LocalTime horaIngreso = LocalTime.parse(request.getParameter("checkInTime"), DateTimeFormatter.ofPattern("H:mm"));

        //LocalTime horaIngreso = LocalTime.parse(request.getParameter("checkInTime"), DateTimeFormatter.ofPattern("HH:mm:ss"));
        String hora_ingreso = request.getParameter("checkInTime");
        System.out.println(hora_ingreso);
        String horas_reservadas = request.getParameter("reservedHours");
        double costoEfectivo;
        double costoYape;
        try {
            costoEfectivo = Double.parseDouble(request.getParameter("cashPayment"));
        } catch (Exception e) {
            costoEfectivo = 0;
        }
        try {
            costoYape = Double.parseDouble(request.getParameter("yapePayment"));
        } catch (Exception e) {
            costoYape = 0;
        }
        String nombrecliente = request.getParameter("clientName");
        System.out.println(nombrecliente);
        int numero_huespedes = Integer.parseInt(request.getParameter("numberOfGuests"));
        System.out.println(numero_huespedes);
        EstadoReserva estado_reserva = EstadoReserva.valueOf(request.getParameter("reservationStatus"));
        System.out.println(estado_reserva);
        BusinessHabitacion infoHabitacion = new BusinessHabitacion();
        Habitacion habitacion = infoHabitacion.getHabitacionByNumero(nHabitacion);
        System.out.println(habitacion);
        BusinessCliente infoCliente = new BusinessCliente();
        Cliente cliente = infoCliente.getClienteByNombre(nombrecliente);
        System.out.println(cliente);
        habitacion.setDisponible(false);
        if (estado_reserva.equals(EstadoReserva.CANCELADO) || estado_reserva.equals(EstadoReserva.CONFIRMADO)) {
            habitacion.setDisponible(true);
            infoHabitacion.actualizar(habitacion);
        }

        //Guardamos datos
        Reserva reserva = new Reserva();
        reserva.setId(id);
        reserva.setNumeroHuespedes(numero_huespedes);
        reserva.setCostoEfectivo(costoEfectivo);
        reserva.setCostoYape(costoYape);
        reserva.setHora_ingreso(hora_ingreso);
        reserva.setHoras_reservadas(horas_reservadas);
        reserva.setEstadoReserva(estado_reserva);
        reserva.setCliente(cliente);
        reserva.setHabitacion(habitacion);
        reserva.setRegistroreserva_id(existing.getRegistroreserva_id());

        BusinessReserva inforeserva = new BusinessReserva();
        inforeserva.actualizar(reserva);
        System.out.println("modificando");

        //this.ActualizarReserva(request, response);
        this.accionDefault(request, response);

        //this.iniciarRegistroReserva();
    }

}
