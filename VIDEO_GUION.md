# Guion del video (aprox. 3 minutos)

Grabar la pantalla con el IDE y la consola visibles. Tiempos orientativos.

**0:00 - 0:20 · Presentación.** "Soy Francisco Lescano. Este es ALEXSTYLE Manager, un sistema para gestionar
turnos, barberos, servicios e inventario de una barbería. Resuelve la agenda en papel, los turnos superpuestos
y el control de insumos."

**0:20 - 0:55 · Arquitectura.** Mostrar el árbol de paquetes. "Usé el patrón MVC con DAO: la vista es el menú, el
controlador tiene las reglas de negocio y los DAO hablan con MySQL por JDBC. La vista no conoce SQL." Mostrar
brevemente `CrudDAO` (interfaz) y `BaseDAO` (clase abstracta).

**0:55 - 1:10 · Base de datos.** Abrir phpMyAdmin con `alexstyle_db`: cinco tablas, `turnos` con claves foráneas.

**1:10 - 2:30 · Demostración en consola.**
1. Clientes → Registrar uno nuevo. Mostrar en phpMyAdmin que apareció la fila (persistencia).
2. Intentar registrar el mismo DNI → mostrar el mensaje de error (excepción de BD).
3. Turnos → Reservar un turno (ej. 20/11/2026 10:00).
4. Reservar otro con el mismo barbero a las 10:30 → error de solapamiento.
5. Cambiar el estado a "Completado".
6. Inventario → retirar más stock del disponible → error; ver productos con stock bajo (arreglo ordenado).
7. Reportes → ingresos por barbero; exportar turnos a CSV e importar `clientes_ejemplo.csv` (una fila con error).

**2:30 - 2:50 · Código clave.** Mostrar `existeSolapamiento()` (consulta SQL) y el uso de `try/catch` con
`AlexstyleExcepcion`. Mencionar `ArrayList` para colecciones y arreglos para estados, stock bajo e ingresos.

**2:50 - 3:00 · Cierre.** "El código completo está en GitHub y el informe explica el patrón elegido y las pruebas.
Gracias."
