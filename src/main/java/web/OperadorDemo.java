package web;

import dominio.Empleado;

final class OperadorDemo {
    private OperadorDemo() {
    }

    static Empleado administrador() {
        Empleado operador = new Empleado();
        operador.setId(0);
        operador.setNombre("Administrador");
        return operador;
    }
}
