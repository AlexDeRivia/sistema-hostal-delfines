package web;

import dominio.BusinessProducto;
import dominio.Categoria;
import dominio.Producto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SvAddProducto")
public class SvAddProducto extends HttpServlet {

    private BusinessProducto productoInformation = new BusinessProducto();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.accionDefault(request, response);
        //this.iniciarRegistroReserva();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        System.out.println("doPost");
        if (accion != null) {
            switch (accion) {
                case "add":
                    this.agregarProducto(request, response);
                    this.accionDefault(request, response);
                    System.out.println("Agregando");
                    break;
                default:
                    this.accionDefault(request, response);
            }
        }else{
            this.accionDefault(request, response);
        }
    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGET 1");
        request.getRequestDispatcher("AddProducto.jsp").forward(request, response);
    }

    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Recuperamos datos del formulario
        String nombreProducto = request.getParameter("producto");
        double precioV = Double.parseDouble(request.getParameter("pventa"));
        double precioC = Double.parseDouble(request.getParameter("pcompra"));
        String ctg = String.valueOf(request.getParameter("categoria"));
        Categoria categoria;
        if(ctg.equals("ALIMENTOS")){
            categoria = Categoria.ALIMENTOS;
        }else{
            categoria = Categoria.USO_PERSONAL;
        }
        if(!nombreProducto.equals("") && precioV > 0 && precioC > 0 && !ctg.equals("")){
            productoInformation.crear(new Producto(nombreProducto, 0, precioV, precioC, categoria));
        }
        System.out.println("SE GUARDO");
    }
}
