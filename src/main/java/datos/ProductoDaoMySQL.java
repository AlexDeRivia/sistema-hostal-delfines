package datos;

import dominio.Categoria;
import dominio.Producto;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDaoMySQL extends Dao<Producto> {

    private static final String TABLE_NAME = "producto";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_STOCK = "stock";
    private static final String COLUMN_PRECIO_VENTA = "precio_venta";
    private static final String COLUMN_PRECIO_COMPRA = "precio_compra";
    private static final String COLUMN_CATEGORIA = "categoria";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NOMBRE + ", " + COLUMN_STOCK + ", " +
            COLUMN_PRECIO_VENTA + ", " + COLUMN_PRECIO_COMPRA + ", " + COLUMN_CATEGORIA + ") VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_NOMBRE + " = ?, " + COLUMN_STOCK + " = ?, " +
            COLUMN_PRECIO_VENTA + " = ?, " + COLUMN_PRECIO_COMPRA + " = ?, " + COLUMN_CATEGORIA + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Producto crear(Producto obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, obj.getNombre());
            preparedStatement.setInt(2, obj.getStock());
            preparedStatement.setDouble(3, obj.getPrecioVenta());
            preparedStatement.setDouble(4, obj.getPrecioCompra());
            preparedStatement.setString(5, obj.getCategoria().name());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating producto failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating producto failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public Producto eliminar(Producto obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Producto actualizar(Producto obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, obj.getNombre());
            preparedStatement.setInt(2, obj.getStock());
            preparedStatement.setDouble(3, obj.getPrecioVenta());
            preparedStatement.setDouble(4, obj.getPrecioCompra());
            preparedStatement.setString(5, obj.getCategoria().name());
            preparedStatement.setInt(6, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Producto buscar(int id) {
        Producto producto = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    producto = mapResultSetToProducto(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return producto;
    }

    @Override
    public List<Producto> listado() {
        List<Producto> listaProductos = new ArrayList<>();

        try (Statement statement = connect.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                Producto producto = mapResultSetToProducto(resultSet);
                listaProductos.add(producto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaProductos;
    }

    private Producto mapResultSetToProducto(ResultSet resultSet) throws SQLException {
        Producto producto = new Producto();
        producto.setId(resultSet.getInt(COLUMN_ID));
        producto.setNombre(resultSet.getString(COLUMN_NOMBRE));
        producto.setStock(resultSet.getInt(COLUMN_STOCK));
        producto.setPrecioVenta(resultSet.getDouble(COLUMN_PRECIO_VENTA));
        producto.setPrecioCompra(resultSet.getDouble(COLUMN_PRECIO_COMPRA));
        producto.setCategoria(Categoria.valueOf(resultSet.getString(COLUMN_CATEGORIA)));
        return producto;
    }
}
