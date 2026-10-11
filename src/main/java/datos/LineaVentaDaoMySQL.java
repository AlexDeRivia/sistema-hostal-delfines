package datos;

import dominio.LineaVenta;
import dominio.Producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LineaVentaDaoMySQL extends Dao<LineaVenta> {

    private static final String TABLE_NAME = "linea_venta";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_VENTA_ID = "venta_id";
    private static final String COLUMN_PRODUCTO_ID = "producto_id";
    private static final String COLUMN_PRECIO_UNITARIO = "precio_unitario";
    private static final String COLUMN_CANTIDAD = "cantidad";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_VENTA_ID + ", " + COLUMN_PRODUCTO_ID + ", " +
            COLUMN_PRECIO_UNITARIO + ", " + COLUMN_CANTIDAD + ") VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_VENTA_ID + " = ?, " + COLUMN_PRODUCTO_ID + " = ?, " +
            COLUMN_PRECIO_UNITARIO + " = ?, " + COLUMN_CANTIDAD + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public LineaVenta crear(LineaVenta obj) {
        try (PreparedStatement statement = connect.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, obj.getVenta_id());
            statement.setInt(2, obj.getProducto().getId());
            statement.setDouble(3, obj.getPrecioUnitario());
            statement.setInt(4, obj.getCantidad());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating LineaVenta failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));

                    // Actualizar el stock del producto
                    try (PreparedStatement updateStockStatement = connect.prepareStatement(
                            "UPDATE producto SET stock = stock - ? WHERE id = ?")) {
                        updateStockStatement.setInt(1, obj.getCantidad());
                        updateStockStatement.setInt(2, obj.getProducto().getId());

                        int stockRowsAffected = updateStockStatement.executeUpdate();
                        if (stockRowsAffected == 0) {
                            throw new SQLException("Updating stock failed, no rows affected.");
                        }
                    }
                } else {
                    throw new SQLException("Creating LineaVenta failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public LineaVenta eliminar(LineaVenta obj) {
        try (PreparedStatement statement = connect.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, obj.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public LineaVenta actualizar(LineaVenta obj) {
        try (PreparedStatement statement = connect.prepareStatement(SQL_UPDATE)) {
            statement.setInt(1, obj.getVenta_id());
            statement.setInt(2, obj.getProducto().getId());
            statement.setDouble(3, obj.getPrecioUnitario());
            statement.setInt(4, obj.getCantidad());
            statement.setInt(5, obj.getId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public LineaVenta buscar(int id) {
        LineaVenta lineaVenta = null;

        try (PreparedStatement statement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lineaVenta = mapResultSetToLineaVenta(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lineaVenta;
    }

    @Override
    public List<LineaVenta> listado() {
        List<LineaVenta> listaLineasVenta = new ArrayList<>();

        try (PreparedStatement statement = connect.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                LineaVenta lineaVenta = mapResultSetToLineaVenta(resultSet);
                listaLineasVenta.add(lineaVenta);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaLineasVenta;
    }

    private LineaVenta mapResultSetToLineaVenta(ResultSet resultSet) throws SQLException {
        LineaVenta lineaVenta = new LineaVenta();
        lineaVenta.setId(resultSet.getInt(COLUMN_ID));
        lineaVenta.setVenta_id(resultSet.getInt(COLUMN_VENTA_ID));
        //int productoId = resultSet.getInt(COLUMN_PRODUCTO_ID);
        
        // Aquí deberías cargar el Producto a partir de su ID
        /*ProductoDaoMySQL productoDao = new ProductoDaoMySQL();
        Producto producto = productoDao.buscar(productoId);
        lineaVenta.setProducto(producto);*/
        lineaVenta.setProducto(new Producto(resultSet.getInt(COLUMN_PRODUCTO_ID)));

        lineaVenta.setPrecioUnitario(resultSet.getDouble(COLUMN_PRECIO_UNITARIO));
        lineaVenta.setCantidad(resultSet.getInt(COLUMN_CANTIDAD));

        return lineaVenta;
    }
}