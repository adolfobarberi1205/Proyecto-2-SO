/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

/**
 *
 * @author user
 */
import filesystem.Disco;
import filesystem.Directorio;
import filesystem.TablaAsignacion;
import procesos.Planificador;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class VentanaPrincipal extends JFrame {

    private Disco disco;
    private Directorio raiz;
    private TablaAsignacion tablaAsignacion;
    private Planificador planificador;

    public VentanaPrincipal(Disco disco,
                            Directorio raiz,
                            TablaAsignacion tablaAsignacion,
                            Planificador planificador) {

        this.disco = disco;
        this.raiz = raiz;
        this.tablaAsignacion = tablaAsignacion;
        this.planificador = planificador;

        initUI();
    }

    private void initUI() {
        setTitle("Simulador de Sistema de Archivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Árbol", new pArbol(raiz));
        tabs.addTab("Disco", new pDisco(disco));
        tabs.addTab("Procesos", new pProcesos(planificador));
        tabs.addTab("Tabla de Asignación", new pTablaAsignacion(tablaAsignacion));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabs, BorderLayout.CENTER);
    }
}
