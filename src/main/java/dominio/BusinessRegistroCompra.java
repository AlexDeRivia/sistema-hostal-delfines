package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BusinessRegistroCompra {
    Dao<RegistroCompra> rcompraDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getRegistroCompraDAO();
    
    public List<RegistroCompra> listado() {
        return rcompraDAO.listado();
    }
    
    public RegistroCompra crear(RegistroCompra obj) {
        return rcompraDAO.crear(obj);
    }
    
    public RegistroCompra actualizar(RegistroCompra obj) {
        return rcompraDAO.actualizar(obj);
    }
    public RegistroCompra registroCompraDiario(Turno turno, int empleadoId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        String fecha = fechaHoraActual.format(formatter);
        
        for(RegistroCompra rc : this.listado()){
            if(rc.getFecha().equals(fecha) && rc.getTurno().equals(turno)
                    && rc.getEmpleado().getId() == empleadoId){
                return rc;
            }
        }
        return null;
    }
}
