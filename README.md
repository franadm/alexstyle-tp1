# ALEXSTYLE Manager - Trabajo Práctico N°4 (versión final integradora)

Sistema de gestión de turnos, personal, servicios e inventario para una barbería.
Java + MySQL (JDBC), con patrón **MVC + DAO**.

## Estructura
- `src/alexstyle/modelo` – entidades (Persona, Barbero, Cliente, Servicio, CorteCabello, ServicioBarba, Turno, Producto, Exportable)
- `src/alexstyle/dao` – acceso a datos (CrudDAO, BaseDAO, un DAO por entidad, ConexionBD)
- `src/alexstyle/controlador` – reglas de negocio (BarberiaControlador)
- `src/alexstyle/vista` – menú de consola (MenuConsola)
- `src/alexstyle/excepcion`, `src/alexstyle/util` – excepción propia y manejo de archivos CSV
- `sql/script_base_datos.sql` – crea la base `alexstyle_db` con datos de ejemplo
- `datos/clientes_ejemplo.csv` – archivo de ejemplo para importar clientes

## Cómo ejecutarlo
1. Iniciar MySQL (XAMPP).
2. Ejecutar `sql/script_base_datos.sql` en phpMyAdmin.
3. Agregar `mysql-connector-j` (JAR) a las librerías del proyecto en el IDE (o a la carpeta `lib/`).
4. Revisar usuario/contraseña en `db.properties`.
5. Ejecutar `alexstyle.GestionAlexstyle`.

Por consola: `javac -encoding UTF-8 -d out $(find src -name "*.java")` y
`java -cp out:lib/* alexstyle.GestionAlexstyle` (en Windows, separar con `;`).

## Funciones
Clientes, barberos, servicios (corte y barba), turnos con control de solapamiento y horario,
inventario con alerta de stock bajo, reporte de ingresos por barbero, exportación de turnos e
importación de clientes en CSV.
