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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
        if (session.getAttribute("srreserva") == null) {
            session.setAttribute("srreserva", new ServletControladorReserva().iniciarRegistroReserva());
        }
        List<Habitacion> habitaciones = new BusinessHabitacion().getHabitacionesDisponibles();
        List<Cliente> clientes = new BusinessCliente().listado();
        request.setAttribute("habitaciones", habitaciones);
        request.setAttribute("clientes", clientes);
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
                    this.agregarReserva(request, response);
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
        Habitacion habitacion = habitacionInformation.getHabitacionByNumero(Integer.parseInt(request.getParameter("roomNumber")));
        String horasReservadas = request.getParameter("reservedHours");
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
        //Recuperamos datos del formulario
        Integer nHabitacion = Integer.valueOf(request.getParameter("roomNumber"));
        
        // Intenta analizar el texto del campo de entrada de tiempo en un objeto LocalTime
        //LocalTime horaIngreso = LocalTime.parse(request.getParameter("checkInTime"), DateTimeFormatter.ofPattern("H:mm"));
        System.out.println(request.getParameter("checkInTime"));
        //LocalTime horaIngreso = LocalTime.parse(request.getParameter("checkInTime"), DateTimeFormatter.ofPattern("HH:mm:ss"));
        String hora_ingreso = request.getParameter("checkInTime");
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
        int numero_huespedes = Integer.parseInt(request.getParameter("numberOfGuests"));
        EstadoReserva estado_reserva = EstadoReserva.PENDIENTE;

        //Liquidacion
        HttpSession sessionr = request.getSession();
        RegistroReserva rreserva = new ServletControladorReserva().iniciarRegistroReserva();
        if (rreserva.getId() == 0) {
            rreserva = inforreserva.crear(rreserva);
        }
        sessionr.setAttribute("srreserva", rreserva);
        System.out.println(rreserva);
        rreserva.setLiquidacion(rreserva.getLiquidacion() + costoEfectivo + costoYape);
        System.out.println("Liquidación: "+rreserva.getLiquidacion());
        inforreserva.actualizar(rreserva);
        
        //Guardamos datos
        BusinessHabitacion infoHabitacion = new BusinessHabitacion();
        Habitacion habitacion = infoHabitacion.getHabitacionByNumero(nHabitacion);
        BusinessCliente infoCliente = new BusinessCliente();
        Cliente cliente = infoCliente.getClienteByNombre(nombrecliente);

        // Verificar si habitacion es null antes de continuar
        if (habitacion != null) {
            habitacion.setDisponible(false);
            infoHabitacion.actualizar(habitacion);
        } else {
            // Manejar el caso cuando habitacion es null
            System.out.println("La habitación con número " + nHabitacion + " no fue encontrada.");
        }

        Reserva reserva = new Reserva();

        reserva.setNumeroHuespedes(numero_huespedes);
        reserva.setCostoEfectivo(costoEfectivo);
        reserva.setCostoYape(costoYape);
        reserva.setHoras_reservadas(horas_reservadas);
        reserva.setHora_ingreso(hora_ingreso);
        reserva.setEstadoReserva(estado_reserva);
        reserva.setCliente(cliente);
        reserva.setHabitacion(habitacion);
        System.out.println("registro reserva " +rreserva.getId());
        reserva.setRegistroreserva_id(rreserva.getId());

        BusinessReserva inforeserva = new BusinessReserva();
        inforeserva.crear(reserva);

        //Redirigimos hacia la pagina por default
        this.accionDefault(request, response);
        System.out.println("se guardo los datos");
        // Continúa con el resto de tu lógica aquí

    }

}
