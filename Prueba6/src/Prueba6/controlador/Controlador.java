package Prueba6.controlador;

import Prueba6.modelo.Explorador;
import Prueba6.modelo.RutaInvalidaException;
import Prueba6.vista.VentanaPrincipal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Controlador implements ActionListener {

    private VentanaPrincipal vista;

    public Controlador(VentanaPrincipal vista) {
        this.vista = vista;
        vista.btnBuscar.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            File ruta = Explorador.validarRuta(vista.txtRuta.getText());
            int prof = Integer.parseInt(vista.txtProfundidad.getText().trim());
            long min = numero(vista.txtMinSize.getText());
            long max = numero(vista.txtMaxSize.getText());
            long desde = fecha(vista.txtDesde.getText());
            long hasta = fecha(vista.txtHasta.getText());

            if(hasta<desde){
                JOptionPane.showMessageDialog(vista,"La fecha final no puede ir antes de la inicial");
                return;
            }
            Explorador modelo = new Explorador();
            modelo.filtrar(vista.txtBusqueda.getText(), vista.txtExtension.getText(),
                    min, max, desde, hasta);

            vista.cargarArbol(ruta, prof);

            modelo.contar(ruta, prof, 1);
            ArrayList<String> resultados = new ArrayList<>();
            modelo.buscar(ruta, prof, 1, resultados);

            String salida = "Estadisticas por extension:\n" + modelo.genResumen();
            salida += "\nSe encontraron " + resultados.size() + " con los valores\n";
            for (String s : resultados) {
                salida += s + "\n";
            }
            vista.txtResultado.setText(salida);
        } catch (RutaInvalidaException ex) {
            vista.mostrarError(ex.getMessage());
        } catch (NumberFormatException ex) {
            vista.mostrarError("Profundidad y tamaños deben ser numeros");
        } catch (ParseException ex) {
            vista.mostrarError("Las fechas deben tener formato aaaa-mm-dd");
        }
    }

    private long numero(String s) {
        if (s.trim().equals("")) {
            return -1;
        }
        return Long.parseLong(s.trim());
    }

    private long fecha(String s) throws ParseException {
        if (s.trim().equals("")) {
            return -1;
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        return formato.parse(s.trim()).getTime();
    }
}
