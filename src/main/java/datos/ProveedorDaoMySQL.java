package datos;

import dominio.Proveedor;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDaoMySQL extends Dao<Proveedor> {

    private static final String TABLE_NAME = "proveedor";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RUC = "ruc";
    private static final String COLUMN_RAZON_SOCIAL = "razon_social";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_RUC + ", " + COLUMN_RAZON_SOCIAL + ") VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_RUC + " = ?, " +
            COLUMN_RAZON_SOCIAL + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Proveedor crear(Proveedor obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, obj.getRuc());
            preparedStatement.setString(2, obj.getRazonSocial());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating proveedor failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating proveedor failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public Proveedor eliminar(Proveedor obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Proveedor actualizar(Proveedor obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, obj.getRuc());
            preparedStatement.setString(2, obj.getRazonSocial());
            preparedStatement.setInt(3, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Proveedor buscar(int id) {
        Proveedor proveedor = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    proveedor = mapResultSetToProveedor(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return proveedor;
    }

    @Override
    public List<Proveedor> listado() {
        List<Proveedor> listaProveedores = new ArrayList<>();

        try (Statement statement = connect.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                Proveedor proveedor = mapResultSetToProveedor(resultSet);
                listaProveedores.add(proveedor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaProveedores;
    }

    private Proveedor mapResultSetToProveedor(ResultSet resultSet) throws SQLException {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(resultSet.getInt(COLUMN_ID));
        proveedor.setRuc(resultSet.getString(COLUMN_RUC));
        proveedor.setRazonSocial(resultSet.getString(COLUMN_RAZON_SOCIAL));
        return proveedor;
    }
}
