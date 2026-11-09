package alexstyle.modelo;

/** Superclase abstracta de las personas del sistema (barberos y clientes). */
public abstract class Persona {
    protected String nombre;
    protected String apellido;
    protected String dni;
    protected String telefono;

    public Persona(String nombre, String apellido, String dni, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
    }

    /** Polimorfismo: cada subclase describe su rol. */
    public abstract String getRol();

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
