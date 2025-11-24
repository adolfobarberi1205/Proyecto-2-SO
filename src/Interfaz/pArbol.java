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
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class pArbol extends JPanel {

    private Directorio raiz;
    private JTree arbol;

    public pArbol(Directorio raiz) {
        this.raiz = raiz;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        DefaultMutableTreeNode nodoRoot = new DefaultMutableTreeNode(raiz.getNombre());
        // Por ahora solo mostramos la raíz, luego llenamos con subdirectorios/archivos
        arbol = new JTree(nodoRoot);

        add(new JScrollPane(arbol), BorderLayout.CENTER);
    }
}