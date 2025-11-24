/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procesos;

/**
 *
 * @author user
 */
public class pFIFO implements PoliticaPlanificacion {

    @Override
    public int seleccionarIndice(Proceso[] cola, int numProcesos, int posicionCabeza) {
        if (numProcesos == 0) return -1;
        return 0; // siempre el primero que llegó
    }

    @Override
    public String getNombre() {
        return "FIFO";
    }
}