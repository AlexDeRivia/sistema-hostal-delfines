package dominio;

public class IGV {
    private int valor;

    public IGV() {
        this.setValor(18);
    }
    
    
    public double calcularIGV(double total){
        double x = this.getValor() / 100.0;
        double igv = x * total;
        return (Math.round(igv*100.0)/100.0)+total;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
