package dominio;

import datos.AbstractDaoFactory;
import datos.Dao;
import datos.FactoryType;

public class BusinessVenta {
    Dao<Venta> ventaDAO = AbstractDaoFactory.getFactory(FactoryType.ARCHIVO_TEXTO_DAO_FACTORY).getVentaDAO();
    
    public Venta crear(Venta obj) {
        return ventaDAO.crear(obj);
    }
}
