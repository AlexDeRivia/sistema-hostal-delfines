package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;

import java.util.List;

public class BusinessCliente {
    Dao<Cliente> clienteDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getClienteDAO();
    
    public List<Cliente> listado() {
        return clienteDAO.listado();
    }
    
    public Cliente crear(Cliente obj) {
        return clienteDAO.crear(obj);
    }
    
    public Cliente buscar(int id) {
        return clienteDAO.buscar(id);
    }
    
    public Cliente eliminar(Cliente obj) {
        return clienteDAO.eliminar(obj);
    }
    
    public Cliente actualizar(Cliente obj) {
        return clienteDAO.actualizar(obj);
    }
    
    public Cliente getClienteByNombre(String nombre) {
        for(Cliente c : clienteDAO.listado()){
            String nombreObtenido = c.getNombre() + " " + c.getApellido();
            if(nombreObtenido.equals(nombre)){
                return c;
            }
        }
        return null;
    }
}
