package datos;

import dominio.Cliente;
import dominio.Empleado;
import dominio.RegistroVenta;
import dominio.Turno;
import dominio.Venta;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegistroVentaDaoMySQL extends Dao<RegistroVenta> {

    private static final String TABLE_NAME = "registro_venta";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMPLEADO_ID = "empleado_id";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_TURNO = "turno";
    private static final String COLUMN_LIQUIDACION = "liquidacion";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME +
            " (" + COLUMN_EMPLEADO_ID + ", " + COLUMN_FECHA + ", " + COLUMN_TURNO + ", " + COLUMN_LIQUIDACION + ") VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME +
            " SET " + COLUMN_EMPLEADO_ID + " = ?, " + COLUMN_FECHA + " = ?, " + COLUMN_TURNO + " = ?, " + COLUMN_LIQUIDACION + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL_WITH_VENTAS =
            "SELECT rv.*, v.id AS v_id, v.cliente_id AS v_cliente_id, v.registro_venta_id AS registro_venta_id " +
            "FROM registro_venta rv " +
            "JOIN venta v ON rv.id = v.registro_venta_id";
    private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public RegistroVenta crear(RegistroVenta obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, obj.getEmpleado().getId());
            preparedStatement.setString(2, obj.getFecha());
            preparedStatement.setString(3, obj.getTurno().toString());
            preparedStatement.setDouble(4, obj.getLiquidacion());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating registro_venta failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating registro_venta failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroVenta eliminar(RegistroVenta obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroVenta actualizar(RegistroVenta obj) {
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
    public RegistroVenta buscar(int id) {
        RegistroVenta registroVenta = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    registroVenta = mapResultSetToRegistroVenta(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return registroVenta;
    }

    @Override
    public List<RegistroVenta> listado() {
        List<RegistroVenta> listaRegistroVentas = new ArrayList<>();
        List<Venta> listaVentas = new ArrayList<>();

        try (Statement statement = connect.createStatement(); ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_WITH_VENTAS)) {

            while (resultSet.next()) {
                RegistroVenta registroVenta = mapResultSetToRegistroVenta(resultSet);
                Venta venta = mapResultSetToVenta(resultSet);

                if (!listaVentas.contains(venta)) {
                    // Si la venta no está en la lista, agrégala
                    listaVentas.add(venta);
                }

                // Agregar el registroVenta a la lista
                listaRegistroVentas.add(registroVenta);
            }

            // Asigna las ventas a los registros de venta correspondientes
            for (RegistroVenta registroVenta : listaRegistroVentas) {
                List<Venta> ventas = new ArrayList<>();
                for (Venta venta : listaVentas) {
                    if (venta.getId_registroVenta()== registroVenta.getId()) {
                        ventas.add(venta);
                    }
                }
                registroVenta.setListVentas((ArrayList<Venta>) ventas);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaRegistroVentas;
    }

    private RegistroVenta mapResultSetToRegistroVenta(ResultSet resultSet) throws SQLException {
        RegistroVenta registroVenta = new RegistroVenta();
        registroVenta.setId(resultSet.getInt(COLUMN_ID));
        registroVenta.setFecha(resultSet.getString(COLUMN_FECHA));
        registroVenta.setLiquidacion(resultSet.getDouble(COLUMN_LIQUIDACION));
        registroVenta.setTurno(Turno.valueOf(resultSet.getString(COLUMN_TURNO)));

        // Obtener el ID del empleado desde el ResultSet
        int idEmpleado = resultSet.getInt(COLUMN_EMPLEADO_ID);

        // Obtener el empleado a través de su ID
        EmpleadoDaoMySQL empleadoDao = new EmpleadoDaoMySQL();
        Empleado empleado = empleadoDao.buscar(idEmpleado);

        // Establecer el empleado en el registroVenta
        registroVenta.setEmpleado(empleado);

        return registroVenta;
    }

    private Venta mapResultSetToVenta(ResultSet resultSet) throws SQLException {
        Venta venta = new Venta();
        venta.setId(resultSet.getInt("v_id"));
        venta.setId_registroVenta(resultSet.getInt("registro_venta_id"));
        // Obtener el ID del cliente desde el ResultSet
        int idCliente = resultSet.getInt("v_cliente_id");
        // Obtener el cliente a través de su ID
        ClienteDaoMySQL clienteDao = new ClienteDaoMySQL();
        Cliente cliente = clienteDao.buscar(idCliente);
        // Establecer el cliente en la venta
        venta.setCliente(cliente);

        return venta;
    }
}
