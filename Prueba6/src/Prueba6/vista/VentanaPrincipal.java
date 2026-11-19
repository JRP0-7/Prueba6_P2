package Prueba6.vista;

import java.awt.FlowLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class VentanaPrincipal extends JFrame {

    public JTextField txtRuta;
    public JTextField txtBusqueda;
    public JTextField txtExtension;
    public JTextField txtMinSize;
    public JTextField txtMaxSize;
    public JTextField txtHasta;
    public JTextField txtDesde;
    public JTextField txtProfundidad = new JTextField("0", 3);
    public JButton btnBuscar = new JButton("Buscar");
    public JTextArea txtResultado = new JTextArea();
    public JTree arbol = new JTree(new DefaultTreeModel(null));

    public VentanaPrincipal() {
        setTitle("Gestor de archivos");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lblRaiz = new JLabel("Raiz del directorio:");
        txtRuta = new JTextField(20);
        panelDatos.add(lblRaiz);
        panelDatos.add(txtRuta);

        JLabel lblBuscar = new JLabel("Buscar");
        txtBusqueda = new JTextField(20);
        panelDatos.add(lblBuscar);
        panelDatos.add(txtBusqueda);

        JLabel lblExtension = new JLabel("Extension:");
        txtExtension = new JTextField(5);
        panelDatos.add(lblExtension);
        panelDatos.add(txtExtension);

        JLabel lblMinSize = new JLabel("Min Size:");
        txtMinSize = new JTextField(5);
        panelDatos.add(lblMinSize);
        panelDatos.add(txtMinSize);

        JLabel lblMaxSize = new JLabel("Max Size:");
        txtMaxSize = new JTextField(5);
        panelDatos.add(lblMaxSize);
        panelDatos.add(txtMaxSize);

        JLabel lblDesde = new JLabel("Desde (aaaa-mm-dd):");
        txtDesde = new JTextField(15);
        panelDatos.add(lblDesde);
        panelDatos.add(txtDesde);

        JLabel lblHasta = new JLabel("Hasta:");
        txtHasta = new JTextField(10);
        panelDatos.add(lblHasta);
        panelDatos.add(txtHasta);

        JLabel lblProfundidad = new JLabel("Profundidad (0=ilimitada):");
        txtProfundidad = new JTextField(5);
        panelDatos.add(lblProfundidad);
        panelDatos.add(txtProfundidad);

        btnBuscar = new JButton("Buscar");
        panelDatos.add(btnBuscar);

        txtResultado.setEditable(false);

        JSplitPane centro = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(arbol), new JScrollPane(txtResultado));
        centro.setDividerLocation(350);

        add(panelDatos, "North");
        add(centro, "Center");
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void cargarArbol(File raiz, int prof) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(raiz.getAbsolutePath());
        agregarElementos(nodo, raiz, prof, 1);
        arbol.setModel(new DefaultTreeModel(nodo));
    }

    private void agregarElementos(DefaultMutableTreeNode padre, File f, int prof, int nivel) {
        File[] hijos = f.listFiles();
        if (hijos == null) {
            return;
        }

        for (File h : hijos) {
            if (h.isDirectory()) {
                DefaultMutableTreeNode carpeta = new DefaultMutableTreeNode("[Carpeta] " + h.getName());
                padre.add(carpeta);
                if (prof == 0 || nivel < prof) {
                    agregarElementos(carpeta, h, prof, nivel + 1);
                }
            } else {
                padre.add(new DefaultMutableTreeNode(h.getName()));
            }
        }
    }
}
