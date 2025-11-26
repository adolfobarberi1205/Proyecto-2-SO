/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaz;

/**
 *
 * @author user
 */
import filesystem.Bloque;
import filesystem.Disco;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class pDisco extends JPanel {

    private Disco disco;

    public pDisco(Disco disco) {
        this.disco = disco;
        setPreferredSize(new Dimension(800, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Bloque[] bloques = disco.getTodosLosBloques();
        int n = bloques.length;

        int columnas = 20; // celdas por fila
        int ancho = 30;
        int alto = 18;
        int margin = 4;

        for (int i = 0; i < n; i++) {
            int fila = i / columnas;
            int col = i % columnas;

            int x = margin + col * (ancho + 2);
            int y = margin + fila * (alto + 2);

            if (bloques[i].isOcupado()) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            g.fillRect(x, y, ancho, alto);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, ancho, alto);
            g.drawString(String.valueOf(i), x + 3, y + alto - 4);
        }
    }public void actualizar() {
        repaint();
    }
}
