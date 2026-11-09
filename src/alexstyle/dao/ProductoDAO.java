package alexstyle.dao;

import alexstyle.excepcion.AlexstyleExcepcion;
import alexstyle.modelo.Producto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO extends BaseDAO<Producto> {

    @Override
    protected String sqlSelect() {
        return "SELECT id_producto, nombre_producto, stock, precio_unitario FROM inventario";
    }

    @Override
    protected String columnaId() { return "id_producto"; }

    @Override
    protected String nombreEntidad() { return "el producto"; }

    @Override
    protected Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(rs.getInt("id_producto"), rs.getString("nombre_producto"),
                rs.getInt("stock"), rs.getDouble("precio_unitario"));
    }

    @Override
    public void insertar(Producto p) throws AlexstyleExcepcion {
        int id = ejecutarInsert(
                "INSERT INTO inventario (nombre_producto, stock, precio_unitario) VALUES (?, ?, ?)",
                ps -> {
                    ps.setString(1, p.getNombreProducto());
                    ps.setInt(2, p.getStockActual());
                    ps.setDouble(3, p.getPrecioUnitario());
                });
        p.setIdProducto(id);
    }

    @Override
    public void actualizar(Producto p) throws AlexstyleExcepcion {
        ejecutarUpdate(
                "UPDATE inventario SET nombre_producto = ?, stock = ?, precio_unitario = ? WHERE id_producto = ?",
                ps -> {
                    ps.setString(1, p.getNombreProducto());
                    ps.setInt(2, p.getStockActual());
                    ps.setDouble(3, p.getPrecioUnitario());
                    ps.setInt(4, p.getIdProducto());
                }, "actualizar");
    }

    @Override
    public void eliminar(int id) throws AlexstyleExcepcion {
        ejecutarUpdate("DELETE FROM inventario WHERE id_producto = ?", ps -> ps.setInt(1, id), "eliminar");
    }
}
