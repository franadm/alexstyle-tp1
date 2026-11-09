package alexstyle.vista;

import alexstyle.controlador.BarberiaControlador;
import alexstyle.excepcion.AlexstyleExcepcion;
import alexstyle.modelo.Barbero;
import alexstyle.modelo.Cliente;
import alexstyle.modelo.Producto;
import alexstyle.modelo.Servicio;
import alexstyle.modelo.Turno;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/** Vista (patrón MVC): muestra menús y resultados, y lee los datos que ingresa el usuario. */
public class MenuConsola {
    private final BarberiaControlador controlador;
    private final Scanner scanner = new Scanner(System.in);

    public MenuConsola(BarberiaControlador controlador) {
        this.controlador = controlador;
    }

    public void iniciar() {
        boolean ejecutando = true;
        while (ejecutando) {
            System.out.println("\n=========================================");
            System.out.println("      ALEXSTYLE Manager - MENÚ PRINCIPAL");
            System.out.println("=========================================");
            System.out.println("1. Clientes");
            System.out.println("2. Barberos");
            System.out.println("3. Servicios");
            System.out.println("4. Turnos");
            System.out.println("5. Inventario");
            System.out.println("6. Reportes y archivos");
            System.out.println("0. Salir");
            int opcion = leerEntero("Elige una opción: ");
            try {
                switch (opcion) {
                    case 1 -> menuClientes();
                    case 2 -> menuBarberos();
                    case 3 -> menuServicios();
                    case 4 -> menuTurnos();
                    case 5 -> menuInventario();
                    case 6 -> menuReportes();
                    case 0 -> {
                        System.out.println("Saliendo de ALEXSTYLE Manager...");
                        ejecutando = false;
                    }
                    default -> System.out.println("Opción no válida. Ingrese un número del 0 al 6.");
                }
            } catch (AlexstyleExcepcion e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
        scanner.close();
    }

    // ---------- Submenús ----------

    private void menuClientes() throws AlexstyleExcepcion {
        System.out.println("\n--- CLIENTES ---\n1. Registrar\n2. Listar\n3. Actualizar teléfono\n4. Eliminar\n0. Volver");
        switch (leerEntero("Opción: ")) {
            case 1 -> {
                Cliente c = controlador.registrarCliente(leerTexto("Nombre: "), leerTexto("Apellido: "),
                        leerTexto("DNI: "), leerTexto("Teléfono: "), leerTexto("Correo (opcional): "));
                System.out.println("Cliente registrado con ID " + c.getIdCliente() + ".");
            }
            case 2 -> mostrarClientes(controlador.listarClientes());
            case 3 -> {
                controlador.actualizarTelefonoCliente(leerEntero("ID del cliente: "), leerTexto("Nuevo teléfono: "));
                System.out.println("Teléfono actualizado.");
            }
            case 4 -> {
                controlador.eliminarCliente(leerEntero("ID del cliente a eliminar: "));
                System.out.println("Cliente eliminado.");
            }
            default -> { }
        }
    }

    private void menuBarberos() throws AlexstyleExcepcion {
        System.out.println("\n--- BARBEROS ---\n1. Registrar\n2. Listar\n0. Volver");
        switch (leerEntero("Opción: ")) {
            case 1 -> {
                Barbero b = controlador.registrarBarbero(leerTexto("Nombre: "), leerTexto("Apellido: "),
                        leerTexto("DNI: "), leerTexto("Teléfono: "), leerTexto("Especialidad: "),
                        leerDecimal("Comisión (%): "));
                System.out.println("Barbero registrado con ID " + b.getIdBarbero() + ".");
            }
            case 2 -> mostrarBarberos(controlador.listarBarberos());
            default -> { }
        }
    }

    private void menuServicios() throws AlexstyleExcepcion {
        System.out.println("\n--- SERVICIOS ---\n1. Registrar corte de cabello\n2. Registrar servicio de barba\n3. Listar\n0. Volver");
        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1, 2 -> {
                String tipo = opcion == 1 ? "CORTE" : "BARBA";
                String pregunta = opcion == 1 ? "Tipo de corte (ej. Degradado Completo): "
                        : "Estilo de barba (ej. Perfilado Completo): ";
                Servicio s = controlador.registrarServicio(tipo, leerTexto("Nombre del servicio: "),
                        leerDecimal("Precio base: "), leerEntero("Duración en minutos: "), leerTexto(pregunta));
                System.out.println("Servicio registrado con ID " + s.getIdServicio()
                        + ". Precio final: $" + String.format("%.2f", s.calcularPrecioFinal()));
            }
            case 3 -> mostrarServicios(controlador.listarServicios());
            default -> { }
        }
    }

    private void menuTurnos() throws AlexstyleExcepcion {
        System.out.println("\n--- TURNOS ---\n1. Reservar turno\n2. Listar turnos\n3. Cambiar estado\n4. Eliminar turno\n0. Volver");
        switch (leerEntero("Opción: ")) {
            case 1 -> {
                mostrarClientes(controlador.listarClientes());
                int idCliente = leerEntero("ID del cliente: ");
                mostrarBarberos(controlador.listarBarberos());
                int idBarbero = leerEntero("ID del barbero: ");
                mostrarServicios(controlador.listarServicios());
                int idServicio = leerEntero("ID del servicio: ");
                LocalDateTime fecha = leerFechaHora("Fecha y hora (dd/MM/yyyy HH:mm): ");
                Turno t = controlador.reservarTurno(idCliente, idBarbero, idServicio, fecha);
                System.out.println("Turno reservado con ID " + t.getIdTurno() + " para el "
                        + t.getFechaHora().format(Turno.FORMATO) + ".");
            }
            case 2 -> mostrarTurnos(controlador.listarTurnos());
            case 3 -> {
                int id = leerEntero("ID del turno: ");
                String estado = leerTexto("Nuevo estado (" + String.join("/", Turno.ESTADOS) + "): ");
                controlador.cambiarEstadoTurno(id, estado);
                System.out.println("Estado actualizado.");
            }
            case 4 -> {
                controlador.eliminarTurno(leerEntero("ID del turno a eliminar: "));
                System.out.println("Turno eliminado.");
            }
            default -> { }
        }
    }

    private void menuInventario() throws AlexstyleExcepcion {
        System.out.println("\n--- INVENTARIO ---\n1. Registrar producto\n2. Listar\n3. Ingresar stock\n4. Retirar stock\n5. Productos con stock bajo\n0. Volver");
        switch (leerEntero("Opción: ")) {
            case 1 -> {
                Producto p = controlador.registrarProducto(leerTexto("Nombre: "),
                        leerEntero("Stock inicial: "), leerDecimal("Precio unitario: "));
                System.out.println("Producto registrado con ID " + p.getIdProducto() + ".");
            }
            case 2 -> mostrarProductos(controlador.listarProductos());
            case 3 -> {
                Producto p = controlador.ajustarStock(leerEntero("ID del producto: "), leerEntero("Cantidad a ingresar: "));
                System.out.println("Stock actual de " + p.getNombreProducto() + ": " + p.getStockActual());
            }
            case 4 -> {
                Producto p = controlador.ajustarStock(leerEntero("ID del producto: "), -leerEntero("Cantidad a retirar: "));
                System.out.println("Stock actual de " + p.getNombreProducto() + ": " + p.getStockActual());
            }
            case 5 -> {
                Producto[] bajos = controlador.productosConStockBajo(leerEntero("Umbral de stock: "));
                if (bajos.length == 0) {
                    System.out.println("No hay productos con stock bajo.");
                }
                for (Producto p : bajos) {
                    System.out.println("ALERTA - " + p.getNombreProducto() + ": " + p.getStockActual() + " unidades");
                }
            }
            default -> { }
        }
    }

    private void menuReportes() throws AlexstyleExcepcion {
        System.out.println("\n--- REPORTES Y ARCHIVOS ---\n1. Ingresos por barbero (turnos completados)\n"
                + "2. Exportar turnos a CSV\n3. Importar clientes desde CSV\n4. Turnos por barbero y estado\n0. Volver");
        switch (leerEntero("Opción: ")) {
            case 1 -> {
                List<Barbero> barberos = controlador.listarBarberos();
                double[] ingresos = controlador.calcularIngresosPorBarbero(barberos, controlador.listarTurnos());
                double total = 0;
                System.out.println("\n--- INGRESOS POR BARBERO ---");
                for (int i = 0; i < ingresos.length; i++) {
                    double comision = ingresos[i] * barberos.get(i).getComisionPorcentaje() / 100;
                    System.out.printf("%-25s Ingresos: $%10.2f | Comisión (%.0f%%): $%9.2f%n",
                            barberos.get(i).getNombreCompleto(), ingresos[i],
                            barberos.get(i).getComisionPorcentaje(), comision);
                    total += ingresos[i];
                }
                System.out.printf("TOTAL: $%.2f%n", total);
            }
            case 2 -> {
                String ruta = leerTexto("Archivo de destino (ej. datos/turnos.csv): ");
                controlador.exportarTurnosCSV(ruta);
                System.out.println("Turnos exportados a " + ruta);
            }
            case 3 -> {
                String ruta = leerTexto("Archivo a importar (ej. datos/clientes.csv): ");
                for (String linea : controlador.importarClientesCSV(ruta)) {
                    System.out.println(linea);
                }
            }
            case 4 -> {
                List<Barbero> barberos = controlador.listarBarberos();
                int[][] matriz = controlador.contarTurnosPorBarberoYEstado(barberos, controlador.listarTurnos());
                System.out.println("\n--- TURNOS POR BARBERO Y ESTADO ---");
                System.out.printf("%-22s", "Barbero");
                for (String estado : Turno.ESTADOS) {
                    System.out.printf("%12s", estado);
                }
                System.out.println();
                for (int fila = 0; fila < matriz.length; fila++) {
                    System.out.printf("%-22s", barberos.get(fila).getNombreCompleto());
                    for (int col = 0; col < matriz[fila].length; col++) {
                        System.out.printf("%12d", matriz[fila][col]);
                    }
                    System.out.println();
                }
            }
            default -> { }
        }
    }

    // ---------- Presentación de resultados ----------

    private void mostrarClientes(List<Cliente> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("\n--- CLIENTES ---");
        for (Cliente c : lista) {
            System.out.printf("ID %d | %s | DNI %s | Tel %s | %s%n", c.getIdCliente(), c.getNombreCompleto(),
                    c.getDni(), c.getTelefono(), c.getCorreo() == null ? "-" : c.getCorreo());
        }
    }

    private void mostrarBarberos(List<Barbero> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay barberos registrados.");
            return;
        }
        System.out.println("\n--- BARBEROS ---");
        for (Barbero b : lista) {
            System.out.printf("ID %d | %s | %s | Comisión %.0f%%%n", b.getIdBarbero(), b.getNombreCompleto(),
                    b.getRol(), b.getComisionPorcentaje()); // getRol(): polimorfismo
        }
    }

