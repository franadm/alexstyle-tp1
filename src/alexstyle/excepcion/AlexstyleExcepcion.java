package alexstyle.excepcion;

/**
 * Excepción personalizada del sistema. Se usa tanto para errores de negocio
 * (turno solapado, stock insuficiente, datos inválidos) como para envolver los
 * errores técnicos de JDBC (SQLException) sin exponer la capa de datos a la vista.
 */
public class AlexstyleExcepcion extends Exception {
    private static final long serialVersionUID = 1L;

    public AlexstyleExcepcion(String mensaje) {
        super(mensaje);
    }

    public AlexstyleExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
