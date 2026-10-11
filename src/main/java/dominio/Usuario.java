package dominio;

public class Usuario {
    protected int id;
    protected String username;
    protected String clave;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getClave() {
        return clave;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
