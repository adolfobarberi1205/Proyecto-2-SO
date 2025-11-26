package Interfaz;

import procesos.Planificador;
import procesos.Proceso;
import procesos.EstadoProceso;
import procesos.PoliticaPlanificacion;
import procesos.pFIFO;
import procesos.pSSTF;
import procesos.pSCAN;
import procesos.pCSCAN;
import simuladorsistemaarchivos.TipoUsuario;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class pProcesos extends JPanel {

    private Planificador planificador;
    private VentanaPrincipal ventana;

    private TipoUsuario tipoUsuario;
    private int usuarioId;

    private JTable tablaProcesos;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboPoliticas;
    private JButton btnAgregar;
    private JButton btnPlanificar;

    private int contadorId = 1;

    // Constructor con 2 parámetros: Planificador + VentanaPrincipal
    public pProcesos(Planificador planificador, VentanaPrincipal ventana) {
        this.planificador = planificador;
        this.ventana = ventana;

        // Tomamos la info de modos de usuario desde la ventana
        this.tipoUsuario = ventana.getTipoUsuario();
        this.usuarioId = ventana.getUsuarioId();

        initUI();
        actualizarTabla();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Operación", "Archivo", "Posición", "Tamaño", "Estado"},
                0
        );
        tablaProcesos = new JTable(modeloTabla);
        add(new JScrollPane(tablaProcesos), BorderLayout.CENTER);

        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT));

        comboPoliticas = new JComboBox<>(new String[]{"FIFO", "SSTF", "SCAN", "C-SCAN"});
        panelControles.add(comboPoliticas);

        btnAgregar = new JButton("Agregar proceso");
        btnPlanificar = new JButton("Planificar siguiente");

        panelControles.add(btnAgregar);
        panelControles.add(btnPlanificar);

        add(panelControles, BorderLayout.SOUTH);

        // Restricción: el USUARIO no puede cambiar la política
        if (tipoUsuario == TipoUsuario.USUARIO) {
            comboPoliticas.setEnabled(false);
        }

        btnAgregar.addActionListener(e -> agregarProcesoDialog());
        btnPlanificar.addActionListener(e -> planificarProceso());
    }

    private void agregarProcesoDialog() {
        // Operaciones permitidas según el tipo de usuario
        String[] opcionesOperacion;

        if (tipoUsuario == TipoUsuario.ADMINISTRADOR) {
            opcionesOperacion = new String[]{"CREATE", "READ", "UPDATE", "DELETE"};
        } else {
            // Usuario normal: NO puede crear ni eliminar archivos
            opcionesOperacion = new String[]{"READ", "UPDATE"};
        }

        String operacion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione operación:",
                "Nuevo Proceso",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcionesOperacion,
                opcionesOperacion[0]
        );

        if (operacion == null) {
            return; // cancelado
        }

        String nombreArchivo = JOptionPane.showInputDialog(
                this,
                "Nombre del archivo objetivo:"
        );
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return;
        }

        String posStr = JOptionPane.showInputDialog(
                this,
                "Posición del disco (0-199):"
        );
        if (posStr == null) {
            return;
        }

        int posicion;
        try {
            posicion = Integer.parseInt(posStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Posición inválida.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String tamStr = JOptionPane.showInputDialog(
                this,
                "Tamaño en bloques (para CREATE/UPDATE, mínimo 1):"
        );
        if (tamStr == null) {
            return;
        }

        int tam;
        try {
            tam = Integer.parseInt(tamStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tamaño inválido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (tam <= 0) {
            tam = 1;
        }

        int idProceso = contadorId++;
        Proceso p = new Proceso(idProceso, operacion, nombreArchivo, posicion, tam);
        planificador.agregarProceso(p);

        actualizarTabla();
    }

    private void planificarProceso() {
        PoliticaPlanificacion politica = crearPoliticaSeleccionada();

        if (politica == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se ha podido crear la política de planificación.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Proceso p = planificador.planificar(politica);
        if (p == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay procesos en la cola.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Proceso seleccionado: " + p.toString(),
                "Planificación",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Marcamos como terminado y ejecutamos la acción en la ventana principal
        p.setEstado(EstadoProceso.TERMINADO);
        ventana.ejecutarAccionProceso(p);

        actualizarTabla();
    }

    private PoliticaPlanificacion crearPoliticaSeleccionada() {
        String seleccion = (String) comboPoliticas.getSelectedItem();
        if (seleccion == null) return null;

        switch (seleccion) {
            case "FIFO":
                return new pFIFO();
            case "SSTF":
                return new pSSTF();
            case "SCAN":
                return new pSCAN();
            case "C-SCAN":
                return new pCSCAN();
            default:
                return null;
        }
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        Proceso[] cola = planificador.getCola();
        int n = planificador.getNumProcesos();

        for (int i = 0; i < n; i++) {
            Proceso p = cola[i];
            if (p != null) {
                modeloTabla.addRow(new Object[]{
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
