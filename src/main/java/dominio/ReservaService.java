package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/** Operaciones de reserva que deben mantenerse coherentes en una instancia. */
public final class ReservaService {
    private ReservaService() {
    }

    public static synchronized Reserva crear(Reserva reserva, RegistroReserva registro) {
        validar(reserva, 0);
        BusinessRegistroReserva registros = new BusinessRegistroReserva();
        if (registro.getId() == 0) {
            registros.crear(registro);
        }
        reserva.setRegistroreserva_id(registro.getId());
        new BusinessReserva().crear(reserva);
        recalcularLiquidacion(registro.getId());
        return reserva;
    }

    public static synchronized Reserva actualizar(Reserva reserva) {
        Reserva anterior = new BusinessReserva().buscar(reserva.getId());
        if (anterior == null) {
            throw new IllegalArgumentException("La reserva ya no existe.");
        }
        reserva.setRegistroreserva_id(anterior.getRegistroreserva_id());
        validar(reserva, reserva.getId());
        new BusinessReserva().actualizar(reserva);
        recalcularLiquidacion(reserva.getRegistroreserva_id());
        return reserva;
    }

    public static synchronized void eliminar(int id) {
        BusinessReserva reservas = new BusinessReserva();
        Reserva reserva = reservas.buscar(id);
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva ya no existe.");
        }
        reservas.eliminar(reserva);
        recalcularLiquidacion(reserva.getRegistroreserva_id());
    }

    public static synchronized boolean disponible(int habitacionId, LocalDateTime inicio,
            LocalDateTime fin, int excluirReservaId) {
        for (Reserva actual : new BusinessReserva().listado()) {
            if (actual.getId() == excluirReservaId || actual.getHabitacion() == null
                    || actual.getHabitacion().getId() != habitacionId
                    || actual.getEstadoReserva() == EstadoReserva.CANCELADO
                    || actual.getEstadoReserva() == EstadoReserva.FINALIZADO) {
                continue;
            }
            if (actual.getFechaIngreso() == null || actual.getFechaIngreso().isBlank()) {
                return false;
            }
            if (inicio.isBefore(actual.getFin()) && actual.getInicio().isBefore(fin)) {
                return false;
            }
        }
        return true;
    }

    private static void validar(Reserva reserva, int excluirReservaId) {
        if (reserva.getCliente() == null || reserva.getCliente().getId() <= 0
                || new BusinessCliente().buscar(reserva.getCliente().getId()) == null) {
            throw new IllegalArgumentException("Selecciona un cliente válido.");
        }
        Habitacion habitacion = reserva.getHabitacion();
        if (habitacion == null || habitacion.getId() <= 0) {
            throw new IllegalArgumentException("Selecciona una habitación válida.");
        }
        habitacion = new BusinessHabitacion().buscar(habitacion.getId());
        if (habitacion == null) {
            throw new IllegalArgumentException("La habitación ya no existe.");
        }
        if (reserva.getNumeroHuespedes() <= 0) {
            throw new IllegalArgumentException("Indica al menos un huésped.");
        }
        if (reserva.getEstadoReserva() == null) {
            throw new IllegalArgumentException("Selecciona un estado válido.");
        }
        if (!"3".equals(reserva.getHoras_reservadas()) && !"24".equals(reserva.getHoras_reservadas())) {
            throw new IllegalArgumentException("La estancia debe ser de 3 o 24 horas.");
        }
        LocalDateTime inicio;
        LocalDateTime fin;
        try {
            inicio = reserva.getInicio();
            fin = reserva.getFin();
        } catch (DateTimeParseException | NullPointerException ex) {
            throw new IllegalArgumentException("Indica una fecha y hora válidas.");
        }
        if (!Double.isFinite(reserva.getCostoEfectivo()) || !Double.isFinite(reserva.getCostoYape())
                || reserva.getCostoEfectivo() < 0 || reserva.getCostoYape() < 0) {
            throw new IllegalArgumentException("Los pagos deben ser números no negativos.");
        }
        double costo = new CostoReserva().calcularCosto(reserva.getHoras_reservadas(), habitacion);
        if (reserva.getCostoEfectivo() + reserva.getCostoYape() > costo + 0.001) {
            throw new IllegalArgumentException("Los pagos superan el costo de la estancia.");
        }
        if (reserva.getEstadoReserva() != EstadoReserva.CANCELADO
                && reserva.getEstadoReserva() != EstadoReserva.FINALIZADO) {
            if (!habitacion.isDisponible() || !disponible(habitacion.getId(), inicio, fin, excluirReservaId)) {
                throw new IllegalArgumentException("La habitación no está disponible en ese horario.");
            }
        }
        reserva.setHabitacion(habitacion);
    }

    private static void recalcularLiquidacion(int registroId) {
        BusinessRegistroReserva registros = new BusinessRegistroReserva();
        RegistroReserva registro = registros.listado().stream()
                .filter(item -> item.getId() == registroId).findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró el registro de la reserva."));
        double total = new BusinessReserva().listado().stream()
                .filter(item -> item.getRegistroreserva_id() == registroId)
                .mapToDouble(item -> item.getCostoEfectivo() + item.getCostoYape()).sum();
        registro.setLiquidacion(total);
        registros.actualizar(registro);
    }
}
