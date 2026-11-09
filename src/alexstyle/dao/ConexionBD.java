package alexstyle.dao;

import alexstyle.excepcion.AlexstyleExcepcion;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Centraliza la conexión a MySQL. Lee los datos de conexión del archivo db.properties
 * (si existe) y, si no, usa valores por defecto para XAMPP.
 */
public final class ConexionBD {
    private static final String ARCHIVO_CONFIG = "db.properties";
    private static final Properties CONFIG = cargarConfiguracion();

    private ConexionBD() {
    }

    private static Properties cargarConfiguracion() {
        Properties p = new Properties();
        p.setProperty("db.url", "jdbc:mysql://localhost:3306/alexstyle_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Argentina/Buenos_Aires");
        p.setProperty("db.user", "root");
        p.setProperty("db.password", "");
        p.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        try (InputStream in = new FileInputStream(ARCHIVO_CONFIG)) {
            p.load(in);
        } catch (IOException e) {
            // Sin archivo de configuración se usan los valores por defecto.
        }
        return p;
    }

    public static Connection obtenerConexion() throws AlexstyleExcepcion {
        try {
            Class.forName(CONFIG.getProperty("db.driver"));
            return DriverManager.getConnection(
                    CONFIG.getProperty("db.url"),
                    CONFIG.getProperty("db.user"),
                    CONFIG.getProperty("db.password"));
        } catch (ClassNotFoundException e) {
            throw new AlexstyleExcepcion("No se encontró el driver JDBC de MySQL. "
                    + "Agregue mysql-connector-j al classpath (carpeta lib).", e);
        } catch (SQLException e) {
            throw new AlexstyleExcepcion("No se pudo conectar a la base de datos. "
                    + "Verifique que MySQL (XAMPP) esté iniciado y que exista alexstyle_db: " + e.getMessage(), e);
        }
    }
}
