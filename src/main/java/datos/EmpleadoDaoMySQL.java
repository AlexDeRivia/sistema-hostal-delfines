package datos;

import dominio.Empleado;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDaoMySQL extends Dao<Empleado> {

    public EmpleadoDaoMySQL() {
    }

    private static final String TABLE_NAME = "empleado";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DNI = "dni";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_APELLIDO = "apellido";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_CLAVE = "clave";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_DNI + ", " + COLUMN_NOMBRE + ", " +
            COLUMN_APELLIDO + ", " + COLUMN_USERNAME + ", " + COLUMN_CLAVE + ") VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_DNI + " = ?, " + COLUMN_NOMBRE + " = ?, " +
            COLUMN_APELLIDO + " = ?, " + COLUMN_USERNAME + " = ?, " + COLUMN_CLAVE + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Empleado crear(Empleado obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, obj.getDni());
            preparedStatement.setString(2, obj.getNombre());
            preparedStatement.setString(3, obj.getApellido());
            preparedStatement.setString(4, obj.getUsername());
            preparedStatement.setString(5, obj.getClave());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating empleado failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating empleado failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public Empleado eliminar(Empleado obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Empleado actualizar(Empleado obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, obj.getDni());
            preparedStatement.setString(2, obj.getNombre());
            preparedStatement.setString(3, obj.getApellido());
            preparedStatement.setString(4, obj.getUsername());
            preparedStatement.setString(5, obj.getClave());
            preparedStatement.setInt(6, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Empleado buscar(int id) {
        Empleado empleado = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    empleado = mapResultSetToEmpleado(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return empleado;
    }

    @Override
    public List<Empleado> listado() {
        List<Empleado> listaEmpleados = new ArrayList<>();

        try (Statement statement = connect.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                Empleado empleado = mapResultSetToEmpleado(resultSet);
                listaEmpleados.add(empleado);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaEmpleados;
    }

    private Empleado mapResultSetToEmpleado(ResultSet resultSet) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setId(resultSet.getInt(COLUMN_ID));
        empleado.setDni(resultSet.getString(COLUMN_DNI));
        empleado.setNombre(resultSet.getString(COLUMN_NOMBRE));
        empleado.setApellido(resultSet.getString(COLUMN_APELLIDO));
        empleado.setUsername(resultSet.getString(COLUMN_USERNAME));
        empleado.setClave(resultSet.getString(COLUMN_CLAVE));
        return empleado;
    }
}
    
