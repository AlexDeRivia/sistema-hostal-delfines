package datos;

import dominio.LineaVenta;
import dominio.Producto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class LineaVentaDaoAT extends Dao<LineaVenta> {

    @Override
    public LineaVenta crear(LineaVenta obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "lineasventa.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos de la línea de venta
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            int cantidad = obj.getCantidad();
            Producto producto = obj.getProducto();
            int venta_id = obj.getVenta_id();
            ProductoDaoAT productoDao = new ProductoDaoAT();
            Producto actual = productoDao.buscar(producto.getId());
            if (actual == null || cantidad <= 0 || actual.getStock() < cantidad) {
                throw new IllegalArgumentException("No hay stock suficiente para esta venta.");
            }
            // Construir la línea para escribir en el archivo
            String line = id + "," + cantidad + "," + producto.getId() + "," + venta_id
                    + "," + obj.getPrecioUnitario();

            // Escribir la línea en el archivo
            bw.write(line);
            bw.newLine();

            // Actualizar el stock del producto
            actual.setStock(actual.getStock() - cantidad);
            productoDao.actualizar(actual);

            bw.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return obj;
    }

    @Override
    public LineaVenta eliminar(LineaVenta obj) {
        List<LineaVenta> lineasVentas = listado();

        // Implementa la lógica para eliminar la línea de venta
        lineasVentas.removeIf(lineaVenta -> lineaVenta.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirLineasVentasEnArchivo(lineasVentas);

        return obj;
    }

    @Override
    public LineaVenta actualizar(LineaVenta obj) {
        List<LineaVenta> lineasVentas = listado();

        // Implementa la lógica para actualizar la línea de venta
        for (int i = 0; i < lineasVentas.size(); i++) {
            if (lineasVentas.get(i).getId() == obj.getId()) {
                lineasVentas.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirLineasVentasEnArchivo(lineasVentas);

        return obj;
    }

    public List<LineaVenta> listadoPorVenta(int idVenta) {
        List<LineaVenta> lineaVentasPorVenta = new ArrayList<>();

        // Obtener todas las líneas de venta
        List<LineaVenta> todasLasLineasVentas = listado();

        // Filtrar líneas de venta por el ID de la venta
        for (LineaVenta lineaVenta : todasLasLineasVentas) {
            // Adaptar según la estructura real de LineaVenta y su relación con Venta
            if (lineaVenta.getVenta_id() == idVenta) {
                lineaVentasPorVenta.add(lineaVenta);
            }
        }

        return lineaVentasPorVenta;
    }

    @Override
    public LineaVenta buscar(int id) {
        List<LineaVenta> lineasVentas = listado();

        // Implementa la lógica para buscar la línea de venta por ID
        for (LineaVenta lineaVenta : lineasVentas) {
            if (lineaVenta.getId() == id) {
                return lineaVenta;
            }
        }

        return null; // Línea de venta no encontrada
    }

    @Override
    public List<LineaVenta> listado() {
        List<LineaVenta> listaLineasVentas = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "lineasventa.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length >= 4) { // El formato antiguo no guardaba el precio.
                    LineaVenta lineaVenta = new LineaVenta();
                    lineaVenta.setId(Integer.parseInt(partes[0]));
                    lineaVenta.setCantidad(Integer.parseInt(partes[1]));

                    // Cargar el Producto a partir de su ID
                    int idProducto = Integer.parseInt(partes[2]);
                    ProductoDaoAT productoDao = new ProductoDaoAT(); // Asume que existe un DAO para Producto
                    Producto producto = productoDao.buscar(idProducto);
                    lineaVenta.setProducto(producto);
                    lineaVenta.setPrecioUnitario(partes.length >= 5
                            ? Double.parseDouble(partes[4]) : producto.getPrecioVenta());

                    lineaVenta.setVenta_id(Integer.parseInt(partes[3]));
                    listaLineasVentas.add(lineaVenta);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaLineasVentas;
    }

    private void escribirLineasVentasEnArchivo(List<LineaVenta> lineasVentas) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "lineasventa.txt"))) {
            for (LineaVenta lineaVenta : lineasVentas) {
                String line = lineaVenta.getId() + "," + lineaVenta.getCantidad() + ","
                        + lineaVenta.getProducto().getId() + "," + lineaVenta.getVenta_id()
                        + "," + lineaVenta.getPrecioUnitario();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<LineaVenta> lineasVentas = listado();

        if (!lineasVentas.isEmpty()) {
            LineaVenta ultimaLineaVenta = lineasVentas.get(lineasVentas.size() - 1);
            return ultimaLineaVenta.getId();
        } else {
            return 0; // Otra lógica si no hay líneas de venta
        }
    }
}
