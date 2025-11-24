/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filesystem;

/**
 *
 * @author user
 */
public abstract class NodoFS {

    protected String nombre;
    protected Directorio padre;

    public NodoFS(String nombre, Directorio padre) {
        this.nombre = nombre;
        this.padre = padre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Directorio getPadre() {
        return padre;
    }

    public void setPadre(Directorio padre) {
        this.padre = padre;
    }

    // Ruta tipo /root/dir/subdir/archivo
    public String getRutaCompleta() {
        if (padre == null) {
            return "/" + nombre;
        }
        return padre.getRutaCompleta() + "/" + nombre;
    }

    public abstract boolean esArchivo();
}