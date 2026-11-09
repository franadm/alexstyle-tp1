package alexstyle.modelo;

/** Interfaz para los objetos que pueden guardarse como una línea de archivo CSV. */
public interface Exportable {
    String getEncabezadoCSV();

    String toCSV();
}
