package alexstyle;

public class CorteCabello extends Servicio {
    private String tipoCorte; // Clásico, Degradado, Moderno, etc.

    public CorteCabello(int idServicio, String nombreServicio, double precioBase, int duracionMinutos, String tipoCorte) {
        super(idServicio, nombreServicio, precioBase, duracionMinutos);
        this.tipoCorte = tipoCorte;
    }

    @Override
    public double calcularPrecioFinal() {
        // Ejemplo de lógica polimórfica: los cortes modernos o complejos tienen un recargo
        if (tipoCorte.equalsIgnoreCase("Degradado Completo")) {
            return precioBase * 1.15; // 15% de recargo
        }
        return precioBase;
    }

    public String getTipoCorte() { return tipoCorte; }
    public void setTipoCorte(String tipoCorte) { this.tipoCorte = tipoCorte; }
}