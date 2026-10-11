package datos;

import static datos.Dao.PATH;
import dominio.Compra;
import dominio.LineaCompra;
import dominio.Proveedor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CompraDaoAT extends Dao<Compra> {

    @Override
    public Compra crear(Compra obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "compras.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos de la compra
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            Proveedor proveedor = obj.getProveedor();
            int id_registroCompra = obj.getId_registroCompra(); // Nuevo campo
            List<LineaCompra> listLineasCompras = obj.getLineaCompras();

            // Construir la línea para escribir en el archivo
            String line = id + "," + proveedor.getId() + "," + id_registroCompra;

            // Escribir la línea en el archivo
            bw.write(line);
            bw.newLine();

            // Escribir las líneas de compra asociadas
            LineaCompraDaoAT lineaCompraDao = new LineaCompraDaoAT();
            for (LineaCompra lineaCompra : listLineasCompras) {
                lineaCompra.setCompra_id(id); // Asignar el ID de la compra a cada línea de compra
                lineaCompraDao.crear(lineaCompra); // Crear cada línea de compra en su respectivo DAO
            }

            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public Compra eliminar(Compra obj) {
        List<Compra> compras = listado();

        // Implementa la lógica para eliminar la compra
        compras.removeIf(compra -> compra.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirComprasEnArchivo(compras);

        return obj;
    }

    @Override
    public Compra actualizar(Compra obj) {
        List<Compra> compras = listado();

        // Implementa la lógica para actualizar la compra
        for (int i = 0; i < compras.size(); i++) {
            if (compras.get(i).getId() == obj.getId()) {
                compras.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirComprasEnArchivo(compras);

        return obj;
    }

    @Override
    public Compra buscar(int id) {
        List<Compra> compras = listado();

        // Implementa la lógica para buscar la compra por ID
        for (Compra compra : compras) {
            if (compra.getId() == id) {
                return compra;
            }
        }

        return null; // Compra no encontrada
    }

    @Override
    public List<Compra> listado() {
        List<Compra> listaCompras = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "compras.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 3) { // Asegurarse de que la línea tenga el formato correcto
                    Compra compra = new Compra();
                    compra.setId(Integer.parseInt(partes[0]));

                    // Aquí deberías cargar el Proveedor a partir de su ID
                    ProveedorDaoAT proveedorDao = new ProveedorDaoAT();
                    Proveedor proveedor = proveedorDao.buscar(Integer.parseInt(partes[1]));
                    compra.setProveedor(proveedor);

                    compra.setId_registroCompra(Integer.parseInt(partes[2])); // Nuevo campo
                    
                    LineaCompraDaoAT lineaCompraDao = new LineaCompraDaoAT();
                    List<LineaCompra> listLineasCompras = lineaCompraDao.listadoPorCompra(compra.getId());
                    compra.setLineaCompras((ArrayList<LineaCompra>) listLineasCompras);

                    listaCompras.add(compra);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaCompras;
    }

    public List<Compra> listadoPorRegistroCompra(int idRegistroCompra) {
        List<Compra> listaCompras = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "compras.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 3) { // Asegurarse de que la línea tenga el formato correcto
                    int idCompra = Integer.parseInt(partes[0]);
                    int idProveedor = Integer.parseInt(partes[1]);
                    int idRegistroCompraEnCompra = Integer.parseInt(partes[2]);

                    if (idRegistroCompraEnCompra == idRegistroCompra) {
                        Compra compra = new Compra();
                        compra.setId(idCompra);

                        // Aquí deberías cargar el Proveedor a partir de su ID
                        ProveedorDaoAT proveedorDao = new ProveedorDaoAT();
                        Proveedor proveedor = proveedorDao.buscar(idProveedor);
                        compra.setProveedor(proveedor);

                        // Cargar las líneas de venta asociadas a la venta
                        LineaCompraDaoAT lineaCompraDao = new LineaCompraDaoAT();
                        List<LineaCompra> lineaCompras = lineaCompraDao.listadoPorCompra(compra.getId());
                        compra.setLineaCompras((ArrayList<LineaCompra>) lineaCompras);

                        compra.setId_registroCompra(idRegistroCompra); // Nuevo campo

                        listaCompras.add(compra);
                    }
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaCompras;
    }


    private void escribirComprasEnArchivo(List<Compra> compras) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "compras.txt"))) {
            for (Compra compra : compras) {
                String line = compra.getId() + "," + compra.getProveedor().getId() + "," + obtenerIdLineasCompras(compra.getLineaCompras());;  // Adaptar según la estructura real de la compra
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private String obtenerIdLineasCompras(List<LineaCompra> listLineasCompras) {
        StringBuilder ids = new StringBuilder();
        for (LineaCompra lineaCompra : listLineasCompras) {
            ids.append(lineaCompra.getId()).append(",");
        }
        // Elimina la última coma si hay compras
        if (ids.length() > 0) {
            ids.deleteCharAt(ids.length() - 1);
        }
        return ids.toString();
    }

    public int obtenerUltimoId() {
        List<Compra> compras = listado();

        if (!compras.isEmpty()) {
            Compra ultimaCompra = compras.get(compras.size() - 1);
            return ultimaCompra.getId();
        } else {
            return 0; // Otra lógica si no hay compras
        }
    }
}

