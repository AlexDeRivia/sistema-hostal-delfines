package datos;

import dominio.Categoria;
import dominio.Producto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class ProductoDaoAT extends Dao<Producto>{
    @Override
    public Producto crear(Producto obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "productos.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos del producto
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            String nombre = obj.getNombre();
            int stock = obj.getStock();
            double precioVenta = obj.getPrecioVenta();
            double precioCompra = obj.getPrecioCompra();
            Categoria categoria = obj.getCategoria();

            // Construir la línea para escribir en el archivo
            String line = id + "," + nombre + "," + stock + "," + precioVenta + "," + precioCompra + "," + categoria;

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
    public Producto eliminar(Producto obj) {
        List<Producto> productos = listado();

        // Implementa la lógica para eliminar el producto
        productos.removeIf(producto -> producto.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirProductosEnArchivo(productos);

        return obj;
    }

    @Override
    public Producto actualizar(Producto obj) {
        List<Producto> productos = listado();

        // Implementa la lógica para actualizar el producto
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == obj.getId()) {
                productos.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirProductosEnArchivo(productos);

        return obj;
    }

    @Override
    public Producto buscar(int id) {
        List<Producto> productos = listado();

        // Implementa la lógica para buscar el producto por ID
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }

        return null; // Producto no encontrado
    }

    @Override
    public List<Producto> listado() {
        List<Producto> listaProductos = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "productos.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 6) { // Asegurarse de que la línea tenga el formato correcto
                    Producto producto = new Producto();
                    producto.setId(Integer.parseInt(partes[0]));
                    producto.setNombre(partes[1]);
                    producto.setStock(Integer.parseInt(partes[2]));
                    producto.setPrecioVenta(Double.parseDouble(partes[3]));
                    producto.setPrecioCompra(Double.parseDouble(partes[4]));
                    // Ajusta la lógica para obtener la categoría según tu implementación
                    producto.setCategoria(Categoria.valueOf(partes[5])); 
                    listaProductos.add(producto);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaProductos;
    }

    private void escribirProductosEnArchivo(List<Producto> productos) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "productos.txt"))) {
            for (Producto producto : productos) {
                // Ajusta la lógica para obtener la representación de la categoría según tu implementación
                String line = producto.getId() + "," + producto.getNombre() + "," + producto.getStock() + ","
                        + producto.getPrecioVenta() + "," + producto.getPrecioCompra() + ","
                        + producto.getCategoria().toString();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<Producto> productos = listado();

        if (!productos.isEmpty()) {
            Producto ultimoProducto = productos.get(productos.size() - 1);
            return ultimoProducto.getId();
        } else {
            return 0; // Otra lógica si no hay productos
        }
    }
}