    private void mostrarServicios(List<Servicio> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay servicios registrados.");
            return;
        }
        System.out.println("\n--- SERVICIOS ---");
        for (Servicio s : lista) {
            System.out.printf("ID %d | %s (%s: %s) | %d min | Base $%.2f | Final $%.2f%n", s.getIdServicio(),
                    s.getNombreServicio(), s.getTipoServicio(), s.getDetalle(), s.getDuracionMinutos(),
                    s.getPrecioBase(), s.calcularPrecioFinal()); // calcularPrecioFinal(): polimorfismo
        }
    }

    private void mostrarTurnos(List<Turno> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay turnos registrados.");
            return;
        }
        System.out.println("\n--- TURNOS ---");
        for (Turno t : lista) {
            System.out.printf("ID %d | %s | %s | Barbero: %s | %s | $%.2f | %s%n", t.getIdTurno(),
                    t.getFechaHora().format(Turno.FORMATO), t.getCliente().getNombreCompleto(),
                    t.getBarbero().getNombreCompleto(), t.getServicio().getNombreServicio(),
                    t.getServicio().calcularPrecioFinal(), t.getEstado());
        }
    }

    private void mostrarProductos(List<Producto> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay productos en el inventario.");
            return;
        }
        System.out.println("\n--- INVENTARIO ---");
        for (Producto p : lista) {
            System.out.printf("ID %d | %s | Stock: %d | $%.2f%n", p.getIdProducto(), p.getNombreProducto(),
                    p.getStockActual(), p.getPrecioUnitario());
        }
    }

    // ---------- Lectura segura de datos ----------

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    /** Repite la consulta hasta que se ingrese un entero (captura InputMismatchException). */
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine(); // limpia el salto de línea
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // descarta la entrada inválida
                System.out.println("[ERROR DE ENTRADA] Debe ingresar un número entero.");
            }
        }
    }

    /** Acepta coma o punto decimal (el teclado argentino usa coma). */
    private double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String ingreso = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(ingreso);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR DE ENTRADA] Debe ingresar un número (ej. 8500 o 8500,50).");
            }
        }
    }

    private LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDateTime.parse(scanner.nextLine().trim(), Turno.FORMATO);
            } catch (DateTimeParseException e) {
                System.out.println("[ERROR DE ENTRADA] Formato inválido. Use dd/MM/yyyy HH:mm (ej. 20/11/2026 15:30).");
            }
        }
    }
}
