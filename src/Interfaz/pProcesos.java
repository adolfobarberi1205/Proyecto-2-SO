/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

/**
 *
 * @author user
 */
import procesos.EstadoProceso;
import procesos.Planificador;
import procesos.Proceso;
import procesos.pCSCAN;
import procesos.pFIFO;
import procesos.pSCAN;
import procesos.pSSTF;
import procesos.PoliticaPlanificacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class pProcesos extends JPanel {

    private Planificador planificador;
    private VentanaPrincipal ventana;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> comboPolitica;
    private int siguienteId = 1;

    public pProcesos(Planificador planificador, VentanaPrincipal ventana) {
        this.planificador = planificador;
        this.ventana = ventana;
        initUI();
        actualizarTabla();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Operación", "Archivo", "Posición", "Tamaño", "Estado"},
                0
        );
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgregar = new JButton("Agregar proceso");
        JButton btnPlanificar = new JButton("Planificar siguiente");

        comboPolitica = new JComboBox<>(
                new String[]{"FIFO", "SSTF", "SCAN", "C-SCAN"}
        );

        panelControles.add(new JLabel("Política:"));
        panelControles.add(comboPolitica);
        panelControles.add(btnAgregar);
        panelControles.add(btnPlanificar);

        add(panelControles, BorderLayout.SOUTH);

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProcesoDialog();
            }
        });

        btnPlanificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                planificarProceso();
            }
        });
    }

    private void agregarProcesoDialog() {
        String[] opcionesOp = {"CREATE", "READ", "UPDATE", "DELETE"};
        String operacion = (String) JOptionPane.showInputDialog(
                this,
                "Tipo de operación:",
                "Nuevo proceso",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesOp,
                opcionesOp[0]);

        if (operacion == null) return;

        String archivo = JOptionPane.showInputDialog(this,
                "Nombre del archivo objetivo:",
                "archivo" + siguienteId);
        if (archivo == null || archivo.trim().isEmpty()) return;

        String posStr = JOptionPane.showInputDialog(this,
                "Posición en disco (0 - 199):",
                "0");
        if (posStr == null) return;

        int pos;
        try {
            pos = Integer.parseInt(posStr);
            if (pos < 0) pos = 0;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número inválido.");
            return;
        }

        String tamStr = JOptionPane.showInputDialog(this,
                "Tamaño del archivo en bloques:",
                "3");
        if (tamStr == null) return;

        int tam;
        try {
            tam = Integer.parseInt(tamStr);
            if (tam <= 0) tam = 1;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número inválido de bloques.");
            return;
        }

        Proceso p = new Proceso(siguienteId++, operacion, archivo, pos, tam);
        planificador.agregarProceso(p);
        actualizarTabla();
    }

    private PoliticaPlanificacion crearPoliticaSeleccionada() {
        String sel = (String) comboPolitica.getSelectedItem();
        if ("FIFO".equals(sel)) {
            return new pFIFO();
        } else if ("SSTF".equals(sel)) {
            return new pSSTF();
        } else if ("SCAN".equals(sel)) {
            return new pSCAN();
        } else {
            return new pCSCAN();
        }
    }

    private void planificarProceso() {
        PoliticaPlanificacion politica = crearPoliticaSeleccionada();
        Proceso p = planificador.planificar(politica);

        if (p == null) {
            JOptionPane.showMessageDialog(this, "No hay procesos en la cola.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Seleccionado: " + p.toString() +
                        "\nPolítica: " + politica.getNombre());

        p.setEstado(EstadoProceso.TERMINADO);

        // Ejecuta la acción real (CREATE, DELETE, etc.) en el sistema de archivos
        ventana.ejecutarAccionProceso(p);

        actualizarTabla();
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);

        Proceso[] cola = planificador.getCola();
        int n = planificador.getNumProcesos();

        for (int i = 0; i < n; i++) {
            Proceso p = cola[i];
            if (p != null) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getOperacion(),
                        p.getArchivoObjetivo(),
                        p.getPosicionObjetivo(),
                        p.getTamanoEnBloques(),
                        p.getEstado()
                });
            }
        }
    }
}