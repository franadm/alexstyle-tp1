import java.util.Date;

public class Turno {
    private int idTurno;
    private Cliente cliente;
    private Barbero barbero;
    private Servicio servicio;
    private Date fechaHora;
    private String estado;

    public Turno(int idTurno, Cliente cliente, Barbero barbero, Servicio servicio, Date fechaHora, String estado) {
        this.idTurno = idTurno;
        this.cliente = cliente;
        this.barbero = barbero;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdTurno() { return idTurno; }
    public void setIdTurno(int idTurno) { this.idTurno = idTurno; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Barbero getBarbero() { return barbero; }
    public void setBarbero(Barbero barbero) { this.barbero = barbero; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}