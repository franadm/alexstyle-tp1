package alexstyle;

import alexstyle.controlador.BarberiaControlador;
import alexstyle.vista.MenuConsola;

/** Punto de entrada del sistema ALEXSTYLE Manager (MVC + DAO sobre MySQL). */
public class GestionAlexstyle {
    public static void main(String[] args) {
        BarberiaControlador controlador = new BarberiaControlador();
        new MenuConsola(controlador).iniciar();
    }
}
