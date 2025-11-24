/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filesystem;

/**
 *
 * @author user
 */
public class Bloque {

    // true = está en uso, false = libre
    private boolean ocupado;

    // índice del siguiente bloque de la lista encadenada, -1 si es el último
    private int siguiente;

    // Referencia opcional al archivo dueño (útil para la GUI)
    private Archivo archivo;

    public Bloque() {
        this.ocupado = false;
        this.siguiente = -1;
        this.archivo = null;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public int getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(int siguiente) {
        this.siguiente = siguiente;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }
}