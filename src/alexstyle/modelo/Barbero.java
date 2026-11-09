package alexstyle.modelo;

public class Barbero extends Persona {
    private int idBarbero;
    private String especialidad;
    private double comisionPorcentaje;

    public Barbero(int idBarbero, String nombre, String apellido, String dni, String telefono,
                   String especialidad, double comisionPorcentaje) {
        super(nombre, apellido, dni, telefono);
        this.idBarbero = idBarbero;
        this.especialidad = especialidad;
        this.comisionPorcentaje = comisionPorcentaje;
    }

    @Override
    public String getRol() {
        return "Barbero profesional - " + especialidad;
    }

    public int getIdBarbero() { return idBarbero; }
    public void setIdBarbero(int idBarbero) { this.idBarbero = idBarbero; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public double getComisionPorcentaje() { return comisionPorcentaje; }
    public void setComisionPorcentaje(double comisionPorcentaje) { this.comisionPorcentaje = comisionPorcentaje; }
}
