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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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

        add(new JScrollPane(arbol), BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode construirNodoDirectorio(Directorio dir) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(dir.getNombre());

        for (int i = 0; i < dir.getNumHijos(); i++) {
            NodoFS hijo = dir.getHijo(i);
            if (hijo == null) continue;

            if (hijo.esArchivo()) {
                Archivo a = (Archivo) hijo;
                nodo.add(new DefaultMutableTreeNode(a.getNombre()));
            } else {
                nodo.add(construirNodoDirectorio((Directorio) hijo));
            }
        }
        return nodo;
    }

    public void actualizar() {
        DefaultMutableTreeNode nodoRoot = construirNodoDirectorio(raiz);
        arbol.setModel(new DefaultTreeModel(nodoRoot));
    }
}