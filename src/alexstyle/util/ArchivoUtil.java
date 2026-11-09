package alexstyle.util;

import alexstyle.excepcion.AlexstyleExcepcion;
import alexstyle.modelo.Exportable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Lectura y escritura de archivos CSV (UTF-8) con las clases de java.io:
 * File, flujos de bytes (FileInputStream / FileOutputStream) envueltos en
 * BufferedReader y PrintWriter. Los recursos se liberan siempre en el bloque finally.
 */
public final class ArchivoUtil {

    private ArchivoUtil() {
    }

    /** Guarda una lista de objetos Exportable en un CSV con encabezado. */
    public static void exportar(List<? extends Exportable> elementos, String ruta) throws AlexstyleExcepcion {
        if (elementos.isEmpty()) {
            throw new AlexstyleExcepcion("No hay datos para exportar.");
        }
        File archivo = new File(ruta);
        File carpeta = archivo.getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            throw new AlexstyleExcepcion("No se pudo crear la carpeta " + carpeta.getPath() + ".");
        }
        PrintWriter salida = null;
        try {
            salida = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8));
            salida.println(elementos.get(0).getEncabezadoCSV());
            for (Exportable e : elementos) {
                salida.println(e.toCSV());
            }
            if (salida.checkError()) {
                throw new IOException("falló la escritura en el disco");
            }
        } catch (IOException e) {
            throw new AlexstyleExcepcion("No se pudo escribir el archivo " + ruta + ": " + e.getMessage(), e);
        } finally {
            if (salida != null) {
                salida.close(); // se ejecuta siempre, haya o no excepción
            }
        }
    }

    /** Lee todas las líneas de un archivo de texto (la primera es el encabezado). */
    public static List<String> leerLineas(String ruta) throws AlexstyleExcepcion {
        File archivo = new File(ruta);
        if (!archivo.exists() || !archivo.isFile()) {
            throw new AlexstyleExcepcion("El archivo " + ruta + " no existe.");
        }
        List<String> lineas = new ArrayList<>();
        BufferedReader entrada = null;
        try {
            entrada = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8));
            String linea;
            while ((linea = entrada.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            throw new AlexstyleExcepcion("No se pudo leer el archivo " + ruta + ": " + e.getMessage(), e);
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException e) {
                    // cerrar un archivo ya leído no debe interrumpir el programa
                }
            }
        }
        return lineas;
    }
}
