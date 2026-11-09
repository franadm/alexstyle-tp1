package alexstyle.dao;

import alexstyle.excepcion.AlexstyleExcepcion;
import alexstyle.modelo.Barbero;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BarberoDAO extends BaseDAO<Barbero> {

    @Override
    protected String sqlSelect() {
        return "SELECT id_barbero, nombre, apellido, dni, telefono, especialidad, comision_porcentaje FROM barberos";
    }

    @Override
    protected String columnaId() { return "id_barbero"; }

    @Override
    protected String nombreEntidad() { return "el barbero"; }

    @Override
    protected Barbero mapear(ResultSet rs) throws SQLException {
        return new Barbero(rs.getInt("id_barbero"), rs.getString("nombre"), rs.getString("apellido"),
                rs.getString("dni"), rs.getString("telefono"), rs.getString("especialidad"),
                rs.getDouble("comision_porcentaje"));
    }

    @Override
    public void insertar(Barbero b) throws AlexstyleExcepcion {
        int id = ejecutarInsert(
                "INSERT INTO barberos (nombre, apellido, dni, telefono, especialidad, comision_porcentaje) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, b.getNombre());
                    ps.setString(2, b.getApellido());
                    ps.setString(3, b.getDni());
                    ps.setString(4, b.getTelefono());
                    ps.setString(5, b.getEspecialidad());
                    ps.setDouble(6, b.getComisionPorcentaje());
                });
        b.setIdBarbero(id);
    }

    @Override
    public void actualizar(Barbero b) throws AlexstyleExcepcion {
        ejecutarUpdate(
                "UPDATE barberos SET nombre = ?, apellido = ?, dni = ?, telefono = ?, especialidad = ?, "
                        + "comision_porcentaje = ? WHERE id_barbero = ?",
                ps -> {
                    ps.setString(1, b.getNombre());
                    ps.setString(2, b.getApellido());
                    ps.setString(3, b.getDni());
                    ps.setString(4, b.getTelefono());
                    ps.setString(5, b.getEspecialidad());
                    ps.setDouble(6, b.getComisionPorcentaje());
                    ps.setInt(7, b.getIdBarbero());
                }, "actualizar");
    }

    @Override
    public void eliminar(int id) throws AlexstyleExcepcion {
        ejecutarUpdate("DELETE FROM barberos WHERE id_barbero = ?", ps -> ps.setInt(1, id), "eliminar");
    }
}
