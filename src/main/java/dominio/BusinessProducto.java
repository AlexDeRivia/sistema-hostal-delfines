package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;
import java.util.ArrayList;
import java.util.List;

public class BusinessProducto {
    Dao<Producto> productoDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getProductoDAO();
    
    public List<Producto> listado() {
        return productoDAO.listado();
    }
    public List<Producto> getProductoAlimento(){
        List<Producto> alimentos = new ArrayList<>();
        for(Producto p: productoDAO.listado()){
            if(p.getCategoria().equals(Categoria.ALIMENTOS)){
                alimentos.add(p);
            }
        }
        return alimentos;
    }
    public List<Producto> getProductoPersonal(){
        List<Producto> personal = new ArrayList<>();
        for(Producto p: productoDAO.listado()){
            if(p.getCategoria().equals(Categoria.USO_PERSONAL)){
                personal.add(p);
            }
        }
        return personal;
    }
    
    public Producto getProductoByNombre(String nombre){
        for(Producto p: productoDAO.listado()){
            if(p.getNombre().equals(nombre)){
                return p;
            }
        }
        return null;
    }
    public Producto crear(Producto obj) {
        return productoDAO.crear(obj);
    }
    
    public Producto eliminar(Producto obj) {
        return productoDAO.eliminar(obj);
    }

    public Producto actualizar(Producto obj) {

        return productoDAO.actualizar(obj);
    }
    
    public Producto buscar(int id) {
        return productoDAO.buscar(id);
    }
}
