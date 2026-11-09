package alexstyle.controlador;

import alexstyle.dao.BarberoDAO;
import alexstyle.dao.ClienteDAO;
import alexstyle.dao.CrudDAO;
import alexstyle.dao.ProductoDAO;
import alexstyle.dao.ServicioDAO;
import alexstyle.dao.TurnoDAO;
import alexstyle.excepcion.AlexstyleExcepcion;
import alexstyle.modelo.Barbero;
import alexstyle.modelo.Cliente;
import alexstyle.modelo.CorteCabello;
import alexstyle.modelo.Producto;
import alexstyle.modelo.Servicio;
import alexstyle.modelo.ServicioBarba;
import alexstyle.modelo.Turno;
import alexstyle.util.ArchivoUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Controlador (patrón MVC): contiene las reglas de negocio y coordina a los DAO.
 * La vista le pide operaciones; el controlador valida, aplica las reglas y persiste.
 */
public class BarberiaControlador {
    public static final LocalTime APERTURA = LocalTime.of(9, 0);
    public static final LocalTime CIERRE = LocalTime.of(20, 0);

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final BarberoDAO barberoDAO = new BarberoDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final TurnoDAO turnoDAO = new TurnoDAO(clienteDAO, barberoDAO, servicioDAO);

    // ---------- Validaciones auxiliares ----------

    private static String texto(String valor, String campo) throws AlexstyleExcepcion {
        if (valor == null || valor.trim().isEmpty()) {
            throw new AlexstyleExcepcion("El campo '" + campo + "' no puede estar vacío.");
        }
        return valor.trim();
    }

    private static String dni(String valor) throws AlexstyleExcepcion {
        String d = texto(valor, "DNI");
        if (!d.matches("\\d{7,8}")) {
            throw new AlexstyleExcepcion("El DNI debe tener 7 u 8 dígitos numéricos.");
        }
        return d;
    }

    private static <T> T existente(CrudDAO<T> dao, int id, String entidad) throws AlexstyleExcepcion {
        T encontrado = dao.buscarPorId(id);
        if (encontrado == null) {
            throw new AlexstyleExcepcion("No existe " + entidad + " con ID " + id + ".");
        }
        return encontrado;
    }

    // ---------- Clientes ----------

    public Cliente registrarCliente(String nombre, String apellido, String dni, String telefono, String correo)
            throws AlexstyleExcepcion {
        String mail = (correo == null || correo.trim().isEmpty()) ? null : correo.trim();
        Cliente c = new Cliente(0, texto(nombre, "Nombre"), texto(apellido, "Apellido"), dni(dni),
                texto(telefono, "Teléfono"), mail);
        clienteDAO.insertar(c);
        return c;
    }

    public List<Cliente> listarClientes() throws AlexstyleExcepcion {
        return clienteDAO.listar();
    }

    public void actualizarTelefonoCliente(int idCliente, String telefono) throws AlexstyleExcepcion {
        Cliente c = existente(clienteDAO, idCliente, "un cliente");
        c.setTelefono(texto(telefono, "Teléfono"));
        clienteDAO.actualizar(c);
    }

    public void eliminarCliente(int idCliente) throws AlexstyleExcepcion {
        existente(clienteDAO, idCliente, "un cliente");
        clienteDAO.eliminar(idCliente);
    }

    // ---------- Barberos ----------

    public Barbero registrarBarbero(String nombre, String apellido, String dni, String telefono,
                                    String especialidad, double comision) throws AlexstyleExcepcion {
        if (comision < 0 || comision > 100) {
            throw new AlexstyleExcepcion("La comisión debe estar entre 0 y 100.");
        }
        Barbero b = new Barbero(0, texto(nombre, "Nombre"), texto(apellido, "Apellido"), dni(dni),
                texto(telefono, "Teléfono"), texto(especialidad, "Especialidad"), comision);
        barberoDAO.insertar(b);
        return b;
    }

    public List<Barbero> listarBarberos() throws AlexstyleExcepcion {
        return barberoDAO.listar();
    }

    // ---------- Servicios ----------

