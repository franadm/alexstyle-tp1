package alexstyle.modelo;

public class ServicioBarba extends Servicio {
    private String estiloBarba; // Perfilado Completo, Recorte, Afeitado clásico, etc.

    public ServicioBarba(int idServicio, String nombreServicio, double precioBase, int duracionMinutos, String estiloBarba) {
        super(idServicio, nombreServicio, precioBase, duracionMinutos);
        this.estiloBarba = estiloBarba;
    }

    @Override
    public double calcularPrecioFinal() {
        if ("Perfilado Completo".equalsIgnoreCase(estiloBarba)) {
            return precioBase * 1.10; // 10% de recargo por perfilado
        }
        return precioBase;
    }

    @Override
    public String getTipoServicio() { return "BARBA"; }

    @Override
    public String getDetalle() { return estiloBarba; }

    public String getEstiloBarba() { return estiloBarba; }
    public void setEstiloBarba(String estiloBarba) { this.estiloBarba = estiloBarba; }
}
