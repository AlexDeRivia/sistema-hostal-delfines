package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;
import datos.RegistroReservaDaoMySQL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BusinessReserva {
    Dao<Reserva> reservaDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getReservaDAO();
    
    public Reserva crear(Reserva obj){
        return reservaDAO.crear(obj);
    }
    
    public Reserva actualizar(Reserva obj) {
        return reservaDAO.actualizar(obj);
    }
    
    public List<Reserva> listado() {
        return reservaDAO.listado();
    }

    public Reserva eliminar(Reserva obj) {
        return reservaDAO.eliminar(obj);
    }
    
    public Reserva buscar(int id) {
        return reservaDAO.buscar(id);
    }
    
    
}
