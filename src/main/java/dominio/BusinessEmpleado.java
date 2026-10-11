package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;
import java.util.List;

public class BusinessEmpleado {
    Dao<Empleado> empleadoDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getEmpleadoDAO();
    
    public Empleado login(String username, String clave){
        List<Empleado> listaEmpleados = empleadoDAO.listado();
        for(Empleado e: listaEmpleados){
            if(username != null && username.equals(e.getUsername())
                    && PasswordCodec.matches(clave, e.getClave())){
                if (!PasswordCodec.isEncoded(e.getClave())) {
                    e.setClave(PasswordCodec.encode(clave));
                    empleadoDAO.actualizar(e);
                }
                return e;
            }
        }
        return null;
    }
    
    public Empleado crear(Empleado obj){
        obj.setClave(PasswordCodec.encode(obj.getClave()));
        return empleadoDAO.crear(obj);
    }
    
    public Empleado buscar(int id){
        return empleadoDAO.buscar(id);
    }
    
    public List<Empleado> listado(){
        return empleadoDAO.listado();
    }
    
    public Empleado actualizar(Empleado obj){
        if (obj.getClave() == null || obj.getClave().isBlank()) {
            Empleado existing = empleadoDAO.buscar(obj.getId());
            if (existing == null) {
                return null;
            }
            obj.setClave(existing.getClave());
        } else if (!PasswordCodec.isEncoded(obj.getClave())) {
            obj.setClave(PasswordCodec.encode(obj.getClave()));
        }
        return empleadoDAO.actualizar(obj);
    }
    
    public Empleado eliminar(Empleado obj){
        return empleadoDAO.eliminar(obj);
    }
}
