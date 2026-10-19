package alexstyle;

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

    // Método abstracto para calcular el precio final con posibles recargos
    public abstract double calcularPrecioFinal();

    // Getters y Setters
    public int getIdServicio() { return idServicio; }
    public String getNombreServicio() { return nombreServicio; }
    public double getPrecioBase() { return precioBase; }
    public int getDuracionMinutos() { return duracionMinutos; }
}