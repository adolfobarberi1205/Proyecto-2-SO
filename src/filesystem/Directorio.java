/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filesystem;

/**
 *
 * @author user
 */
public class Directorio extends NodoFS {

    public static final int MAX_HIJOS = 100;

    private NodoFS[] hijos;
    private int numHijos;

    public Directorio(String nombre, Directorio padre) {
        super(nombre, padre);
        this.hijos = new NodoFS[MAX_HIJOS];
        this.numHijos = 0;
    }

    public boolean agregarHijo(NodoFS hijo) {
        if (numHijos >= MAX_HIJOS) {
            return false; // lleno
        }
        hijos[numHijos] = hijo;
        numHijos++;
        return true;
    }

    public void eliminarHijo(NodoFS hijo) {
        for (int i = 0; i < numHijos; i++) {
            if (hijos[i] == hijo) {
                hijos[i] = hijos[numHijos - 1];
                hijos[numHijos - 1] = null;
                numHijos--;
                return;
            }
        }
    }

    public NodoFS getHijo(int indice) {
        if (indice < 0 || indice >= numHijos) {
            return null;
        }
        return hijos[indice];
    }

    public int getNumHijos() {
        return numHijos;
    }

    public NodoFS buscarPorNombre(String nombreBuscado) {
        for (int i = 0; i < numHijos; i++) {
            if (hijos[i] != null && hijos[i].getNombre().equals(nombreBuscado)) {
                return hijos[i];
            }
        }
        return null;
    }

    @Override
    public boolean esArchivo() {
        return false;
    }
}