    /** @param tipo "CORTE" o "BARBA" */
    public Servicio registrarServicio(String tipo, String nombre, double precio, int duracion, String detalle)
            throws AlexstyleExcepcion {
        if (precio <= 0) {
            throw new AlexstyleExcepcion("El precio debe ser mayor que cero.");
        }
        if (duracion <= 0 || duracion > 240) {
            throw new AlexstyleExcepcion("La duración debe estar entre 1 y 240 minutos.");
        }
        String nom = texto(nombre, "Nombre del servicio");
        String det = texto(detalle, "Detalle");
        Servicio s = "BARBA".equalsIgnoreCase(tipo)
                ? new ServicioBarba(0, nom, precio, duracion, det)
                : new CorteCabello(0, nom, precio, duracion, det);
        servicioDAO.insertar(s);
        return s;
    }

    public List<Servicio> listarServicios() throws AlexstyleExcepcion {
        return servicioDAO.listar();
    }

    // ---------- Turnos ----------

    public Turno reservarTurno(int idCliente, int idBarbero, int idServicio, LocalDateTime inicio)
            throws AlexstyleExcepcion {
        Cliente cliente = existente(clienteDAO, idCliente, "un cliente");
        Barbero barbero = existente(barberoDAO, idBarbero, "un barbero");
        Servicio servicio = existente(servicioDAO, idServicio, "un servicio");

        if (inicio.isBefore(LocalDateTime.now())) {
            throw new AlexstyleExcepcion("No se puede reservar un turno en una fecha pasada.");
        }
        LocalDateTime fin = inicio.plusMinutes(servicio.getDuracionMinutos());
        if (inicio.toLocalTime().isBefore(APERTURA) || fin.toLocalTime().isAfter(CIERRE)
                || !fin.toLocalDate().equals(inicio.toLocalDate())) {
            throw new AlexstyleExcepcion("El turno debe estar dentro del horario de atención ("
                    + APERTURA + " a " + CIERRE + ").");
        }
        if (turnoDAO.existeSolapamiento(idBarbero, inicio, fin)) {
            throw new AlexstyleExcepcion("El barbero " + barbero.getNombreCompleto()
                    + " ya tiene un turno que se superpone con ese horario.");
        }
        Turno t = new Turno(0, cliente, barbero, servicio, inicio, "Pendiente");
        turnoDAO.insertar(t);
        return t;
    }

    public List<Turno> listarTurnos() throws AlexstyleExcepcion {
        return turnoDAO.listar();
    }

    public void cambiarEstadoTurno(int idTurno, String estado) throws AlexstyleExcepcion {
        if (!Turno.esEstadoValido(estado)) {
            throw new AlexstyleExcepcion("Estado inválido. Valores permitidos: "
                    + String.join(", ", Turno.ESTADOS) + ".");
        }
        for (String e : Turno.ESTADOS) {
            if (e.equalsIgnoreCase(estado)) {
                turnoDAO.actualizarEstado(idTurno, e);
                return;
            }
        }
    }

    public void eliminarTurno(int idTurno) throws AlexstyleExcepcion {
        existente(turnoDAO, idTurno, "un turno");
        turnoDAO.eliminar(idTurno);
    }

    // ---------- Inventario ----------

    public Producto registrarProducto(String nombre, int stock, double precio) throws AlexstyleExcepcion {
        if (stock < 0) {
            throw new AlexstyleExcepcion("El stock no puede ser negativo.");
        }
        if (precio < 0) {
            throw new AlexstyleExcepcion("El precio no puede ser negativo.");
        }
        Producto p = new Producto(0, texto(nombre, "Nombre del producto"), stock, precio);
        productoDAO.insertar(p);
        return p;
    }

    public List<Producto> listarProductos() throws AlexstyleExcepcion {
        return productoDAO.listar();
    }

    /** Suma (o resta, con cantidad negativa) unidades al stock de un producto. */
    public Producto ajustarStock(int idProducto, int cantidad) throws AlexstyleExcepcion {
        Producto p = existente(productoDAO, idProducto, "un producto");
        int nuevo = p.getStockActual() + cantidad;
        if (nuevo < 0) {
            throw new AlexstyleExcepcion("Stock insuficiente: hay " + p.getStockActual()
                    + " unidades y se intentan retirar " + (-cantidad) + ".");
        }
        p.setStockActual(nuevo);
        productoDAO.actualizar(p);
        return p;
    }

