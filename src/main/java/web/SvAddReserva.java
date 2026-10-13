package web;

import dominio.BusinessCliente;
import dominio.BusinessHabitacion;
import dominio.BusinessRegistroReserva;
import dominio.BusinessReserva;
import dominio.Cliente;
import dominio.CostoReserva;
import dominio.EstadoReserva;
import dominio.Habitacion;
import dominio.RegistroReserva;
import dominio.Reserva;
import dominio.ReservaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.time.LocalDate;
import org.json.JSONObject;

@WebServlet("/SvAddReserva")
public class SvAddReserva extends HttpServlet{
    private BusinessRegistroReserva inforreserva = new BusinessRegistroReserva();
    private BusinessHabitacion habitacionInformation= new BusinessHabitacion();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (accion != null) {
            switch (accion) {
                case "obtenerCosto":
                    this.calcularCosto(request, response);
                    return;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }
    
    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setAttribute("srreserva", new ServletControladorReserva().iniciarRegistroReserva(request));
        List<Habitacion> habitaciones = new BusinessHabitacion().getHabitacionesDisponibles();
        List<Cliente> clientes = new BusinessCliente().listado();
        request.setAttribute("habitaciones", habitaciones);
        request.setAttribute("clientes", clientes);
        request.setAttribute("fechaHoy", LocalDate.now().toString());
        request.getRequestDispatcher("AddReserva.jsp").forward(request, response);
        //response.sendRedirect("AddReserva.jsp");
        System.out.println("comenzando");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        //double costoCalculado = 0.0;

        if (accion != null) {
            switch (accion) {
                case "add":
                    try {
                        this.agregarReserva(request, response);
                    } catch (IllegalArgumentException ex) {
                        request.setAttribute("error", ex.getMessage());
                        this.accionDefault(request, response);
                    }
                    break;
                case "obtenerCosto":
                    this.calcularCosto(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void calcularCosto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Habitacion habitacion = habitacionInformation.buscar(Integer.parseInt(request.getParameter("roomId")));
        String horasReservadas = request.getParameter("reservedHours");
        if (habitacion == null || (!"3".equals(horasReservadas) && !"24".equals(horasReservadas))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        System.out.println("habitacion: " + habitacion.getNumero());
        System.out.println("horas: " + horasReservadas);
        CostoReserva crsv = new CostoReserva();
        double costo = crsv.calcularCosto(horasReservadas, habitacion);
        System.out.println("costo: " + costo);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("costo", costo);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }

    private void agregarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Reserva reserva = new Reserva();
        reserva.setNumeroHuespedes(Integer.parseInt(request.getParameter("numberOfGuests")));
        reserva.setCostoEfectivo(pago(request.getParameter("cashPayment")));
        reserva.setCostoYape(pago(request.getParameter("yapePayment")));
        reserva.setHoras_reservadas(request.getParameter("reservedHours"));
        reserva.setFechaIngreso(request.getParameter("checkInDate"));
        reserva.setHora_ingreso(request.getParameter("checkInTime"));
        reserva.setEstadoReserva(EstadoReserva.PENDIENTE);
        reserva.setCliente(new BusinessCliente().buscar(Integer.parseInt(request.getParameter("clientId"))));
        reserva.setHabitacion(habitacionInformation.buscar(Integer.parseInt(request.getParameter("roomId"))));
        RegistroReserva registro = new ServletControladorReserva().iniciarRegistroReserva(request);
        ReservaService.crear(reserva, registro);
        response.sendRedirect(request.getContextPath() + "/ServletControladorReserva?ok=creada");
    }

    private static double pago(String valor) {
        return valor == null || valor.isBlank() ? 0 : Double.parseDouble(valor);
    }

}
