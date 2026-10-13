package datos;

import dominio.Empleado;
import dominio.RegistroVenta;
import dominio.Turno;
import dominio.Venta;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class RegistroVentaDaoAT extends Dao<RegistroVenta>{
    
    @Override
    public RegistroVenta crear(RegistroVenta obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "RegistroVentas.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos del registro de venta
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            obj.setId(id);
            String fecha = obj.getFecha();
            double liquidacion = obj.getLiquidacion();
            Turno turno = obj.getTurno();
            Empleado empleado = obj.getEmpleado();

            // Construir la línea para escribir en el archivo
            String line = id + "," + fecha + "," + liquidacion + "," + turno + "," + empleado.getId();

            // Escribir la línea en el archivo
            bw.write(line);
            bw.newLine();

            bw.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return obj;
    }

    @Override
    public RegistroVenta eliminar(RegistroVenta obj) {
        List<RegistroVenta> registrosVenta = listado();

        // Implementa la lógica para eliminar el registro de venta
        registrosVenta.removeIf(registroVenta -> registroVenta.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirRegistrosVentaEnArchivo(registrosVenta);

        return obj;
    }

    @Override
    public RegistroVenta actualizar(RegistroVenta obj) {
        List<RegistroVenta> registrosVenta = listado();

        // Implementa la lógica para actualizar el registro de venta
        for (int i = 0; i < registrosVenta.size(); i++) {
            if (registrosVenta.get(i).getId() == obj.getId()) {
                registrosVenta.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirRegistrosVentaEnArchivo(registrosVenta);

        return obj;
    }

    @Override
    public RegistroVenta buscar(int id) {
        List<RegistroVenta> registrosVenta = listado();

        // Implementa la lógica para buscar el registro de venta por ID
        for (RegistroVenta registroVenta : registrosVenta) {
            if (registroVenta.getId() == id) {
                return registroVenta;
            }
        }

        return null; // Registro de venta no encontrado
    }

    @Override
    public List<RegistroVenta> listado() {
        List<RegistroVenta> listaRegistrosVenta = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "RegistroVentas.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 5) { // Asegurarse de que la línea tenga el formato correcto
                    RegistroVenta registroVenta = new RegistroVenta();
                    registroVenta.setId(Integer.parseInt(partes[0]));
                    registroVenta.setFecha(partes[1]);
                    registroVenta.setLiquidacion(Double.parseDouble(partes[2]));
                    try {
                        registroVenta.setTurno(Turno.valueOf(partes[3]));
                    } catch (IllegalArgumentException e) {
                        // Manejar la excepción, por ejemplo, imprimir un mensaje de error
                        System.err.println("Error al convertir el nombre del turno a un valor de enum Turno: " + e.getMessage());
                    }
                    
                    int idEmpleado = Integer.parseInt(partes[4]);
                    EmpleadoDaoAT empleadoDao = new EmpleadoDaoAT(); // Asume que existe un DAO para Empleado
                    Empleado empleado = empleadoDao.buscar(idEmpleado);
                    if (empleado == null) {
                        empleado = new Empleado();
                        empleado.setId(idEmpleado);
                    }
                    registroVenta.setEmpleado(empleado);

                    VentaDaoAT ventaDao = new VentaDaoAT();
                    List<Venta> listVentas = ventaDao.listadoPorRegistroVenta(registroVenta.getId());
                    registroVenta.setListVentas((ArrayList<Venta>) listVentas);

                    listaRegistrosVenta.add(registroVenta);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaRegistrosVenta;
    }

    private void escribirRegistrosVentaEnArchivo(List<RegistroVenta> registrosVenta) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "RegistroVentas.txt"))) {
            for (RegistroVenta registroVenta : registrosVenta) {
                String line = registroVenta.getId() + "," + registroVenta.getFecha() + "," +
                        registroVenta.getLiquidacion() + "," + registroVenta.getTurno() + "," +
                        registroVenta.getEmpleado().getId();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private String obtenerIdVentas(List<Venta> listVentas) {
        StringBuilder ids = new StringBuilder();
        for (Venta venta : listVentas) {
            ids.append(venta.getId()).append(",");
        }
        // Elimina la última coma si hay ventas
        if (ids.length() > 0) {
            ids.deleteCharAt(ids.length() - 1);
        }
        return ids.toString();
    }

    public int obtenerUltimoId() {
        List<RegistroVenta> registrosVenta = listado();

        if (!registrosVenta.isEmpty()) {
            RegistroVenta ultimoRegistroVenta = registrosVenta.get(registrosVenta.size() - 1);
            return ultimoRegistroVenta.getId();
        } else {
            return 0; // Otra lógica si no hay registros de venta
        }
    }
}
