package datos;

import dominio.Habitacion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDaoMySQL extends Dao<Habitacion> {

    private static final String TABLE_NAME = "habitacion";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NUMERO = "numero";
    private static final String COLUMN_NUMERO_CAMAS = "numero_camas";
    private static final String COLUMN_DISPONIBLE = "disponible";
    private static final String COLUMN_VENTANA = "ventana";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NUMERO + ", " + COLUMN_NUMERO_CAMAS + ", " +
            COLUMN_DISPONIBLE + ", " + COLUMN_VENTANA + ") VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_NUMERO + " = ?, " + COLUMN_NUMERO_CAMAS + " = ?, " +
            COLUMN_DISPONIBLE + " = ?, " + COLUMN_VENTANA + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Habitacion crear(Habitacion obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, obj.getNumero());
            preparedStatement.setInt(2, obj.getNumeroCamas());
            preparedStatement.setBoolean(3, obj.isDisponible());
            preparedStatement.setBoolean(4, obj.isVentanaAfuera());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating habitacion failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating habitacion failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public Habitacion eliminar(Habitacion obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Habitacion actualizar(Habitacion obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setInt(1, obj.getNumero());
            preparedStatement.setInt(2, obj.getNumeroCamas());
            preparedStatement.setBoolean(3, obj.isDisponible());
            preparedStatement.setBoolean(4, obj.isVentanaAfuera());
            preparedStatement.setInt(5, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Habitacion buscar(int id) {
        Habitacion habitacion = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    habitacion = mapResultSetToHabitacion(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return habitacion;
    }

    @Override
    public List<Habitacion> listado() {
        List<Habitacion> listaHabitaciones = new ArrayList<>();

        try (Statement statement = connect.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                Habitacion habitacion = mapResultSetToHabitacion(resultSet);
                listaHabitaciones.add(habitacion);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaHabitaciones;
    }

    private Habitacion mapResultSetToHabitacion(ResultSet resultSet) throws SQLException {
        Habitacion habitacion = new Habitacion();
        habitacion.setId(resultSet.getInt(COLUMN_ID));
        habitacion.setNumero(resultSet.getInt(COLUMN_NUMERO));
        habitacion.setNumeroCamas(resultSet.getInt(COLUMN_NUMERO_CAMAS));
        habitacion.setDisponible(resultSet.getBoolean(COLUMN_DISPONIBLE));
        habitacion.setVentanaAfuera(resultSet.getBoolean(COLUMN_VENTANA));
        return habitacion;
    }
}
