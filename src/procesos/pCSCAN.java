/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procesos;

/**
 *
 * @author user
 */
public class pCSCAN implements PoliticaPlanificacion {

    private int maxPista = 199; // puedes ajustarlo según tu disco

    @Override
    public int seleccionarIndice(Proceso[] cola, int numProcesos, int posicionCabeza) {
        if (numProcesos == 0) return -1;

        int elegido = -1;
        int mejorDist = Integer.MAX_VALUE;

        // buscamos solo hacia la derecha (como C-SCAN)
        for (int i = 0; i < numProcesos; i++) {
            int pos = cola[i].getPosicionObjetivo();
            int dist;

            if (pos >= posicionCabeza) {
                dist = pos - posicionCabeza;
            } else {
                // cuando "regresa" al inicio
                dist = (maxPista - posicionCabeza) + pos;
            }

            if (dist < mejorDist) {
                mejorDist = dist;
                elegido = i;
            }
        }

        return elegido;
    }

    @Override
    public String getNombre() {
        return "C-SCAN";
    }
}