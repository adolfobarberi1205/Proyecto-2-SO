/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

/**
 *
 * @author user
 */

import filesystem.Directorio;
import filesystem.NodoFS;
import filesystem.Archivo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class pArbol extends JPanel {

    private Directorio raiz;
    private JTree arbol;

    public pArbol(Directorio raiz) {
        this.raiz = raiz;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        DefaultMutableTreeNode nodoRoot = construirNodoDirectorio(raiz);
        arbol = new JTree(nodoRoot);

        // Árbol al centro
        add(new JScrollPane(arbol), BorderLayout.CENTER);

        // Panel de botones abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevoDir = new JButton("Nuevo directorio");
        JButton btnEliminarDir = new JButton("Eliminar directorio");

        panelBotones.add(btnNuevoDir);
        panelBotones.add(btnEliminarDir);

        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnNuevoDir.addActionListener(e -> crearDirectorio());
        btnEliminarDir.addActionListener(e -> eliminarDirectorioSeleccionado());
    }

    /**
     * Construye recursivamente el nodo gráfico a partir de un Directorio.
     * Guardamos el propio objeto (Directorio/Archivo) como userObject
     * para poder mapear de vuelta en las operaciones.
     */
    private DefaultMutableTreeNode construirNodoDirectorio(Directorio dir) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(dir);

        for (int i = 0; i < dir.getNumHijos(); i++) {
            NodoFS hijo = dir.getHijo(i);
            if (hijo == null) continue;

            if (hijo.esArchivo()) {
                Archivo a = (Archivo) hijo;
                DefaultMutableTreeNode nodoArchivo = new DefaultMutableTreeNode(a);
                nodo.add(nodoArchivo);
            } else {
                nodo.add(construirNodoDirectorio((Directorio) hijo));
            }
        }
        return nodo;
    }

    /**
     * Devuelve el Directorio sobre el cual se debe operar (padre para crear).
     * - Si no hay selección: se usa la raíz.
     * - Si se selecciona un directorio: se usa ese directorio.
     * - Si se selecciona un archivo: se usa su directorio padre.
     */
    private Directorio obtenerDirectorioParaOperacion() {
        TreePath path = arbol.getSelectionPath();
        if (path == null) {
            return raiz;
        }

        DefaultMutableTreeNode nodoSel =
                (DefaultMutableTreeNode) path.getLastPathComponent();
        Object obj = nodoSel.getUserObject();

        if (obj instanceof Directorio) {
            return (Directorio) obj;
        }

        if (obj instanceof NodoFS) {
            NodoFS n = (NodoFS) obj;
            return n.getPadre();
        }

        return raiz;
    }

    /**
     * Crea un nuevo subdirectorio dentro del directorio seleccionado
     * (o en la raíz si nada está seleccionado).
     */
    private void crearDirectorio() {
        Directorio padre = obtenerDirectorioParaOperacion();
        if (padre == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo determinar el directorio padre.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String nombre = JOptionPane.showInputDialog(
                this,
                "Nombre del nuevo directorio:"
        );
        if (nombre == null) {
            return; // cancelado
        }
        nombre = nombre.trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre no puede estar vacío.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Verificamos que no exista ya un hijo con ese nombre
        if (padre.buscarPorNombre(nombre) != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ya existe un nodo con ese nombre en este directorio.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Directorio nuevo = new Directorio(nombre, padre);
        boolean ok = padre.agregarHijo(nuevo);
        if (!ok) {
            JOptionPane.showMessageDialog(
                    this,
                    "El directorio padre está lleno (MAX_HIJOS alcanzado).",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        actualizar();
    }

    /**
     * Elimina el directorio seleccionado (no permite eliminar la raíz).
     */
    private void eliminarDirectorioSeleccionado() {
        TreePath path = arbol.getSelectionPath();
        if (path == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un directorio a eliminar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        DefaultMutableTreeNode nodoSel =
                (DefaultMutableTreeNode) path.getLastPathComponent();
        Object obj = nodoSel.getUserObject();

        if (!(obj instanceof NodoFS)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selección inválida.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        NodoFS nodoFS = (NodoFS) obj;

        if (!(nodoFS instanceof Directorio)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo se pueden eliminar directorios desde este panel.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Directorio dir = (Directorio) nodoFS;

        if (dir == raiz) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se puede eliminar el directorio raíz.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Directorio padre = dir.getPadre();
        if (padre == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el directorio padre.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int resp = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea eliminar el directorio \"" + dir.getNombre() +
                        "\" y todo su contenido?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }

        padre.eliminarHijo(dir);
        actualizar();
    }

    /**
     * Reconstruye el árbol visual a partir de la estructura en memoria.
     */
    public void actualizar() {
        DefaultMutableTreeNode nodoRoot = construirNodoDirectorio(raiz);
        arbol.setModel(new DefaultTreeModel(nodoRoot));
    }
}
