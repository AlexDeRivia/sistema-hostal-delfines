package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;

public class BusinessCompra {
    Dao<Compra> compraDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getCompraDAO();
    
    public Compra crear(Compra obj) {
        return compraDAO.crear(obj);
    }
    
}
