
package datos;

import dominio.Cliente;
import dominio.EstadoReserva;
import dominio.Habitacion;
import dominio.RegistroReserva;
import dominio.Reserva;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReservaDaoMySQL extends Dao<Reserva> {

    private static final String TABLE_NAME = "reserva";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_REGISTRO_RESERVA_ID = "registro_reserva_id";
    private static final String COLUMN_CLIENTE_ID = "cliente_id";
    private static final String COLUMN_HABITACION_ID = "habitacion_id";
    private static final String COLUMN_NUMERO_HUESPEDES = "numero_huespedes";
    private static final String COLUMN_ESTADO_RESERVA = "estado_reserva";
    private static final String COLUMN_HORA_INGRESO = "hora_ingreso";
    private static final String COLUMN_HORA_SALIDA = "hora_salida";
    private static final String COLUMN_HORA_RESERVADAS = "hora_reservadas";
    private static final String COLUMN_COSTO_EFECTIVO = "costo_efectivo";
    private static final String COLUMN_COSTO_YAPE = "costo_yape";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_REGISTRO_RESERVA_ID + ", " + COLUMN_CLIENTE_ID + ", " +
            COLUMN_HABITACION_ID + ", " + COLUMN_NUMERO_HUESPEDES + ", " + COLUMN_ESTADO_RESERVA + ", " + COLUMN_HORA_INGRESO + ", " +
            COLUMN_HORA_SALIDA + ", " + COLUMN_HORA_RESERVADAS + ", " + COLUMN_COSTO_EFECTIVO + ", " + COLUMN_COSTO_YAPE + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_REGISTRO_RESERVA_ID + " = ?, " + COLUMN_CLIENTE_ID + " = ?, " +
            COLUMN_HABITACION_ID + " = ?, " + COLUMN_NUMERO_HUESPEDES + " = ?, " + COLUMN_ESTADO_RESERVA + " = ?, " + COLUMN_HORA_INGRESO + " = ?, " +
            COLUMN_HORA_SALIDA + " = ?, " + COLUMN_HORA_RESERVADAS + " = ?, " + COLUMN_COSTO_EFECTIVO + " = ?, " + COLUMN_COSTO_YAPE + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Reserva crear(Reserva obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, obj.getRegistroreserva_id());
            preparedStatement.setInt(2, obj.getCliente().getId());
            preparedStatement.setInt(3, obj.getHabitacion().getId());
            preparedStatement.setInt(4, obj.getNumeroHuespedes());
            preparedStatement.setString(5, obj.getEstadoReserva().name());
            preparedStatement.setString(6, obj.getHora_ingreso());
            preparedStatement.setString(7, obj.getHora_salida());
            preparedStatement.setString(8, obj.getHoras_reservadas());
            preparedStatement.setDouble(9, obj.getCostoEfectivo());
            preparedStatement.setDouble(10, obj.getCostoYape());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating reserva failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating reserva failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    public Reserva eliminar(Reserva obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Reserva actualizar(Reserva obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setInt(1, obj.getRegistroreserva_id());
            preparedStatement.setInt(2, obj.getCliente().getId());
            preparedStatement.setInt(3, obj.getHabitacion().getId());
            preparedStatement.setInt(4, obj.getNumeroHuespedes());
            preparedStatement.setString(5, obj.getEstadoReserva().name());
            preparedStatement.setString(6, obj.getHora_ingreso());
            preparedStatement.setString(7, obj.getHora_salida());
            preparedStatement.setString(8, obj.getHoras_reservadas());
            preparedStatement.setDouble(9, obj.getCostoEfectivo());
            preparedStatement.setDouble(10, obj.getCostoYape());
            preparedStatement.setInt(11, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Reserva buscar(int id) {
        Reserva reserva = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    reserva = mapResultSetToReserva(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reserva;
    }

    @Override
    public List<Reserva> listado() {
        List<Reserva> listaReservas = new ArrayList<>();

        try (Statement statement = connect.createStatement(); ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            while (resultSet.next()) {
                Reserva reserva = mapResultSetToReserva(resultSet);
                listaReservas.add(reserva);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaReservas;
    }

    private Reserva mapResultSetToReserva(ResultSet resultSet) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setId(resultSet.getInt(COLUMN_ID));
        reserva.setRegistroreserva_id(resultSet.getInt(COLUMN_REGISTRO_RESERVA_ID));
        reserva.setNumeroHuespedes(resultSet.getInt(COLUMN_NUMERO_HUESPEDES));
        reserva.setCostoEfectivo(resultSet.getDouble(COLUMN_COSTO_EFECTIVO));
        reserva.setCostoYape(resultSet.getDouble(COLUMN_COSTO_YAPE));
        reserva.setHoras_reservadas(resultSet.getString(COLUMN_HORA_RESERVADAS));
        reserva.setHora_ingreso(resultSet.getString(COLUMN_HORA_INGRESO));
        reserva.setHora_salida(resultSet.getString(COLUMN_HORA_SALIDA));
        reserva.setEstadoReserva(EstadoReserva.fromString(resultSet.getString(COLUMN_ESTADO_RESERVA)));

        // Obtener el cliente asociado a la reserva
        int idCliente = resultSet.getInt(COLUMN_CLIENTE_ID); // Corregir aquí
        ClienteDaoMySQL clienteDao = new ClienteDaoMySQL();
        Cliente cliente = clienteDao.buscar(idCliente);
        reserva.setCliente(cliente);

        // Obtener la habitación asociada a la reserva
        int idHabitacion = resultSet.getInt(COLUMN_HABITACION_ID); // Corregir aquí
        HabitacionDaoMySQL habitacionDao = new HabitacionDaoMySQL();
        Habitacion habitacion = habitacionDao.buscar(idHabitacion);
        reserva.setHabitacion(habitacion);

        return reserva;
    }
    
    

    // Nuevo método para obtener las reservas por fecha
    /*public List<Reserva> obtenerReservasPorFecha(String fecha) {
        List<Reserva> reservasPorFecha = new ArrayList<>();

        try {
            // Utilizar PreparedStatement para evitar problemas de SQL injection
            String sql = "SELECT r.*, rr.fecha AS registro_reserva_fecha "
                    + "FROM " + TABLE_NAME + " r "
                    + "JOIN registro_reserva rr ON r.registro_reserva_id = rr.id "
                    + "WHERE rr.fecha = ?";

            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1, fecha);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Reserva reserva = mapResultSetToReserva(resultSet);

                        // Agregar la fecha del registro_reserva
                        String registroReservaFecha = resultSet.getString("registro_reserva_fecha");
                        reserva.setFechaRegistroReserva(registroReservaFecha);

                        reservasPorFecha.add(reserva);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reservasPorFecha;
    }*/
}
