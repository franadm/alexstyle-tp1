package alexstyle;

import java.util.ArrayList;
import java.util.List;

public class BarberiaCentral {
    private String nombreBarberia;
    private List<Barbero> barberos;
    private List<Servicio> servicios;

    public BarberiaCentral(String nombreBarberia) {
        this.nombreBarberia = nombreBarberia;
        this.barberos = new ArrayList<>();
        this.servicios = new ArrayList<>();
    }

    public void agregarBarbero(Barbero barbero) {
        barberos.add(barbero);
        System.out.println("Barbero registrado con éxito: " + barbero.getNombre());
    }

    public void agregarServicio(Servicio servicio) {
        servicios.add(servicio);
        System.out.println("Servicio agregado con éxito: " + servicio.getNombreServicio());
    }

    public void listarPersonal() {
        if (barberos.isEmpty()) {
            System.out.println("No hay barberos registrados en el sistema.");
            return;
        }
        System.out.println("\n--- PLANTEL DE BARBEROS ---");
        for (Barbero b : barberos) {
            System.out.println("ID: " + b.getIdBarbero() + " | Nombre: " + b.getNombre() + " " + b.getApellido());
            b.mostrarRol(); // Polimorfismo en acción
        }
    }

    public void listarServicios() {
        if (servicios.isEmpty()) {
            System.out.println("No hay servicios registrados.");
            return;
        }
        System.out.println("\n--- LISTA DE SERVICIOS Y PRECIOS ---");
        for (Servicio s : servicios) {
            System.out.println("Servicio: " + s.getNombreServicio() + " | Precio Base: $" + s.getPrecioBase() + " | Precio Final Estimado: $" + s.calcularPrecioFinal());
        }
    }
}