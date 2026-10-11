package datos;

import dominio.Cliente;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoMySQL extends Dao<Cliente> {

    public ClienteDaoMySQL() {
    }

    private static final String TABLE_NAME = "cliente";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DNI = "dni";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_APELLIDO = "apellido";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_DNI + ", " + COLUMN_NOMBRE + ", " +
            COLUMN_APELLIDO + ") VALUES (?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_DNI + " = ?, " +
            COLUMN_NOMBRE + " = ?, " + COLUMN_APELLIDO + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Cliente crear(Cliente obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, obj.getDni());
            preparedStatement.setString(2, obj.getNombre());
            preparedStatement.setString(3, obj.getApellido());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating cliente failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating cliente failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public Cliente eliminar(Cliente obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Cliente actualizar(Cliente obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, obj.getDni());
            preparedStatement.setString(2, obj.getNombre());
            preparedStatement.setString(3, obj.getApellido());
            preparedStatement.setInt(4, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Cliente buscar(int id) {
        Cliente cliente = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    cliente = mapResultSetToCliente(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return cliente;
    }

    @Override
    public List<Cliente> listado() {
        List<Cliente> listaClientes = new ArrayList<>();

        try (Statement statement = connect.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                Cliente cliente = mapResultSetToCliente(resultSet);
                listaClientes.add(cliente);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaClientes;
    }

    private Cliente mapResultSetToCliente(ResultSet resultSet) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(resultSet.getInt(COLUMN_ID));
        cliente.setDni(resultSet.getString(COLUMN_DNI));
        cliente.setNombre(resultSet.getString(COLUMN_NOMBRE));
        cliente.setApellido(resultSet.getString(COLUMN_APELLIDO));
        return cliente;
    }
}
