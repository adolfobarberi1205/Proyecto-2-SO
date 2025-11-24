/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filesystem;

/**
 *
 * @author user
 */
public class Archivo extends NodoFS {

    // Tamaño lógico solicitado (en bloques)
    private int tamanoEnBloques;

    // Índice del primer bloque en el disco (lista encadenada)
    private int indicePrimerBloque;

    // Cantidad real de bloques asignados (por si luego cambia)
    private int bloquesAsignados;

    // ID del proceso que lo creó (para mostrar en la tabla)
    private int idProcesoCreador;

    // Color opcional para la interfaz (ej: "#FF0000")
    private String color;

    public Archivo(String nombre, Directorio padre,
                   int tamanoEnBloques,
                   int idProcesoCreador,
                   String color) {
        super(nombre, padre);
        this.tamanoEnBloques = tamanoEnBloques;
        this.idProcesoCreador = idProcesoCreador;
        this.color = color;
        this.indicePrimerBloque = -1; // aún no asignado
        this.bloquesAsignados = 0;
    }

    @Override
    public boolean esArchivo() {
        return true;
    }

    public int getTamanoEnBloques() {
        return tamanoEnBloques;
    }

    public void setTamanoEnBloques(int tamanoEnBloques) {
        this.tamanoEnBloques = tamanoEnBloques;
    }

    public int getIndicePrimerBloque() {
        return indicePrimerBloque;
    }

    public void setIndicePrimerBloque(int indicePrimerBloque) {
        this.indicePrimerBloque = indicePrimerBloque;
    }

    public int getBloquesAsignados() {
        return bloquesAsignados;
    }

    public void setBloquesAsignados(int bloquesAsignados) {
        this.bloquesAsignados = bloquesAsignados;
    }

    public int getIdProcesoCreador() {
        return idProcesoCreador;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
