package alexstyle.modelo;

/** Superclase abstracta de los servicios de la barbería. */
public abstract class Servicio {
    protected int idServicio;
    protected String nombreServicio;
    protected double precioBase;
    protected int duracionMinutos;

    public Servicio(int idServicio, String nombreServicio, double precioBase, int duracionMinutos) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.precioBase = precioBase;
        this.duracionMinutos = duracionMinutos;
    }

    /** Polimorfismo: precio final con los recargos propios de cada tipo de servicio. */
    public abstract double calcularPrecioFinal();

    /** Identificador del tipo, tal como se guarda en la columna tipo_servicio ("CORTE", "BARBA"). */
    public abstract String getTipoServicio();

    /** Dato específico del subtipo (tipo de corte, estilo de barba), guardado en la columna detalle. */
    public abstract String getDetalle();

    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }
    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
}
