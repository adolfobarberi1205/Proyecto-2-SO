/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procesos;

/**
 *
 * @author user
 */
public class Planificador {

    public static final int MAX_PROCESOS = 100;

    private Proceso[] cola;
    private int numProcesos;

    // posición del cabezal del disco (0..N-1)
    private int posicionCabeza;

    public Planificador() {
        cola = new Proceso[MAX_PROCESOS];
        numProcesos = 0;
        posicionCabeza = 0;
    }

    public int getNumProcesos() {
        return numProcesos;
    }

    public int getPosicionCabeza() {
        return posicionCabeza;
    }

    public void setPosicionCabeza(int posicionCabeza) {
        this.posicionCabeza = posicionCabeza;
    }

    public boolean agregarProceso(Proceso p) {
        if (numProcesos >= MAX_PROCESOS) {
            return false;
        }
        p.setEstado(EstadoProceso.LISTO);
        cola[numProcesos] = p;
        numProcesos++;
        return true;
    }

    public Proceso[] getCola() {
        return cola;
    }

    /**
     * Aplica una política (FIFO, SSTF, SCAN, CSCAN) y devuelve
     * el proceso seleccionado, eliminándolo de la cola.
     */
    public Proceso planificar(PoliticaPlanificacion politica) {
        if (numProcesos == 0) {
            return null;
        }

        int indice = politica.seleccionarIndice(cola, numProcesos, posicionCabeza);
        if (indice < 0 || indice >= numProcesos) {
            return null;
        }

        Proceso seleccionado = cola[indice];

        // actualizamos la posición del cabezal
        posicionCabeza = seleccionado.getPosicionObjetivo();

        // sacamos el proceso de la cola (compactando el arreglo)
        cola[indice] = cola[numProcesos - 1];
        cola[numProcesos - 1] = null;
        numProcesos--;

        seleccionado.setEstado(EstadoProceso.EJECUTANDO);
        return seleccionado;
    }
}
