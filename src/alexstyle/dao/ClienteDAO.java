package alexstyle.dao;

import alexstyle.excepcion.AlexstyleExcepcion;
import alexstyle.modelo.Cliente;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO extends BaseDAO<Cliente> {

    @Override
    protected String sqlSelect() {
        return "SELECT id_cliente, nombre, apellido, dni, telefono, correo FROM clientes";
    }

    @Override
    protected String columnaId() { return "id_cliente"; }

    @Override
    protected String nombreEntidad() { return "el cliente"; }

    @Override
    protected Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(rs.getInt("id_cliente"), rs.getString("nombre"), rs.getString("apellido"),
                rs.getString("dni"), rs.getString("telefono"), rs.getString("correo"));
    }

    @Override
    public void insertar(Cliente c) throws AlexstyleExcepcion {
        int id = ejecutarInsert(
                "INSERT INTO clientes (nombre, apellido, dni, telefono, correo) VALUES (?, ?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, c.getNombre());
                    ps.setString(2, c.getApellido());
                    ps.setString(3, c.getDni());
                    ps.setString(4, c.getTelefono());
                    ps.setString(5, c.getCorreo());
                });
        c.setIdCliente(id);
    }

    @Override
    public void actualizar(Cliente c) throws AlexstyleExcepcion {
        ejecutarUpdate(
                "UPDATE clientes SET nombre = ?, apellido = ?, dni = ?, telefono = ?, correo = ? WHERE id_cliente = ?",
                ps -> {
                    ps.setString(1, c.getNombre());
                    ps.setString(2, c.getApellido());
                    ps.setString(3, c.getDni());
                    ps.setString(4, c.getTelefono());
                    ps.setString(5, c.getCorreo());
                    ps.setInt(6, c.getIdCliente());
                }, "actualizar");
    }

    @Override
    public void eliminar(int id) throws AlexstyleExcepcion {
        ejecutarUpdate("DELETE FROM clientes WHERE id_cliente = ?", ps -> ps.setInt(1, id), "eliminar");
    }
}
