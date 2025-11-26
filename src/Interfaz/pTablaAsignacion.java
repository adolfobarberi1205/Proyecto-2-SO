/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

/**
 *
 * @author user
 */
import filesystem.TablaAsignacion;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class pTablaAsignacion extends JPanel {

    private TablaAsignacion tablaAsignacion;
    private JTable tabla;
    private DefaultTableModel modelo;

    public pTablaAsignacion(TablaAsignacion tablaAsignacion) {
        this.tablaAsignacion = tablaAsignacion;
        initUI();
        actualizar();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(
                new Object[]{"Archivo", "Bloques", "Primer bloque", "Color", "Proceso creador"},
                0
        );
        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    public void actualizar() {
        modelo.setRowCount(0);
        int size = tablaAsignacion.getSize();

        for (int i = 0; i < size; i++) {
            modelo.addRow(new Object[]{
                    tablaAsignacion.getNombreArchivoEn(i),
                    tablaAsignacion.getCantidadBloquesEn(i),
                    tablaAsignacion.getIndicePrimerBloqueEn(i),
                    tablaAsignacion.getColorEn(i),
                    tablaAsignacion.getIdProcesoCreadorEn(i)
            });
        }
    }
}
