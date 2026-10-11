package datos;

import static datos.Dao.PATH;
import dominio.LineaCompra;
import dominio.Producto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class LineaCompraDaoAT extends Dao<LineaCompra> {

    @Override
    public LineaCompra crear(LineaCompra obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "lineascompra.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos de la línea de compra
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            int cantidad = obj.getCantidad();
            Producto producto = obj.getProducto();
            int compraId = obj.getCompra_id();

            // Construir la línea para escribir en el archivo
            String line = id + "," + cantidad + "," + producto.getId() + "," + compraId;

            // Escribir la línea en el archivo
            bw.write(line);
            bw.newLine();

            // Actualizar el stock del producto
            ProductoDaoAT productoDao = new ProductoDaoAT();
            int nuevoStock = producto.getStock() + cantidad;
            producto.setStock(nuevoStock);
            productoDao.actualizar(producto);

            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public LineaCompra eliminar(LineaCompra obj) {
        List<LineaCompra> lineasCompras = listado();

        // Implementa la lógica para eliminar la línea de compra
        lineasCompras.removeIf(lineaCompra -> lineaCompra.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirLineasComprasEnArchivo(lineasCompras);

        return obj;
    }

    @Override
    public LineaCompra actualizar(LineaCompra obj) {
        List<LineaCompra> lineasCompras = listado();

        // Implementa la lógica para actualizar la línea de compra
        for (int i = 0; i < lineasCompras.size(); i++) {
            if (lineasCompras.get(i).getId() == obj.getId()) {
                lineasCompras.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirLineasComprasEnArchivo(lineasCompras);

        return obj;
    }

    @Override
    public LineaCompra buscar(int id) {
        List<LineaCompra> lineasCompras = listado();

        // Implementa la lógica para buscar la línea de compra por ID
        for (LineaCompra lineaCompra : lineasCompras) {
            if (lineaCompra.getId() == id) {
                return lineaCompra;
            }
        }

        return null; // Línea de compra no encontrada
    }

    @Override
    public List<LineaCompra> listado() {
        List<LineaCompra> listaLineasCompras = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "lineascompra.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 4) { // Asegurarse de que la línea tenga el formato correcto
                    LineaCompra lineaCompra = new LineaCompra();
                    lineaCompra.setId(Integer.parseInt(partes[0]));
                    lineaCompra.setCantidad(Integer.parseInt(partes[1]));

                    // Cargar el Producto a partir de su ID
                    int idProducto = Integer.parseInt(partes[2]);
                    ProductoDaoAT productoDao = new ProductoDaoAT(); // Asume que existe un DAO para Producto
                    Producto producto = productoDao.buscar(idProducto);
                    lineaCompra.setProducto(producto);

                    lineaCompra.setCompra_id(Integer.parseInt(partes[3]));

                    listaLineasCompras.add(lineaCompra);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaLineasCompras;
    }


    public List<LineaCompra> listadoPorCompra(int idCompra) {
        List<LineaCompra> lineasComprasPorCompra = new ArrayList<>();

        // Obtener todas las líneas de compra
        List<LineaCompra> todasLasLineasCompras = listado();

        // Filtrar líneas de compra por el ID de la compra
        for (LineaCompra lineaCompra : todasLasLineasCompras) {
            if (lineaCompra.getCompra_id() == idCompra) {
                lineasComprasPorCompra.add(lineaCompra);
            }
        }

        return lineasComprasPorCompra;
    }

    // ... Otros métodos
    private void escribirLineasComprasEnArchivo(List<LineaCompra> lineasCompras) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "lineascompra.txt"))) {
            for (LineaCompra lineaCompra : lineasCompras) {
                String line = lineaCompra.getId() + "," + lineaCompra.getCantidad() + ","
                        + lineaCompra.getProducto().getId() + "," + lineaCompra.getCompra_id();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<LineaCompra> lineasCompras = listado();

        if (!lineasCompras.isEmpty()) {
            LineaCompra ultimaLineaCompra = lineasCompras.get(lineasCompras.size() - 1);
            return ultimaLineaCompra.getId();
        } else {
            return 0; // Otra lógica si no hay líneas de compra
        }
    }
}

