package alexstyle.modelo;

public class CorteCabello extends Servicio {
    private String tipoCorte; // Clásico, Degradado Completo, Moderno, etc.

    public CorteCabello(int idServicio, String nombreServicio, double precioBase, int duracionMinutos, String tipoCorte) {
        super(idServicio, nombreServicio, precioBase, duracionMinutos);
        this.tipoCorte = tipoCorte;
    }

    @Override
    public double calcularPrecioFinal() {
        if ("Degradado Completo".equalsIgnoreCase(tipoCorte)) {
            return precioBase * 1.15; // 15% de recargo por complejidad
        }
        return precioBase;
    }

    @Override
    public String getTipoServicio() { return "CORTE"; }

    @Override
    public String getDetalle() { return tipoCorte; }

    public String getTipoCorte() { return tipoCorte; }
    public void setTipoCorte(String tipoCorte) { this.tipoCorte = tipoCorte; }
}
