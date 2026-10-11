package datos;

import dominio.Factura;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FacturaDaoAT extends Dao<Factura> {

    @Override
    public Factura crear(Factura obj) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "facturas.txt"), StandardOpenOption.APPEND);

            // Obtener los atributos de la factura
            int nuevoId = obtenerUltimoId() + 1;
            int id = nuevoId;
            String fecha = obj.getFecha();  // Usar la fecha actual proporcionada por el objeto
            String ruc = obj.getRuc();

            // Construir la línea para escribir en el archivo
            String line = id + "," + fecha + "," + ruc;

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
    public Factura eliminar(Factura obj) {
        List<Factura> facturas = listado();

        // Implementa la lógica para eliminar la factura
        facturas.removeIf(factura -> factura.getId() == obj.getId());

        // Vuelve a escribir la lista actualizada en el archivo
        escribirFacturasEnArchivo(facturas);

        return obj;
    }

    @Override
    public Factura actualizar(Factura obj) {
        List<Factura> facturas = listado();

        // Implementa la lógica para actualizar la factura
        for (int i = 0; i < facturas.size(); i++) {
            if (facturas.get(i).getId() == obj.getId()) {
                facturas.set(i, obj);
                break;
            }
        }

        // Vuelve a escribir la lista actualizada en el archivo
        escribirFacturasEnArchivo(facturas);

        return obj;
    }

    @Override
    public Factura buscar(int id) {
        List<Factura> facturas = listado();

        // Implementa la lógica para buscar la factura por ID
        for (Factura factura : facturas) {
            if (factura.getId() == id) {
                return factura;
            }
        }

        return null; // Factura no encontrada
    }

    @Override
    public List<Factura> listado() {
        List<Factura> listaFacturas = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + "facturas.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] partes = line.split(",");

                if (partes.length == 3) { // Asegurarse de que la línea tenga el formato correcto
                    Factura factura = new Factura();
                    factura.setId(Integer.parseInt(partes[0]));
                    factura.setFecha(partes[1]);
                    factura.setRuc(partes[2]);
                    listaFacturas.add(factura);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace(); // Manejar la excepción según tu lógica
        }

        return listaFacturas;
    }

    private void escribirFacturasEnArchivo(List<Factura> facturas) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + "facturas.txt"))) {
            for (Factura factura : facturas) {
                String line = factura.getId() + "," + factura.getFecha() + "," + factura.getRuc();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int obtenerUltimoId() {
        List<Factura> facturas = listado();

        if (!facturas.isEmpty()) {
            Factura ultimaFactura = facturas.get(facturas.size() - 1);
            return ultimaFactura.getId();
        } else {
            return 0; // Otra lógica si no hay facturas
        }
    }
}
