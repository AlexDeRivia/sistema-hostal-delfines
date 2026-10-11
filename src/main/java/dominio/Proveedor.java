package dominio;

public class Proveedor {
    protected int id;
    protected String razonSocial;
    protected String ruc;

    public int getId() {
        return id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
}
