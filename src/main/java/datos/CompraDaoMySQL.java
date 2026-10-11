package datos;

import dominio.Compra;
import dominio.LineaCompra;
import dominio.Proveedor;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompraDaoMySQL extends Dao<Compra> {

    private static final String TABLE_NAME = "compra";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_REGISTRO_COMPRA_ID = "registro_compra_id";
    private static final String COLUMN_PROVEEDOR_ID = "proveedor_id";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_REGISTRO_COMPRA_ID + ", " + COLUMN_PROVEEDOR_ID + ") VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_REGISTRO_COMPRA_ID + " = ?, " + COLUMN_PROVEEDOR_ID + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL_WITH_LINES = 
        "SELECT c.*, lc.id AS lc_id, lc.compra_id AS lc_compra_id, lc.producto_id AS lc_producto_id, lc.precio_unitario AS lc_precio_unitario, lc.cantidad AS lc_cantidad " +
        "FROM compra c " +
        "LEFT JOIN linea_compra lc ON c.id = lc.compra_id";
    //private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Compra crear(Compra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, obj.getId_registroCompra());
            preparedStatement.setInt(2, obj.getProveedor().getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating compra failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating compra failed, no ID obtained.");
                }
            }

            // Llamar al DAO de LineaCompra y realizar la inserción
            LineaCompraDaoMySQL lineaCompraDao = new LineaCompraDaoMySQL();
            for (LineaCompra lineaCompra : obj.getLineaCompras()) {
                lineaCompra.setCompra_id(obj.getId());
                lineaCompraDao.crear(lineaCompra);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public Compra eliminar(Compra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Compra actualizar(Compra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setInt(1, obj.getId_registroCompra());
            preparedStatement.setInt(2, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Compra buscar(int id) {
        Compra compra = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    compra = mapResultSetToCompra(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return compra;
    }

    @Override
    public List<Compra> listado() {
        List<Compra> listaCompras = new ArrayList<>();
        List<LineaCompra> listaLineasCompras = new ArrayList<>();

        try (Statement statement = connect.createStatement(); ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_WITH_LINES)) {

            while (resultSet.next()) {
                Compra compra = mapResultSetToCompra(resultSet);
                LineaCompra lineaCompra = mapResultSetToLineaCompra(resultSet);

                if (!listaCompras.contains(compra)) {
                    // Si la compra no está en la lista, agrégala
                    listaCompras.add(compra);
                }

                // Agrega la línea de compra a la lista
                listaLineasCompras.add(lineaCompra);
            }

            // Asigna las líneas de compra a las compras correspondientes
            for (Compra compra : listaCompras) {
                List<LineaCompra> lineasCompras = new ArrayList<>();
                for (LineaCompra lineaCompra : listaLineasCompras) {
                    if (lineaCompra.getCompra_id() == compra.getId()) {
                        lineasCompras.add(lineaCompra);
                    }
                }
                compra.setLineaCompras((ArrayList<LineaCompra>) lineasCompras);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaCompras;
    }

    private LineaCompra mapResultSetToLineaCompra(ResultSet resultSet) throws SQLException {
        LineaCompra lineaCompra = new LineaCompra();
        lineaCompra.setId(resultSet.getInt("lc_id"));
        lineaCompra.setCompra_id(resultSet.getInt("lc_compra_id"));
        // Configurar atributos de LineaCompra según sea necesario
        lineaCompra.setCantidad(resultSet.getInt("lc_cantidad"));
        int idProducto = resultSet.getInt("lc_producto_id");
        ProductoDaoMySQL productoDao = new ProductoDaoMySQL();
        lineaCompra.setProducto(productoDao.buscar(idProducto));
        lineaCompra.setPrecioUnitario(resultSet.getDouble("lc_precio_unitario"));

        return lineaCompra;
    }

    private Compra mapResultSetToCompra(ResultSet resultSet) throws SQLException {
        Compra compra = new Compra();
        compra.setId(resultSet.getInt(COLUMN_ID));
        compra.setId_registroCompra(resultSet.getInt(COLUMN_REGISTRO_COMPRA_ID));

        // Obtener el ID del proveedor desde el ResultSet
        int idProveedor = resultSet.getInt("proveedor_id");

        // Obtener el proveedor a través de su ID
        ProveedorDaoMySQL proveedorDao = new ProveedorDaoMySQL();
        Proveedor proveedor = proveedorDao.buscar(idProveedor);

        // Establecer el proveedor en la compra
        compra.setProveedor(proveedor);

        return compra;
    }
}
