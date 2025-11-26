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
import javax.swing.JOptionPane;

public class SimuladorSistemaArchivos {

    public static void main(String[] args) {

        // Crear estructuras principales del simulador
        final Disco disco = new Disco(200);          // 200 bloques (ajusta si quieres)
        final Directorio raiz = new Directorio("root", null);
        final TablaAsignacion tabla = new TablaAsignacion();
        final Planificador planificador = new Planificador();

        // Elegir modo de usuario (Administrador / Usuario)
        String[] opciones = {"Administrador", "Usuario"};
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione el modo de uso del simulador",
                "Modo de usuario",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        TipoUsuario tipoUsuario;
        int usuarioId;

        if (seleccion == 1) {
            // Usuario normal
            tipoUsuario = TipoUsuario.USUARIO;
            usuarioId = 1; // ID simbólico para el usuario normal
        } else {
            // Por defecto, Administrador
            tipoUsuario = TipoUsuario.ADMINISTRADOR;
            usuarioId = 0; // ID simbólico para el administrador
        }

        final TipoUsuario tipoFinal = tipoUsuario;
        final int usuarioIdFinal = usuarioId;

        // Lanzar la GUI en el hilo de eventos
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal v = new VentanaPrincipal(
                        disco,
                        raiz,
                        tabla,
                        planificador,
                        tipoFinal,
                        usuarioIdFinal
                );
                v.setVisible(true);
            }
        });
    }
}
