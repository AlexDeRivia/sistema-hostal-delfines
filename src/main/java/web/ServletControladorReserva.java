package web;

import datos.ReservaDaoAT;
import dominio.BusinessCliente;
import dominio.BusinessHabitacion;
import dominio.BusinessRegistroReserva;
import dominio.BusinessReserva;
import dominio.Cliente;
import dominio.Empleado;
import dominio.Habitacion;
import dominio.RegistroReserva;
import dominio.Reserva;
import dominio.Turno;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.Authenticator;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "/ServletControladorReserva", urlPatterns = {"/ServletControladorReserva"})

public class ServletControladorReserva extends HttpServlet{
    private BusinessReserva reservaInformation;
    private BusinessCliente clienteInformation;
    private BusinessHabitacion habitacionInformation;
    private BusinessRegistroReserva inforreserva;
    private Reserva reserva;
    private Empleado empleadoSession;
    
    public ServletControladorReserva(){
        this.empleadoSession = new Empleado();
        empleadoSession.setId(1);
        empleadoSession.setDni("75465361");
        empleadoSession.setNombre("Nilton");
        empleadoSession.setUsername("admin");
        
        reservaInformation = new BusinessReserva();
        clienteInformation = new BusinessCliente();
        habitacionInformation = new BusinessHabitacion();
        inforreserva = new BusinessRegistroReserva();
        reserva = new Reserva();
    }
            
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RegistroReserva rreserva = this.iniciarRegistroReserva();

        List<Reserva> reservas = new ReservaDaoAT().listado();
        request.setAttribute("reservas", reservas);
        request.setAttribute("rreserva", rreserva);
        HttpSession sessionr = request.getSession();
        sessionr.setAttribute("srreserva", rreserva);
        RegistroReserva rrservanuevo = (RegistroReserva) sessionr.getAttribute("srreserva");
        System.out.println("sesion registro reserva " + rrservanuevo);
        request.getRequestDispatcher("reservas.jsp").forward(request, response);

    }

    public RegistroReserva iniciarRegistroReserva() {
        //CREACION DEL REGISTRO DE RESERVA DIARIO
        RegistroReserva rsvTemp = null;
        Turno turno = Turno.valueOf(this.obtenerTurno());
        rsvTemp = inforreserva.registroReservaDiario(turno);
        if (rsvTemp != null) {
            return rsvTemp;
        } else {
            rsvTemp = new RegistroReserva();
            rsvTemp.setFecha(this.obtenerFecha());
            rsvTemp.setTurno(turno);
            rsvTemp.setEmpleado(empleadoSession);
            System.out.println(rsvTemp);
            // Verifica si el Empleado es nulo antes de intentar acceder a su id
            if (empleadoSession != null) {
                rsvTemp.setEmpleado(empleadoSession);
            } else {
                System.out.println("No existe empleado");
            }

            rsvTemp.setLiquidacion(0);
            return rsvTemp;
        }
    }
    
    public String obtenerTurno(){
        LocalTime hora = LocalTime.now();
        if (hora.isAfter(LocalTime.of(6, 0)) && hora.isBefore(LocalTime.of(12, 1))) {
            return Turno.MANANA.name();
        } else if (hora.isAfter(LocalTime.of(12, 0)) && hora.isBefore(LocalTime.of(18, 1))) {
            return Turno.TARDE.name();
        } else {
            return Turno.NOCHE.name();
        }
    }
    
    public String obtenerFecha(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        return fechaHoraActual.format(formatter);
    }
    
    
}
