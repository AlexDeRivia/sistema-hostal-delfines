package datos;

import dominio.Cliente;
import dominio.LineaVenta;
import dominio.Venta;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class VentaDaoAT extends Dao<Venta>{
    
       @Override
    public Venta crear(Venta obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "Ventas.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos de la venta
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            Cliente cliente = obj.getCliente();
            int id_registroVenta = obj.getId_registroVenta(); // Nuevo campo
            List<LineaVenta> listLineasVentas = obj.getLineaVentas();

            // Construir la línea para escribir en el archivo
            String line = id + "," + cliente.getId() + "," + id_registroVenta;

            // Escribir la línea en el archivo
            bw.write(line);
            bw.newLine();

            LineaVentaDaoAT lineaVentaDao = new LineaVentaDaoAT();
            for (LineaVenta lineaVenta : listLineasVentas) {
                lineaVenta.setVenta_id(id); // Asignar el ID de la venta a cada línea de venta
                lineaVentaDao.crear(lineaVenta); // Crear cada línea de venta en su respectivo DAO
            }

            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Venta eliminar(Venta obj) {
        List<Venta> ventas = listado();

        // Implementa la lógica para eliminar la venta
        ventas.removeIf(venta -> venta.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirVentasEnArchivo(ventas);

        return obj;
    }

    @Override
    public Venta actualizar(Venta obj) {
        List<Venta> ventas = listado();

        // Implementa la lógica para actualizar la venta
        for (int i = 0; i < ventas.size(); i++) {
            if (ventas.get(i).getId() == obj.getId()) {
                ventas.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirVentasEnArchivo(ventas);

        return obj;
    }

    @Override
    public Venta buscar(int id) {
        List<Venta> ventas = listado();

        // Implementa la lógica para buscar la venta por ID
        for (Venta venta : ventas) {
            if (venta.getId() == id) {
                return venta;
            }
        }

        return null; // Venta no encontrada
    }

    @Override
    public List<Venta> listado() {
        List<Venta> listaVentas = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "Ventas.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 3) { // Asegurarse de que la línea tenga el formato correcto
                    Venta venta = new Venta();
                    venta.setId(Integer.parseInt(partes[0]));

                    int idCliente = Integer.parseInt(partes[1]);
                    ClienteDaoAT clienteDao = new ClienteDaoAT(); // Asume que existe un DAO para Cliente
                    Cliente cliente = clienteDao.buscar(idCliente);
                    venta.setCliente(cliente);

                    int idRegistroVenta = Integer.parseInt(partes[2]);
                    venta.setId_registroVenta(idRegistroVenta);

                    // Cargar las líneas de venta asociadas a la venta
                    LineaVentaDaoAT lineaVentaDao = new LineaVentaDaoAT(); // Asume que existe un DAO para LineaVenta
                    List<LineaVenta> lineaVentas = lineaVentaDao.listadoPorVenta(venta.getId());
                    venta.setLineaVentas((ArrayList<LineaVenta>) lineaVentas);

                    listaVentas.add(venta);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaVentas;
    }

    public List<Venta> listadoPorRegistroVenta(int idRegistroVenta) {
        List<Venta> listaVentas = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "Ventas.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 3) { // Asegurarse de que la línea tenga el formato correcto
                    int idVenta = Integer.parseInt(partes[0]);
                    int idCliente = Integer.parseInt(partes[1]);

                    // Verificar si la venta tiene el mismo ID de registro de venta
                    int idRegistroVentaEnVenta = Integer.parseInt(partes[2]);
                    if (idRegistroVentaEnVenta == idRegistroVenta) {
                        Venta venta = new Venta();
                        venta.setId(idVenta);

                        // Aquí deberías cargar el Cliente a partir de su ID
                        ClienteDaoAT clienteDao = new ClienteDaoAT();
                        Cliente cliente = clienteDao.buscar(idCliente);
                        venta.setCliente(cliente);

                        // Cargar las líneas de venta asociadas a la venta
                        LineaVentaDaoAT lineaVentaDao = new LineaVentaDaoAT();
                        List<LineaVenta> lineaVentas = lineaVentaDao.listadoPorVenta(venta.getId());
                        venta.setLineaVentas((ArrayList<LineaVenta>) lineaVentas);

                        venta.setId_registroVenta(idRegistroVenta); // Nuevo campo

                        listaVentas.add(venta);
                    }
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaVentas;
    }

    private void escribirVentasEnArchivo(List<Venta> ventas) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "Ventas.txt"))) {
            for (Venta venta : ventas) {
                String line = venta.getId() + "," + venta.getCliente().getId() + "," + venta.getId_registroVenta();
                bw.write(line);
                bw.newLine();
                // Escribir las líneas de venta asociadas a la venta en el archivo de líneas de venta
                escribirLineasVentasEnArchivo(venta.getLineaVentas(), venta.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void escribirLineasVentasEnArchivo(List<LineaVenta> lineaVentas, int ventaId) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "LineaVentas.txt"), StandardOpenOption.APPEND)) {
            for (LineaVenta lineaVenta : lineaVentas) {
                String line = ventaId + "," + lineaVenta.getId();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<Venta> ventas = listado();

        if (!ventas.isEmpty()) {
            Venta ultimaVenta = ventas.get(ventas.size() - 1);
            return ultimaVenta.getId();
        } else {
            return 0; // Otra lógica si no hay ventas
        }
    }
}
