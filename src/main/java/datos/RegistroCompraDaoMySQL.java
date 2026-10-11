package datos;

import dominio.Compra;
import dominio.Empleado;
import dominio.Proveedor;
import dominio.RegistroCompra;
import dominio.Turno;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegistroCompraDaoMySQL extends Dao<RegistroCompra> {

    private static final String TABLE_NAME = "registro_compra";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMPLEADO_ID = "empleado_id";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_TURNO = "turno";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME +
            " (" + COLUMN_EMPLEADO_ID + ", " + COLUMN_FECHA + ", " + COLUMN_TURNO + ") VALUES (?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME +
            " SET " + COLUMN_EMPLEADO_ID + " = ?, " + COLUMN_FECHA + " = ?, " + COLUMN_TURNO + " = ? WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
    private static final String SQL_SELECT_ALL_WITH_COMPRAS =
            "SELECT rc.*, c.id AS c_id, c.registro_compra_id AS c_registro_compra_id, c.proveedor_id AS c_proveedor_id " +
            "FROM registro_compra rc " +
            "JOIN compra c ON rc.id = c.registro_compra_id";
    //private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public RegistroCompra crear(RegistroCompra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, obj.getEmpleado().getId()); // Asegúrate de tener un método getId() en la clase Empleado
            preparedStatement.setString(2, obj.getFecha());
            preparedStatement.setString(3, obj.getTurno().toString());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating registro_compra failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating registro_compra failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroCompra eliminar(RegistroCompra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_DELETE)) {
            preparedStatement.setInt(1, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroCompra actualizar(RegistroCompra obj) {
        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_UPDATE)) {
            preparedStatement.setInt(1, obj.getEmpleado().getId()); // Asegúrate de tener un método getId() en la clase Empleado
            preparedStatement.setString(2, obj.getFecha());
            preparedStatement.setString(3, obj.getTurno().toString());
            preparedStatement.setInt(4, obj.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroCompra buscar(int id) {
        RegistroCompra registroCompra = null;

        try (PreparedStatement preparedStatement = connect.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    registroCompra = mapResultSetToRegistroCompra(resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return registroCompra;
    }

    @Override
    public List<RegistroCompra> listado() {
        List<RegistroCompra> listaRegistroCompras = new ArrayList<>();
        List<Compra> listaCompras = new ArrayList<>();

        try (Statement statement = connect.createStatement(); ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_WITH_COMPRAS)) {

            while (resultSet.next()) {
                RegistroCompra registroCompra = mapResultSetToRegistroCompra(resultSet);
                Compra compra = mapResultSetToCompra(resultSet);
                
                if (!listaCompras.contains(compra)) {
                    // Si la venta no está en la lista, agrégala
                    listaCompras.add(compra);
                }

                // Agregar el registroCompra a la lista
                listaRegistroCompras.add(registroCompra);
            }

            // Asigna las compras a los registros de compra correspondientes
            for (RegistroCompra registroCompra : listaRegistroCompras) {
                List<Compra> compras = new ArrayList<>();
                for (Compra compra : listaCompras) {
                    if (compra.getId_registroCompra()== registroCompra.getId()) {
                        compras.add(compra);
                    }
                }
                registroCompra.setListCompras((ArrayList<Compra>) compras);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listaRegistroCompras;
    }

    private RegistroCompra mapResultSetToRegistroCompra(ResultSet resultSet) throws SQLException {
        RegistroCompra registroCompra = new RegistroCompra();
        registroCompra.setId(resultSet.getInt(COLUMN_ID));
        // Aquí deberías cargar el Empleado y Turno a partir de sus IDs
        // Asumo que hay métodos getEmpleadoById y getTurnoByNombre en tus Daos respectivos
        EmpleadoDaoMySQL empleadoDao = new EmpleadoDaoMySQL();
        Empleado empleado = empleadoDao.buscar(resultSet.getInt(COLUMN_EMPLEADO_ID));
        registroCompra.setEmpleado(empleado);
        registroCompra.setFecha(resultSet.getString(COLUMN_FECHA));
        registroCompra.setTurno(Turno.valueOf(resultSet.getString(COLUMN_TURNO)));
        return registroCompra;
    }
    
    private Compra mapResultSetToCompra(ResultSet resultSet) throws SQLException {
        Compra compra = new Compra();
        compra.setId(resultSet.getInt("c_id"));
        compra.setId_registroCompra(resultSet.getInt("c_registro_compra_id"));
        // Obtener el ID del proveedor desde el ResultSet
        int idProveedor = resultSet.getInt("c_proveedor_id");
        // Obtener el proveedor a través de su ID
        ProveedorDaoMySQL proveedorDao = new ProveedorDaoMySQL();
        Proveedor proveedor = proveedorDao.buscar(idProveedor);
        // Establecer el proveedor en la compra
        compra.setProveedor(proveedor);

        return compra;
    }
}
