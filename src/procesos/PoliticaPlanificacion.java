/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procesos;

/**
 *
 * @author user
 */
public interface PoliticaPlanificacion {

    /**
     * Devuelve el índice del proceso elegido dentro de la cola.
     * @param cola          arreglo con los procesos en espera
     * @param numProcesos   cantidad válida en la cola
     * @param posicionCabeza posición actual del cabezal del disco
     * @return índice del proceso seleccionado o -1 si no hay
     */
    int seleccionarIndice(Proceso[] cola, int numProcesos, int posicionCabeza);

    String getNombre();
}