    /** Productos con stock menor o igual al umbral, ordenados del menor al mayor stock (arreglo). */
    public Producto[] productosConStockBajo(int umbral) throws AlexstyleExcepcion {
        List<Producto> bajos = productoDAO.listar();
        Iterator<Producto> it = bajos.iterator(); // el Iterator permite quitar elementos mientras se recorre
        while (it.hasNext()) {
            if (it.next().getStockActual() > umbral) {
                it.remove();
            }
        }
        Producto[] arreglo = bajos.toArray(new Producto[0]);
        Arrays.sort(arreglo, Comparator.comparingInt(Producto::getStockActual));
        return arreglo;
    }

    // ---------- Reportes y archivos ----------

    /**
     * Ingresos de turnos completados por barbero. El arreglo resultante está alineado con
     * la lista de barberos recibida (posición i = barbero i).
     */
    public double[] calcularIngresosPorBarbero(List<Barbero> barberos, List<Turno> turnos) {
        double[] ingresos = new double[barberos.size()];
        for (Turno t : turnos) {
            if (!"Completado".equals(t.getEstado())) {
                continue;
            }
            for (int i = 0; i < barberos.size(); i++) {
                if (barberos.get(i).getIdBarbero() == t.getBarbero().getIdBarbero()) {
                    ingresos[i] += t.getServicio().calcularPrecioFinal();
                }
            }
        }
        return ingresos;
    }

    /**
     * Matriz (arreglo bidimensional) de cantidad de turnos: cada fila es un barbero
     * (mismo orden que la lista recibida) y cada columna un estado (orden de Turno.ESTADOS).
     */
    public int[][] contarTurnosPorBarberoYEstado(List<Barbero> barberos, List<Turno> turnos) {
        int[][] matriz = new int[barberos.size()][Turno.ESTADOS.length];
        for (Turno t : turnos) {
            for (int fila = 0; fila < barberos.size(); fila++) {
                if (barberos.get(fila).getIdBarbero() != t.getBarbero().getIdBarbero()) {
                    continue;
                }
                for (int col = 0; col < Turno.ESTADOS.length; col++) {
                    if (Turno.ESTADOS[col].equals(t.getEstado())) {
                        matriz[fila][col]++;
                    }
                }
            }
        }
        return matriz;
    }

    public void exportarTurnosCSV(String ruta) throws AlexstyleExcepcion {
        ArchivoUtil.exportar(turnoDAO.listar(), ruta);
    }

    /**
     * Importa clientes desde un CSV (nombre;apellido;dni;telefono;correo, con encabezado).
     * Las líneas inválidas se informan en la lista devuelta y no detienen la importación.
     *
     * @return mensajes de resultado; el primer elemento resume cuántos clientes se importaron
     */
    public List<String> importarClientesCSV(String ruta) throws AlexstyleExcepcion {
        List<String> lineas = ArchivoUtil.leerLineas(ruta);
        List<String> errores = new ArrayList<>();
        int importados = 0;
        for (int i = 1; i < lineas.size(); i++) { // la línea 0 es el encabezado
            String linea = lineas.get(i).trim();
            if (linea.isEmpty()) {
                continue;
            }
            String[] campos = linea.split(";", -1);
            try {
                if (campos.length < 4) {
                    throw new AlexstyleExcepcion("faltan campos (se esperan nombre;apellido;dni;telefono;correo)");
                }
                registrarCliente(campos[0], campos[1], campos[2], campos[3], campos.length > 4 ? campos[4] : "");
                importados++;
            } catch (AlexstyleExcepcion e) {
                errores.add("Línea " + (i + 1) + ": " + e.getMessage());
            }
        }
        List<String> resultado = new ArrayList<>();
        resultado.add("Clientes importados: " + importados + " | Líneas con error: " + errores.size());
        resultado.addAll(errores);
        return resultado;
    }
}
