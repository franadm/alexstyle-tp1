package alexstyle.dao;

import alexstyle.excepcion.AlexstyleExcepcion;
import alexstyle.modelo.CorteCabello;
import alexstyle.modelo.Servicio;
import alexstyle.modelo.ServicioBarba;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioDAO extends BaseDAO<Servicio> {

    @Override
    protected String sqlSelect() {
        return "SELECT id_servicio, nombre_servicio, precio, duracion_minutos, tipo_servicio, detalle FROM servicios";
    }

    @Override
    protected String columnaId() { return "id_servicio"; }

    @Override
    protected String nombreEntidad() { return "el servicio"; }

    /** Fábrica: según tipo_servicio crea la subclase concreta (polimorfismo). */
    @Override
    protected Servicio mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_servicio");
        String nombre = rs.getString("nombre_servicio");
        double precio = rs.getDouble("precio");
        int duracion = rs.getInt("duracion_minutos");
        String detalle = rs.getString("detalle");
        if ("BARBA".equals(rs.getString("tipo_servicio"))) {
            return new ServicioBarba(id, nombre, precio, duracion, detalle);
        }
        return new CorteCabello(id, nombre, precio, duracion, detalle);
    }

    @Override
    public void insertar(Servicio s) throws AlexstyleExcepcion {
        int id = ejecutarInsert(
                "INSERT INTO servicios (nombre_servicio, precio, duracion_minutos, tipo_servicio, detalle) "
                        + "VALUES (?, ?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, s.getNombreServicio());
                    ps.setDouble(2, s.getPrecioBase());
                    ps.setInt(3, s.getDuracionMinutos());
                    ps.setString(4, s.getTipoServicio());
                    ps.setString(5, s.getDetalle());
                });
        s.setIdServicio(id);
    }

    @Override
    public void actualizar(Servicio s) throws AlexstyleExcepcion {
        ejecutarUpdate(
                "UPDATE servicios SET nombre_servicio = ?, precio = ?, duracion_minutos = ?, "
                        + "tipo_servicio = ?, detalle = ? WHERE id_servicio = ?",
                ps -> {
                    ps.setString(1, s.getNombreServicio());
                    ps.setDouble(2, s.getPrecioBase());
                    ps.setInt(3, s.getDuracionMinutos());
                    ps.setString(4, s.getTipoServicio());
                    ps.setString(5, s.getDetalle());
                    ps.setInt(6, s.getIdServicio());
                }, "actualizar");
    }

    @Override
    public void eliminar(int id) throws AlexstyleExcepcion {
        ejecutarUpdate("DELETE FROM servicios WHERE id_servicio = ?", ps -> ps.setInt(1, id), "eliminar");
    }
}
