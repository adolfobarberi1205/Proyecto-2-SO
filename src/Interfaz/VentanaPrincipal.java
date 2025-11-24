/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

/**
 *
 * @author user
 */
import filesystem.Archivo;
import filesystem.Disco;
import filesystem.Directorio;
import filesystem.GestorArchivos;
import filesystem.TablaAsignacion;
import procesos.Planificador;
import procesos.Proceso;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;

public class VentanaPrincipal extends JFrame {

    private Disco disco;
    private Directorio raiz;
    private TablaAsignacion tablaAsignacion;
    private Planificador planificador;
    private GestorArchivos gestor;

    private pArbol panelArbol;
    private pDisco panelDisco;
    private pProcesos panelProcesos;
    private pTablaAsignacion panelTabla;

    public VentanaPrincipal(Disco disco,
                            Directorio raiz,
                            TablaAsignacion tablaAsignacion,
                            Planificador planificador) {

        this.disco = disco;
        this.raiz = raiz;
        this.tablaAsignacion = tablaAsignacion;
        this.planificador = planificador;
        this.gestor = new GestorArchivos(disco, raiz, tablaAsignacion);

        initUI();
    }

    private void initUI() {
        setTitle("Simulador de Sistema de Archivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        panelArbol = new pArbol(raiz);
        panelDisco = new pDisco(disco);
        panelTabla = new pTablaAsignacion(tablaAsignacion);
        panelProcesos = new pProcesos(planificador, this);

        tabs.addTab("Árbol", panelArbol);
        tabs.addTab("Disco", panelDisco);
        tabs.addTab("Procesos", panelProcesos);
        tabs.addTab("Tabla de Asignación", panelTabla);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    public void ejecutarAccionProceso(Proceso p) {
        String op = p.getOperacion().toUpperCase();

        if ("CREATE".equals(op)) {
            int tam = p.getTamanoEnBloques();
            if (tam <= 0) tam = 1;

            Archivo nuevo = gestor.crearArchivo(
                    p.getArchivoObjetivo(),
                    tam,
                    p.getId(),
                    "#FFCC66"
            );

            if (nuevo == null) {
                JOptionPane.showMessageDialog(this,
                        "No hay espacio suficiente en disco para crear el archivo.",
                        "Sin espacio",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Archivo creado: " + nuevo.getNombre() +
                        " (" + tam + " bloques).");
            }

        } else if ("DELETE".equals(op)) {

            boolean ok = gestor.eliminarArchivo(p.getArchivoObjetivo());
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el archivo: " + p.getArchivoObjetivo(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Archivo eliminado: " + p.getArchivoObjetivo());
            }

        } else if ("READ".equals(op)) {
            JOptionPane.showMessageDialog(this,
                    "Simulación de lectura del archivo: " + p.getArchivoObjetivo());

        } else if ("UPDATE".equals(op)) {
            JOptionPane.showMessageDialog(this,
                    "Simulación de actualización del archivo: " + p.getArchivoObjetivo());
        }

        // Actualizamos vistas
        panelArbol.actualizar();
        panelDisco.actualizar();
        panelTabla.actualizar();
    }
}