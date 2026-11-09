package alexstyle.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Turno implements Exportable {
    public static final String[] ESTADOS = {"Pendiente", "Confirmado", "Completado", "Cancelado"};
    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int idTurno;
    private Cliente cliente;
    private Barbero barbero;
    private Servicio servicio;
    private LocalDateTime fechaHora;
    private String estado;

    public Turno(int idTurno, Cliente cliente, Barbero barbero, Servicio servicio,
                 LocalDateTime fechaHora, String estado) {
        this.idTurno = idTurno;
        this.cliente = cliente;
        this.barbero = barbero;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public static boolean esEstadoValido(String estado) {
        for (String e : ESTADOS) {
            if (e.equalsIgnoreCase(estado)) {
                return true;
            }
        }
        return false;
    }

    public LocalDateTime getFechaFin() {
        return fechaHora.plusMinutes(servicio.getDuracionMinutos());
    }

    @Override
    public String getEncabezadoCSV() {
        return "id_turno;fecha_hora;cliente;barbero;servicio;precio_final;estado";
    }

    @Override
    public String toCSV() {
        return idTurno + ";" + fechaHora.format(FORMATO) + ";" + cliente.getNombreCompleto() + ";"
                + barbero.getNombreCompleto() + ";" + servicio.getNombreServicio() + ";"
                + String.format(java.util.Locale.US, "%.2f", servicio.calcularPrecioFinal()) + ";" + estado;
    }

    public int getIdTurno() { return idTurno; }
    public void setIdTurno(int idTurno) { this.idTurno = idTurno; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Barbero getBarbero() { return barbero; }
    public void setBarbero(Barbero barbero) { this.barbero = barbero; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
