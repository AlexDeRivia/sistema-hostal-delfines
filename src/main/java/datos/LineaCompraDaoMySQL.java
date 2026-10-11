package datos;

import dominio.LineaCompra;
import dominio.Producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LineaCompraDaoMySQL extends Dao<LineaCompra>{

    private static final String TABLE_NAME = "linea_compra";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COMPRA_ID = "compra_id";
    private static final String COLUMN_PRODUCTO_ID = "producto_id";
    private static final String COLUMN_PRECIO_UNITARIO = "precio_unitario";
    private static final String COLUMN_CANTIDAD = "cantidad";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_COMPRA_ID + ", " + COLUMN_PRODUCTO_ID + ", " +
            COLUMN_PRECIO_UNITARIO + ", " + COLUMN_CANTIDAD + ") VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_COMPRA_ID + " = ?, " + COLUMN_PRODUCTO_ID + " = ?, " +
            COLUMN_PRECIO_UNITARIO + " = ?, " + COLUMN_CANTIDAD + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public LineaCompra crear(LineaCompra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, obj.getCompra_id());
            preparedStatement.setInt(2, obj.getProducto().getId());
            preparedStatement.setDouble(3, obj.getPrecioUnitario());
            preparedStatement.setInt(4, obj.getCantidad());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating linea_compra failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));

                    // Actualizar el stock del producto
                    try (PreparedStatement updateStockStatement = connect.prepareStatement(
                            "UPDATE producto SET stock = stock + ? WHERE id = ?")) {
                        updateStockStatement.setInt(1, obj.getCantidad());
                        updateStockStatement.setInt(2, obj.getProducto().getId());

                        int stockRowsAffected = updateStockStatement.executeUpdate();
                        if (stockRowsAffected == 0) {
                            throw new SQLException("Updating stock failed, no rows affected.");
                        }
                    }
                } else {
                    throw new SQLException("Creating linea_compra failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public LineaCompra eliminar(LineaCompra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public LineaCompra actualizar(LineaCompra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setInt(1, obj.getCompra_id());
            preparedStatement.setInt(2, obj.getProducto().getId());
            preparedStatement.setDouble(3, obj.getPrecioUnitario());
            preparedStatement.setInt(4, obj.getCantidad());
            preparedStatement.setInt(5, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public LineaCompra buscar(int id) {
        LineaCompra lineaCompra = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    lineaCompra = mapResultSetToLineaCompra(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lineaCompra;
    }

    @Override
    public List<LineaCompra> listado() {
        List<LineaCompra> listaLineaCompras = new ArrayList<>();

        try (Statement statement = connect.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                LineaCompra lineaCompra = mapResultSetToLineaCompra(resultSet);
                listaLineaCompras.add(lineaCompra);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaLineaCompras;
    }

    private LineaCompra mapResultSetToLineaCompra(ResultSet resultSet) throws SQLException {
        LineaCompra lineaCompra = new LineaCompra();
        lineaCompra.setId(resultSet.getInt(COLUMN_ID));
        lineaCompra.setCompra_id(resultSet.getInt(COLUMN_COMPRA_ID));
        lineaCompra.setProducto(new Producto(resultSet.getInt(COLUMN_PRODUCTO_ID)));  // Asegúrate de tener un constructor que acepte el ID
        lineaCompra.setPrecioUnitario(resultSet.getDouble(COLUMN_PRECIO_UNITARIO));
        lineaCompra.setCantidad(resultSet.getInt(COLUMN_CANTIDAD));

        return lineaCompra;
    }
}
