package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BusinessRegistroVenta {
    Dao<RegistroVenta> rventaDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getRegistroVentaDAO();
    
    public List<RegistroVenta> listado() {
        return rventaDAO.listado();
    }
    
    public RegistroVenta crear(RegistroVenta obj) {
        return rventaDAO.crear(obj);
    }
    
    public RegistroVenta actualizar(RegistroVenta obj) {
        return rventaDAO.actualizar(obj);
    }
    public RegistroVenta registroVentaDiario(Turno turno) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        String fecha = fechaHoraActual.format(formatter);
        
        
        for(RegistroVenta rv : this.listado()){
            if(rv.getFecha().equals(fecha) && rv.getTurno().equals(turno)){
                return rv;
            }
        }
        return null;
    }
}
