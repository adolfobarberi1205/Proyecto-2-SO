/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filesystem;

/**
 *
 * @author user
 */
public class GestorArchivos {

    private Disco disco;
    private Directorio raiz;
    private TablaAsignacion tabla;

    public GestorArchivos(Disco disco, Directorio raiz, TablaAsignacion tabla) {
        this.disco = disco;
        this.raiz = raiz;
        this.tabla = tabla;
    }

    public Archivo crearArchivo(String nombre, int tamanoEnBloques,
                                int idProcesoCreador, String color) {

        if (!disco.hayEspacioDisponible(tamanoEnBloques)) {
            return null; // sin espacio
        }

        Archivo archivo = new Archivo(nombre, raiz, tamanoEnBloques,
                                      idProcesoCreador, color);

        int primer = -1;
        int anterior = -1;

        for (int i = 0; i < tamanoEnBloques; i++) {
            int idx = disco.asignarBloqueLibre();
            if (idx == -1) {
                // algo raro pasó, liberamos lo que ya se asignó
                if (primer != -1) {
                    disco.liberarCadena(primer);
                }
                return null;
            }

            Bloque b = disco.getBloque(idx);
            b.setArchivo(archivo);

            if (primer == -1) {
                primer = idx;
            } else {
                disco.getBloque(anterior).setSiguiente(idx);
            }
            anterior = idx;
        }

        archivo.setIndicePrimerBloque(primer);
        archivo.setBloquesAsignados(tamanoEnBloques);

        raiz.agregarHijo(archivo);
        tabla.registrarArchivo(archivo);

        return archivo;
    }

    public boolean eliminarArchivo(String nombreArchivo) {
        Archivo archivo = buscarArchivoPorNombre(nombreArchivo);
        if (archivo == null) {
            return false;
        }

        int primer = archivo.getIndicePrimerBloque();
        if (primer != -1) {
            disco.liberarCadena(primer);
        }

        Directorio padre = archivo.getPadre();
        if (padre != null) {
            padre.eliminarHijo(archivo);
        }

        tabla.eliminarArchivo(archivo);
        return true;
    }

    public Archivo buscarArchivoPorNombre(String nombreArchivo) {
        return buscarEnDirectorio(raiz, nombreArchivo);
    }

    private Archivo buscarEnDirectorio(Directorio dir, String nombreArchivo) {
        for (int i = 0; i < dir.getNumHijos(); i++) {
            NodoFS hijo = dir.getHijo(i);
            if (hijo == null) continue;

            if (hijo.esArchivo()) {
                if (hijo.getNombre().equals(nombreArchivo)) {
                    return (Archivo) hijo;
                }
            } else {
                Archivo encontrado = buscarEnDirectorio((Directorio) hijo, nombreArchivo);
                if (encontrado != null) return encontrado;
            }
        }
        return null;
    }
}