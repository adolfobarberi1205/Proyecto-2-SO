/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procesos;

/**
 *
 * @author user
 */
public class Proceso {

    private int id;
    private String operacion;        // CREATE, READ, UPDATE, DELETE
    private String archivoObjetivo;  // nombre del archivo
    private EstadoProceso estado;

    // posición en el "disco" para simular movimiento de cabezal
    private int posicionObjetivo;

    public Proceso(int id, String operacion, String archivoObjetivo, int posicionObjetivo) {
        this.id = id;
        this.operacion = operacion;
        this.archivoObjetivo = archivoObjetivo;
        this.posicionObjetivo = posicionObjetivo;
        this.estado = EstadoProceso.NUEVO;
    }

    public int getId() {
        return id;
    }

    public String getOperacion() {
        return operacion;
    }

    public String getArchivoObjetivo() {
        return archivoObjetivo;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public void setEstado(EstadoProceso estado) {
        this.estado = estado;
    }

    public int getPosicionObjetivo() {
        return posicionObjetivo;
    }

    public void setPosicionObjetivo(int posicionObjetivo) {
        this.posicionObjetivo = posicionObjetivo;
    }

    @Override
    public String toString() {
        return "P" + id + " [" + operacion + " " + archivoObjetivo +
               " @ " + posicionObjetivo + ", " + estado + "]";
    }
}