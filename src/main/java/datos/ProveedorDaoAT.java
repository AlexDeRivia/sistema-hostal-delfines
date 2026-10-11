package datos;

import static datos.Dao.PATH;
import dominio.Proveedor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDaoAT extends Dao<Proveedor>{
    @Override
    public Proveedor crear(Proveedor obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "proveedores.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos del proveedor
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            String razonSocial = obj.getRazonSocial();
            String ruc = obj.getRuc();

            // Construir la línea para escribir en el archivo
            String line = id + "," + razonSocial + "," + ruc;

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
    public Proveedor eliminar(Proveedor obj) {
        List<Proveedor> proveedores = listado();

        // Implementa la lógica para eliminar el proveedor
        proveedores.removeIf(proveedor -> proveedor.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirProveedoresEnArchivo(proveedores);

        return obj;
    }

    @Override
    public Proveedor actualizar(Proveedor obj) {
        List<Proveedor> proveedores = listado();

        // Implementa la lógica para actualizar el proveedor
        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getId() == obj.getId()) {
                proveedores.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirProveedoresEnArchivo(proveedores);

        return obj;
    }

    @Override
    public Proveedor buscar(int id) {
        List<Proveedor> proveedores = listado();

        // Implementa la lógica para buscar el proveedor por ID
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getId() == id) {
                return proveedor;
            }
        }

        return null; // Proveedor no encontrado
    }

    @Override
    public List<Proveedor> listado() {
        List<Proveedor> listaProveedores = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "proveedores.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 3) { // Asegurarse de que la línea tenga el formato correcto
                    Proveedor proveedor = new Proveedor();
                    proveedor.setId(Integer.parseInt(partes[0]));
                    proveedor.setRazonSocial(partes[1]);
                    proveedor.setRuc(partes[2]);
                    listaProveedores.add(proveedor);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaProveedores;
    }

    private void escribirProveedoresEnArchivo(List<Proveedor> proveedores) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "proveedores.txt"))) {
            for (Proveedor proveedor : proveedores) {
                String line = proveedor.getId() + "," + proveedor.getRazonSocial() + "," + proveedor.getRuc();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<Proveedor> proveedores = listado();

        if (!proveedores.isEmpty()) {
            Proveedor ultimoProveedor = proveedores.get(proveedores.size() - 1);
            return ultimoProveedor.getId();
        } else {
            return 0; // Otra lógica si no hay proveedores
        }
    }
}
