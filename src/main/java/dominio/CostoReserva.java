package dominio;

public class CostoReserva {
    
    public double calcularCosto(String horas_reservada, Habitacion h){
        
        double costo = 0;
        int cantCamas = h.getNumeroCamas();
        if(horas_reservada.equals("3")){
            if(h.isVentanaAfuera()){
                costo += 25;
            }else{
                costo += 20;
            }
        }else if(horas_reservada.equals("24")){
            if(cantCamas == 2){
                costo += 70;
            }else if(h.isVentanaAfuera()){
                costo += 50;
            }else{
                costo += 40;
            }
        }
        return costo;
    }
    
    public double calcularCostoAdicional(int hora_adicional, String horas_reservada, Habitacion h){
        double costo = 0;
        
        int horas = Integer.parseInt(horas_reservada);
        
        if(hora_adicional > 0 && hora_adicional <= (2*horas) ){
            costo += hora_adicional * 10;
        }
        
        if(hora_adicional > (2*horas)){
            costo += new CostoReserva().calcularCosto(horas_reservada, h);
        }
        return costo;
    }
}
