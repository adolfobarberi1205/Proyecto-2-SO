/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filesystem;

/**
 *
 * @author user
 */
public class Disco {

    private Bloque[] bloques;

    public Disco(int cantidadBloques) {
        bloques = new Bloque[cantidadBloques];
        for (int i = 0; i < cantidadBloques; i++) {
            bloques[i] = new Bloque();
        }
    }

    public int getCantidadBloques() {
        return bloques.length;
    }

    public Bloque getBloque(int indice) {
        if (indice < 0 || indice >= bloques.length) {
            return null;
        }
        return bloques[indice];
    }

    // Devuelve el índice de un bloque libre, o -1 si no hay
    public int asignarBloqueLibre() {
        for (int i = 0; i < bloques.length; i++) {
            if (!bloques[i].isOcupado()) {
                bloques[i].setOcupado(true);
                bloques[i].setSiguiente(-1);
                bloques[i].setArchivo(null);
                return i;
            }
        }
        return -1;
    }

    // Cuenta si existen al menos "cantidad" bloques libres
    public boolean hayEspacioDisponible(int cantidad) {
        int libres = 0;
        for (int i = 0; i < bloques.length; i++) {
            if (!bloques[i].isOcupado()) {
                libres++;
                if (libres >= cantidad) {
                    return true;
                }
            }
        }
        return false;
    }

    // Libera una lista encadenada completa a partir del primer bloque
    public void liberarCadena(int indicePrimerBloque) {
        int actual = indicePrimerBloque;
        while (actual != -1) {
            Bloque b = bloques[actual];
            int siguiente = b.getSiguiente();

            b.setOcupado(false);
            b.setSiguiente(-1);
            b.setArchivo(null);

            actual = siguiente;
        }
    }

    public Bloque[] getTodosLosBloques() {
        return bloques;
    }
}
