package web;

import dominio.BusinessProducto;
import dominio.Producto;
import dominio.Turno;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "SvProducto", urlPatterns = {"/SvProducto"})
public class SvProducto extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Producto> productos = new BusinessProducto().listado();
        request.setAttribute("productos", productos);
        request.getRequestDispatcher("VistaModuloProducto.jsp").forward(request, response);
        request.setAttribute("turno", this.obtenerTurno());
        request.setAttribute("fecha", this.obtenerFecha());
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
