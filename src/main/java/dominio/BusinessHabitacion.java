package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;
import java.util.ArrayList;
import java.util.List;

public class BusinessHabitacion {
    Dao<Habitacion> habitacionDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getHabitacionDAO();
    
    public List<Habitacion> listado() {
        return habitacionDAO.listado();
    }
    
    public Habitacion crear(Habitacion obj) {
        return habitacionDAO.crear(obj);
    }

    public Habitacion eliminar(Habitacion obj) {
        return habitacionDAO.eliminar(obj);
    }

    public Habitacion actualizar(Habitacion obj) {
        return habitacionDAO.actualizar(obj);
    }

    public Habitacion buscar(int id) {
        return habitacionDAO.buscar(id);
    }
    
    public Habitacion getHabitacionByNumero(int numero) {
        for(Habitacion h : habitacionDAO.listado()){
            int numeroObtenido = h.getNumero();
            if(numeroObtenido == numero){
                return h;
            }
        }
        return null;
    }
    
    public List<Habitacion> getHabitacionesDisponibles() {
        List<Habitacion> lh = new ArrayList<>();
        for(Habitacion h : habitacionDAO.listado()){
            if(h.isDisponible()){
                lh.add(h);
            }
        }
        return lh;
    }
}
