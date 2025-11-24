/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procesos;

/**
 *
 * @author user
 */
public class pSCAN implements PoliticaPlanificacion {

    private boolean haciaDerecha = true; // dirección del barrido

    @Override
    public int seleccionarIndice(Proceso[] cola, int numProcesos, int posicionCabeza) {
        if (numProcesos == 0) return -1;

        int elegido = -1;
        int mejorDist = Integer.MAX_VALUE;

        // primero buscamos en la dirección actual
        for (int i = 0; i < numProcesos; i++) {
            int pos = cola[i].getPosicionObjetivo();
            int dist = pos - posicionCabeza;

            if (haciaDerecha && dist >= 0 && dist < mejorDist) {
                mejorDist = dist;
                elegido = i;
            } else if (!haciaDerecha && dist <= 0 && -dist < mejorDist) {
                mejorDist = -dist;
                elegido = i;
            }
        }

        // si no hay en esa dirección, cambiamos la dirección
        if (elegido == -1) {
            haciaDerecha = !haciaDerecha;
            return seleccionarIndice(cola, numProcesos, posicionCabeza);
        }

        return elegido;
    }

    @Override
    public String getNombre() {
        return "SCAN";
    }
}