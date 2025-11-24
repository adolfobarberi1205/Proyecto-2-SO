/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procesos;

/**
 *
 * @author user
 */
public class pSSTF implements PoliticaPlanificacion {

    @Override
    public int seleccionarIndice(Proceso[] cola, int numProcesos, int posicionCabeza) {
        if (numProcesos == 0) return -1;

        int mejorIndice = 0;
        int mejorDist = Math.abs(cola[0].getPosicionObjetivo() - posicionCabeza);

        for (int i = 1; i < numProcesos; i++) {
            int dist = Math.abs(cola[i].getPosicionObjetivo() - posicionCabeza);
            if (dist < mejorDist) {
                mejorDist = dist;
                mejorIndice = i;
            }
        }
        return mejorIndice;
    }

    @Override
    public String getNombre() {
        return "SSTF";
    }
}