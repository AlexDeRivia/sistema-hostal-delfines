package datos;

import dominio.Cliente;
import dominio.Empleado;
import dominio.EstadoReserva;
import dominio.Habitacion;
import dominio.RegistroReserva;
import dominio.Reserva;
import dominio.Turno;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegistroReservaDaoMySQL extends Dao<RegistroReserva> {

    private static final String TABLE_NAME = "registro_reserva";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMPLEADO_ID = "empleado_id";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_TURNO = "turno";
    private static final String COLUMN_LIQUIDACION = "liquidacion";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME +
            " (" + COLUMN_EMPLEADO_ID + ", " + COLUMN_FECHA + ", " + COLUMN_TURNO + ", " + COLUMN_LIQUIDACION + ") " +
            "VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME +
            " SET " + COLUMN_EMPLEADO_ID + " = ?, " + COLUMN_FECHA + " = ?, " +
            COLUMN_TURNO + " = ?, " + COLUMN_LIQUIDACION + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL_WITH_VENTAS =
        "SELECT rr.*, r.id AS r_id, r.cliente_id AS r_cliente_id, r.habitacion_id AS r_habitacion_id, " +
        "r.numero_huespedes AS r_numero_huespedes, r.estado_reserva AS r_estado_reserva, " +
        "r.hora_ingreso AS r_hora_ingreso, r.hora_salida AS r_hora_salida, r.hora_reservadas AS r_hora_reservadas, r.costo_efectivo AS r_costo_efectivo, " +
        "r.costo_yape AS r_costo_yape, r.registro_reserva_id AS r_registro_reserva_id " +
        "FROM registro_reserva rr " +
        "JOIN reserva r ON rr.id = r.registro_reserva_id";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public RegistroReserva crear(RegistroReserva obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, obj.getEmpleado().getId());
            preparedStatement.setString(2, obj.getFecha());
            preparedStatement.setString(3, obj.getTurno().toString());
            preparedStatement.setDouble(4, obj.getLiquidacion());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating registro_reserva failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating registro_reserva failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroReserva eliminar(RegistroReserva obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroReserva actualizar(RegistroReserva obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setInt(1, obj.getEmpleado().getId());
            preparedStatement.setString(2, obj.getFecha());
            preparedStatement.setString(3, obj.getTurno().toString());
            preparedStatement.setDouble(4, obj.getLiquidacion());
            preparedStatement.setInt(5, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroReserva buscar(int id) {
        RegistroReserva registroReserva = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    registroReserva = mapResultSetToRegistroReserva(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return registroReserva;
    }

    @Override
    public List<RegistroReserva> listado() {
        List<RegistroReserva> listaRegistroReservas = new ArrayList<>();
        List<Reserva> listaReservas = new ArrayList<>();

        try (Statement statement = connect.createStatement(); ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_WITH_VENTAS)) {

            while (resultSet.next()) {
                RegistroReserva registroReserva = mapResultSetToRegistroReserva(resultSet);
                Reserva reserva = mapResultSetToReserva(resultSet);
                if (!listaReservas.contains(reserva)) {
                    listaReservas.add(reserva);
                }
                listaRegistroReservas.add(registroReserva);
            }
            for (RegistroReserva registroReserva : listaRegistroReservas) {
                List<Reserva> reservas = new ArrayList<>();
                for (Reserva reserva : listaReservas) {
                    if (reserva.getRegistroreserva_id()== registroReserva.getId()) {
                        reservas.add(reserva);
                    }
                }
                registroReserva.setListReservas((ArrayList<Reserva>) reservas);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaRegistroReservas;
    }

    private RegistroReserva mapResultSetToRegistroReserva(ResultSet resultSet) throws SQLException {
        RegistroReserva registroReserva = new RegistroReserva();
        registroReserva.setId(resultSet.getInt(COLUMN_ID));
        registroReserva.setFecha(resultSet.getString(COLUMN_FECHA));
        registroReserva.setLiquidacion(resultSet.getDouble(COLUMN_LIQUIDACION));
        registroReserva.setTurno(Turno.valueOf(resultSet.getString(COLUMN_TURNO)));

        // Asumo que existe un DAO para Empleado que proporciona el método buscar
        EmpleadoDaoMySQL empleadoDao = new EmpleadoDaoMySQL();
        Empleado empleado = empleadoDao.buscar(resultSet.getInt(COLUMN_EMPLEADO_ID));
        registroReserva.setEmpleado(empleado);

        // Puedes agregar la lógica para cargar la lista de reservas aquí si es necesario

        return registroReserva;
    }
    
    private Reserva mapResultSetToReserva(ResultSet resultSet) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setId(resultSet.getInt("r_id"));
        reserva.setRegistroreserva_id(resultSet.getInt("r_registro_reserva_id"));
        int idCliente = resultSet.getInt("r_cliente_id");
        ClienteDaoMySQL clienteDao = new ClienteDaoMySQL();
        Cliente cliente = clienteDao.buscar(idCliente);
        reserva.setCliente(cliente);
        
        int idHabitacion = resultSet.getInt("r_habitacion_id");
        HabitacionDaoMySQL habitacionDao = new HabitacionDaoMySQL();
        Habitacion habitacion = habitacionDao.buscar(idHabitacion);
        reserva.setHabitacion(habitacion);
        
        reserva.setNumeroHuespedes(resultSet.getInt("r_numero_huespedes"));
        reserva.setEstadoReserva(EstadoReserva.valueOf(resultSet.getString("r_estado_reserva")));
        reserva.setHora_ingreso(resultSet.getString("r_hora_ingreso"));
        reserva.setHora_salida(resultSet.getString("r_hora_salida"));
        reserva.setHoras_reservadas(resultSet.getString("r_hora_reservadas"));
        reserva.setCostoEfectivo(resultSet.getDouble("r_costo_efectivo"));
        reserva.setCostoYape(resultSet.getDouble("r_costo_yape"));

        return reserva;
    }
}
