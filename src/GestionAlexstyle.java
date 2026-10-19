package alexstyle;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GestionAlexstyle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BarberiaCentral central = new BarberiaCentral("ALEXSTYLE Manager");
        boolean ejecutando = true;

        while (ejecutando) {
            System.out.println("\n_________________________________________");
            System.out.println("   MENU PRINCIPAL - ALEXSTYLE Manager    ");
            System.out.println("_________________________________________");
            System.out.println("1. Registrar Barbero");
            System.out.println("2. Registrar Servicio");
            System.out.println("3. Listar Personal (Barberos)");
            System.out.println("4. Listar Servicios y Precios");
            System.out.println("5. Salir");
            System.out.println("_________________________________________");
            System.out.print("Elige una opción: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1 -> {
                        System.out.print("ID del Barbero: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nombre: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Apellido: ");
                        String apellido = scanner.nextLine();
                        System.out.print("DNI: ");
                        String dni = scanner.nextLine();
                        System.out.print("Teléfono: ");
                        String tel = scanner.nextLine();
                        System.out.print("Especialidad: ");
                        String esp = scanner.nextLine();
                        
                        Barbero b = new Barbero(id, nombre, apellido, dni, tel, esp, 20.0);
                        central.agregarBarbero(b);
                    }
                    case 2 -> {
                        System.out.print("ID del Servicio: ");
                        int idS = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nombre del Servicio (ej. Corte Degradado): ");
                        String nomS = scanner.nextLine();
                        System.out.print("Precio Base: ");
                        double precio = scanner.nextDouble();
                        System.out.print("Duración en minutos: ");
                        int duracion = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Tipo específico de corte: ");
                        String tipo = scanner.nextLine();

                        CorteCabello corte = new CorteCabello(idS, nomS, precio, duracion, tipo);
                        central.agregarServicio(corte);
                    }
                    case 3 -> central.listarPersonal();
                    case 4 -> central.listarServicios();
                    case 5 -> {
                        System.out.println("Saliendo del sistema ALEXSTYLE Manager...");
                        ejecutando = false;
                    }
                    default -> System.out.println("Opción no válida. Por favor, ingrese un número del 1 al 5.");
                }

            } catch (InputMismatchException e) {
                // Manejo de la excepción exigido por la consigna ante ingreso de letras o símbolos
                System.out.println("[ERROR DE ENTRADA] Debe ingresar estrictamente un número entero para seleccionar las opciones del menú.");
                scanner.nextLine(); // Limpiar la entrada errónea del scanner
            }
        }
        scanner.close();
    }
}