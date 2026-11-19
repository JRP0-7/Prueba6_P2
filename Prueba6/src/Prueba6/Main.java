package Prueba6;

import Prueba6.controlador.Controlador;
import Prueba6.vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        new Controlador(ventana);
        ventana.setVisible(true);
    }
}
