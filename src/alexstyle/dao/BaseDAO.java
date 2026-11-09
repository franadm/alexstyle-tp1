package alexstyle.dao;

import alexstyle.excepcion.AlexstyleExcepcion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que reutiliza la lógica JDBC común a todos los DAO:
 * apertura de conexión, ejecución de escrituras, lectura de listas y traducción
 * de SQLException a AlexstyleExcepcion.
 */
public abstract class BaseDAO<T> implements CrudDAO<T> {

    /** Convierte la fila actual del ResultSet en un objeto del modelo. */
    protected abstract T mapear(ResultSet rs) throws SQLException;

    protected abstract String sqlSelect();

    protected abstract String nombreEntidad();

    protected Connection conectar() throws AlexstyleExcepcion {
        return ConexionBD.obtenerConexion();
    }

    protected AlexstyleExcepcion error(String operacion, SQLException e) {
        String detalle = e.getMessage();
        if (e.getErrorCode() == 1062) {
            detalle = "ya existe un registro con ese valor único (por ejemplo, el DNI)";
        } else if (e.getErrorCode() == 1451 || e.getErrorCode() == 1452) {
            detalle = "el registro está relacionado con otros datos (turnos)";
        }
        return new AlexstyleExcepcion("Error al " + operacion + " " + nombreEntidad() + ": " + detalle, e);
    }

    /** Ejecuta un INSERT y devuelve la clave autogenerada. */
    protected int ejecutarInsert(String sql, Parametros parametros) throws AlexstyleExcepcion {
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            parametros.asignar(ps);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw error("insertar", e);
        }
    }

    /** Ejecuta un UPDATE o DELETE y devuelve las filas afectadas. */
    protected int ejecutarUpdate(String sql, Parametros parametros, String operacion) throws AlexstyleExcepcion {
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            parametros.asignar(ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw error(operacion, e);
        }
    }

    @Override
    public List<T> listar() throws AlexstyleExcepcion {
        List<T> lista = new ArrayList<>();
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sqlSelect() + " ORDER BY " + columnaId());
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw error("listar", e);
        }
        return lista;
    }

    @Override
    public T buscarPorId(int id) throws AlexstyleExcepcion {
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sqlSelect() + " WHERE " + columnaId() + " = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw error("buscar", e);
        }
    }

    protected abstract String columnaId();

    /** Interfaz funcional para asignar los parámetros de un PreparedStatement. */
    @FunctionalInterface
    protected interface Parametros {
        void asignar(PreparedStatement ps) throws SQLException;
    }
}
