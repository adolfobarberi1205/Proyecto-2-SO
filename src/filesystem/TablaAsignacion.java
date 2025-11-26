/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filesystem;

/**
 *
 * @author user
 */
public class TablaAsignacion {

    public static final int MAX_REGISTROS = 200;

    private Archivo[] archivos;
    private int[] cantidadBloques;
    private int[] indicePrimerBloque;
    private String[] colores;
    private int[] idProcesoCreador;

    private int size;

    public TablaAsignacion() {
        archivos = new Archivo[MAX_REGISTROS];
        cantidadBloques = new int[MAX_REGISTROS];
        indicePrimerBloque = new int[MAX_REGISTROS];
        colores = new String[MAX_REGISTROS];
        idProcesoCreador = new int[MAX_REGISTROS];
        size = 0;
    }
    
        // Limpia completamente la tabla de asignación
    public void limpiar() {
        for (int i = 0; i < size; i++) {
            archivos[i] = null;
            cantidadBloques[i] = 0;
            indicePrimerBloque[i] = -1;
            colores[i] = null;
            idProcesoCreador[i] = -1;
        }
        size = 0;
    }

    public int getSize() {
        return size;
    }

    // Agregar o actualizar una entrada para un archivo
    public void registrarArchivo(Archivo archivo) {
        int index = buscarIndicePorArchivo(archivo);
        if (index == -1) {
            if (size >= MAX_REGISTROS) {
                return; // tabla llena
            }
            index = size;
            archivos[index] = archivo;
            size++;
        }
        cantidadBloques[index] = archivo.getBloquesAsignados();
        indicePrimerBloque[index] = archivo.getIndicePrimerBloque();
        colores[index] = archivo.getColor();
        idProcesoCreador[index] = archivo.getIdProcesoCreador();
    }

    public void eliminarArchivo(Archivo archivo) {
        int index = buscarIndicePorArchivo(archivo);
        if (index == -1) {
            return;
        }
        archivos[index] = archivos[size - 1];
        cantidadBloques[index] = cantidadBloques[size - 1];
        indicePrimerBloque[index] = indicePrimerBloque[size - 1];
        colores[index] = colores[size - 1];
        idProcesoCreador[index] = idProcesoCreador[size - 1];

        archivos[size - 1] = null;
        size--;
    }

    private int buscarIndicePorArchivo(Archivo archivo) {
        for (int i = 0; i < size; i++) {
            if (archivos[i] == archivo) {
                return i;
            }
        }
        return -1;
    }

    // Métodos de acceso para que la GUI construya el JTable
    public Archivo getArchivoEn(int fila) {
        if (fila < 0 || fila >= size) return null;
        return archivos[fila];
    }

    public String getNombreArchivoEn(int fila) {
        Archivo a = getArchivoEn(fila);
        return (a == null) ? "" : a.getNombre();
    }

    public int getCantidadBloquesEn(int fila) {
        if (fila < 0 || fila >= size) return 0;
        return cantidadBloques[fila];
    }

    public int getIndicePrimerBloqueEn(int fila) {
        if (fila < 0 || fila >= size) return -1;
        return indicePrimerBloque[fila];
    }

    public String getColorEn(int fila) {
        if (fila < 0 || fila >= size) return null;
        return colores[fila];
    }

    public int getIdProcesoCreadorEn(int fila) {
        if (fila < 0 || fila >= size) return -1;
        return idProcesoCreador[fila];
    }
}