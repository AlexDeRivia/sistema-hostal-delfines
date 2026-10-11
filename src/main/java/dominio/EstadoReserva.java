package dominio;

public enum EstadoReserva {
    CONFIRMADO, PENDIENTE, CANCELADO;
    
    public static EstadoReserva fromString(String estado) {
        for (EstadoReserva e : EstadoReserva.values()) {
            if (e.name().equalsIgnoreCase(estado)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No se puede convertir a EstadoReserva: " + estado);
    }
}
