package datos;

import dominio.Compra;
import dominio.Empleado;
import dominio.RegistroCompra;
import dominio.Turno;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class RegistroCompraDaoAT extends Dao<RegistroCompra> {
    
    @Override
    public RegistroCompra crear(RegistroCompra obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "registroscompra.txt"), StandardOpenOption.APPEND);
            // Obtener los atributos del registro de compra
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            obj.setId(id);
            String fecha = obj.getFecha();
            Turno turno = obj.getTurno();
            Empleado empleado = obj.getEmpleado();

            // Construir la línea para escribir en el archivo
            String line = id + "," + fecha + "," + turno + "," + empleado.getId();

            // Escribir la línea en el archivo
            bw.write(line);
            bw.newLine();

            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public RegistroCompra eliminar(RegistroCompra obj) {
        List<RegistroCompra> registrosCompra = listado();

        // Implementa la lógica para eliminar el registro de compra
        registrosCompra.removeIf(registroCompra -> registroCompra.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirRegistrosCompraEnArchivo(registrosCompra);

        return obj;
    }

    @Override
    public RegistroCompra actualizar(RegistroCompra obj) {
        List<RegistroCompra> registrosCompra = listado();

        // Implementa la lógica para actualizar el registro de compra
        for (int i = 0; i < registrosCompra.size(); i++) {
            if (registrosCompra.get(i).getId() == obj.getId()) {
                registrosCompra.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirRegistrosCompraEnArchivo(registrosCompra);

        return obj;
    }

    @Override
    public RegistroCompra buscar(int id) {
        List<RegistroCompra> registrosCompra = listado();

        // Implementa la lógica para buscar el registro de compra por ID
        for (RegistroCompra registroCompra : registrosCompra) {
            if (registroCompra.getId() == id) {
                return registroCompra;
            }
        }

        return null; // Registro de compra no encontrado
    }

    @Override
    public List<RegistroCompra> listado() {
        List<RegistroCompra> listaRegistrosCompra = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "registroscompra.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length >= 3) { // Asegurarse de que la línea tenga al menos el formato correcto
                    RegistroCompra registroCompra = new RegistroCompra();
                    registroCompra.setId(Integer.parseInt(partes[0]));
                    registroCompra.setFecha(partes[1]);
                    // Implementa la lógica para obtener el turno y la lista de compras
                    try {
                        registroCompra.setTurno(Turno.valueOf(partes[2]));
                    } catch (IllegalArgumentException e) {
                        // Manejar la excepción, por ejemplo, imprimir un mensaje de error
                        System.err.println("Error al convertir el nombre del turno a un valor de enum Turno: " + e.getMessage());
                    }
                    
                    int idEmpleado = Integer.parseInt(partes[3]);
                    EmpleadoDaoAT empleadoDao = new EmpleadoDaoAT(); // Asume que existe un DAO para Empleado
                    Empleado empleado = empleadoDao.buscar(idEmpleado);
                    if (empleado == null) {
                        empleado = new Empleado();
                        empleado.setId(idEmpleado);
                    }
                    registroCompra.setEmpleado(empleado);
                    
                    CompraDaoAT compraDao = new CompraDaoAT();
                    List<Compra> listCompras = compraDao.listadoPorRegistroCompra(registroCompra.getId());
                    registroCompra.setListCompras((ArrayList<Compra>) listCompras);
                    listaRegistrosCompra.add(registroCompra);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaRegistrosCompra;
    }

    private void escribirRegistrosCompraEnArchivo(List<RegistroCompra> registrosCompra) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "registroscompra.txt"))) {
            for (RegistroCompra registroCompra : registrosCompra) {
                String line = registroCompra.getId() + "," + registroCompra.getFecha() + "," +
                        registroCompra.getTurno() + "," + registroCompra.getEmpleado().getId();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String obtenerIdCompras(List<Compra> listCompras) {
        StringBuilder ids = new StringBuilder();
        for (Compra compra : listCompras) {
            ids.append(compra.getId()).append(",");
        }
        // Elimina la última coma si hay compras
        if (ids.length() > 0) {
            ids.deleteCharAt(ids.length() - 1);
        }
        return ids.toString();
    }

    private int obtenerUltimoId() {
        List<RegistroCompra> registrosCompra = listado();

        if (!registrosCompra.isEmpty()) {
            RegistroCompra ultimoRegistroCompra = registrosCompra.get(registrosCompra.size() - 1);
            return ultimoRegistroCompra.getId();
        } else {
            return 0; // Otra lógica si no hay registros de compra
        }
    }
}
