package alexstyle.dao;

import alexstyle.excepcion.AlexstyleExcepcion;

import java.util.List;

/**
 * Contrato común de persistencia (patrón DAO). Cada entidad lo implementa con JDBC.
 * El controlador depende de esta interfaz y no de los detalles de MySQL.
 */
public interface CrudDAO<T> {
    /** Inserta la entidad y le asigna el id generado por AUTO_INCREMENT. */
    void insertar(T entidad) throws AlexstyleExcepcion;

    List<T> listar() throws AlexstyleExcepcion;

    /** @return la entidad, o null si no existe. */
    T buscarPorId(int id) throws AlexstyleExcepcion;

    void actualizar(T entidad) throws AlexstyleExcepcion;

    void eliminar(int id) throws AlexstyleExcepcion;
}
