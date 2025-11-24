/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simuladorsistemaarchivos;

/**
 *
 * @author user
 */
import Interfaz.VentanaPrincipal;
import filesystem.Disco;
import filesystem.Directorio;
import filesystem.TablaAsignacion;
import procesos.Planificador;

public class SimuladorSistemaArchivos {

    public static void main(String[] args) {

        // Crear estructuras principales del simulador
        final Disco disco = new Disco(200);          // 200 bloques (ajusta si quieres)
        final Directorio raiz = new Directorio("root", null);
        final TablaAsignacion tabla = new TablaAsignacion();
        final Planificador planificador = new Planificador();

        // Lanzar la GUI en el hilo de eventos
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal v = new VentanaPrincipal(disco, raiz, tabla, planificador);
                v.setVisible(true);
            }
        });
    }
}