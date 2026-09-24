public class Barbero {
    private int idBarbero;
    private String nombre;
    private String especialidad;

    public Barbero(int idBarbero, String nombre, String especialidad) {
        this.idBarbero = idBarbero;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public int getIdBarbero() { return idBarbero; }
    public void setIdBarbero(int idBarbero) { this.idBarbero = idBarbero; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}