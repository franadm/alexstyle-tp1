package alexstyle.dao;

import alexstyle.excepcion.AlexstyleExcepcion;
import alexstyle.modelo.Barbero;
import alexstyle.modelo.Cliente;
import alexstyle.modelo.Servicio;
import alexstyle.modelo.Turno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO de turnos. Un turno referencia a un cliente, un barbero y un servicio, por lo que
 * al leer se resuelven esas referencias con los otros DAO (una consulta por tabla).
 */
public class TurnoDAO extends BaseDAO<Turno> {
    private final ClienteDAO clienteDAO;
    private final BarberoDAO barberoDAO;
    private final ServicioDAO servicioDAO;

    public TurnoDAO(ClienteDAO clienteDAO, BarberoDAO barberoDAO, ServicioDAO servicioDAO) {
        this.clienteDAO = clienteDAO;
        this.barberoDAO = barberoDAO;
        this.servicioDAO = servicioDAO;
    }

    @Override
    protected String sqlSelect() {
        return "SELECT id_turno, id_cliente, id_barbero, id_servicio, fecha_hora, estado FROM turnos";
    }

    @Override
    protected String columnaId() { return "id_turno"; }

    @Override
    protected String nombreEntidad() { return "el turno"; }

    /** Las referencias se completan en resolver(); acá solo se lee la fila. */
    @Override
    protected Turno mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(rs.getInt("id_cliente"), null, null, null, null, null);
        Barbero b = new Barbero(rs.getInt("id_barbero"), null, null, null, null, null, 0);
        Servicio s = new alexstyle.modelo.CorteCabello(rs.getInt("id_servicio"), null, 0, 0, null);
        return new Turno(rs.getInt("id_turno"), c, b, s,
                rs.getTimestamp("fecha_hora").toLocalDateTime(), rs.getString("estado"));
    }

    private List<Turno> resolver(List<Turno> crudos) throws AlexstyleExcepcion {
        Map<Integer, Cliente> clientes = new HashMap<>();
        for (Cliente c : clienteDAO.listar()) {
            clientes.put(c.getIdCliente(), c);
        }
        Map<Integer, Barbero> barberos = new HashMap<>();
        for (Barbero b : barberoDAO.listar()) {
            barberos.put(b.getIdBarbero(), b);
        }
        Map<Integer, Servicio> servicios = new HashMap<>();
        for (Servicio s : servicioDAO.listar()) {
            servicios.put(s.getIdServicio(), s);
        }
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : crudos) {
            t.setCliente(clientes.get(t.getCliente().getIdCliente()));
            t.setBarbero(barberos.get(t.getBarbero().getIdBarbero()));
            t.setServicio(servicios.get(t.getServicio().getIdServicio()));
            resultado.add(t);
        }
        return resultado;
    }

    @Override
    public List<Turno> listar() throws AlexstyleExcepcion {
        List<Turno> crudos = new ArrayList<>();
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sqlSelect() + " ORDER BY fecha_hora");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                crudos.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw error("listar", e);
        }
        return resolver(crudos);
    }

    @Override
    public Turno buscarPorId(int id) throws AlexstyleExcepcion {
        for (Turno t : listar()) {
            if (t.getIdTurno() == id) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void insertar(Turno t) throws AlexstyleExcepcion {
        int id = ejecutarInsert(
                "INSERT INTO turnos (id_cliente, id_barbero, id_servicio, fecha_hora, estado) VALUES (?, ?, ?, ?, ?)",
                ps -> {
                    ps.setInt(1, t.getCliente().getIdCliente());
                    ps.setInt(2, t.getBarbero().getIdBarbero());
                    ps.setInt(3, t.getServicio().getIdServicio());
                    ps.setTimestamp(4, Timestamp.valueOf(t.getFechaHora()));
                    ps.setString(5, t.getEstado());
                });
        t.setIdTurno(id);
    }

    @Override
    public void actualizar(Turno t) throws AlexstyleExcepcion {
        ejecutarUpdate(
                "UPDATE turnos SET id_cliente = ?, id_barbero = ?, id_servicio = ?, fecha_hora = ?, estado = ? "
                        + "WHERE id_turno = ?",
                ps -> {
                    ps.setInt(1, t.getCliente().getIdCliente());
                    ps.setInt(2, t.getBarbero().getIdBarbero());
                    ps.setInt(3, t.getServicio().getIdServicio());
                    ps.setTimestamp(4, Timestamp.valueOf(t.getFechaHora()));
                    ps.setString(5, t.getEstado());
                    ps.setInt(6, t.getIdTurno());
                }, "actualizar");
    }

    @Override
    public void eliminar(int id) throws AlexstyleExcepcion {
        ejecutarUpdate("DELETE FROM turnos WHERE id_turno = ?", ps -> ps.setInt(1, id), "eliminar");
    }

    public void actualizarEstado(int idTurno, String estado) throws AlexstyleExcepcion {
        int filas = ejecutarUpdate("UPDATE turnos SET estado = ? WHERE id_turno = ?", ps -> {
            ps.setString(1, estado);
            ps.setInt(2, idTurno);
        }, "actualizar");
        if (filas == 0) {
            throw new AlexstyleExcepcion("No existe un turno con ID " + idTurno + ".");
        }
    }

    /**
     * Indica si el barbero ya tiene un turno activo (no cancelado) que se superpone con
     * el intervalo [inicio, fin).
     */
    public boolean existeSolapamiento(int idBarbero, LocalDateTime inicio, LocalDateTime fin)
            throws AlexstyleExcepcion {
        String sql = "SELECT COUNT(*) FROM turnos t JOIN servicios s ON s.id_servicio = t.id_servicio "
                + "WHERE t.id_barbero = ? AND t.estado <> 'Cancelado' AND t.fecha_hora < ? "
                + "AND DATE_ADD(t.fecha_hora, INTERVAL s.duracion_minutos MINUTE) > ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBarbero);
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            ps.setTimestamp(3, Timestamp.valueOf(inicio));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw error("verificar disponibilidad del", e);
        }
    }
}
