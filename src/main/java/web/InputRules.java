package web;

final class InputRules {
    private InputRules() {
    }

    static String text(String value, String field) {
        if (value == null || value.isBlank() || value.length() > 100
                || value.contains(",") || value.contains("\n") || value.contains("\r")) {
            throw new IllegalArgumentException(field + " no puede estar vacío ni contener comas o saltos de línea.");
        }
        return value.trim();
    }

    static double positiveMoney(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Indica un precio válido.");
        }
        double amount = Double.parseDouble(value);
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        return amount;
    }
}
