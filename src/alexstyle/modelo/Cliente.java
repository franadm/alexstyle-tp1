package alexstyle.modelo;

public class Cliente extends Persona {
    private int idCliente;
    private String correo;

    public Cliente(int idCliente, String nombre, String apellido, String dni, String telefono, String correo) {
        super(nombre, apellido, dni, telefono);
        this.idCliente = idCliente;
        this.correo = correo;
    }

    @Override
    public String getRol() {
        return "Cliente";
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
