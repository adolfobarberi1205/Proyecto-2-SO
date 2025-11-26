package Interfaz;

import filesystem.Archivo;
import filesystem.Bloque;
import filesystem.Disco;
import filesystem.Directorio;
import filesystem.GestorArchivos;
import filesystem.TablaAsignacion;
import procesos.Planificador;
import procesos.Proceso;
import simuladorsistemaarchivos.TipoUsuario;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class VentanaPrincipal extends JFrame {

    // Archivo donde se guarda/carga el estado
    private static final String ARCHIVO_ESTADO = "estado_fs.txt";

    private Disco disco;
    private Directorio raiz;
    private TablaAsignacion tabla;
    private Planificador planificador;

    // Modos de usuario
    private TipoUsuario tipoUsuario;
    private int usuarioId;

    private pArbol panelArbol;
    private pDisco panelDisco;
    private pProcesos panelProcesos;
    private pTablaAsignacion panelTabla;

    public VentanaPrincipal(Disco disco,
                            Directorio raiz,
                            TablaAsignacion tabla,
                            Planificador planificador,
                            TipoUsuario tipoUsuario,
                            int usuarioId) {
        this.disco = disco;
        this.raiz = raiz;
        this.tabla = tabla;
        this.planificador = planificador;
        this.tipoUsuario = tipoUsuario;
        this.usuarioId = usuarioId;

        initUI();
    }

    private void initUI() {
        setTitle("Simulador de Sistema de Archivos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout principal
        getContentPane().setLayout(new BorderLayout());

        // Panel superior con botones de guardar/cargar
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnGuardar = new JButton("Guardar estado");
        JButton btnCargar = new JButton("Cargar estado");
        panelTop.add(btnGuardar);
        panelTop.add(btnCargar);
        add(panelTop, BorderLayout.NORTH);

        // Pestañas principales
        JTabbedPane tabs = new JTabbedPane();

        panelArbol = new pArbol(raiz);
        panelDisco = new pDisco(disco);
        panelTabla = new pTablaAsignacion(tabla);
        panelProcesos = new pProcesos(planificador, this);

        tabs.addTab("Árbol", panelArbol);
        tabs.addTab("Disco", panelDisco);
        tabs.addTab("Procesos", panelProcesos);
        tabs.addTab("Tabla de Asignación", panelTabla);

        add(tabs, BorderLayout.CENTER);

        // Listeners
        btnGuardar.addActionListener(e -> guardarEstado());
        btnCargar.addActionListener(e -> cargarEstado());
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * Ejecuta la acción asociada al proceso seleccionado por el planificador.
     */
    public void ejecutarAccionProceso(Proceso p) {
        if (p == null) {
            return;
        }

        String operacion = p.getOperacion();
        String nombreArchivo = p.getArchivoObjetivo();
        int tam = p.getTamanoEnBloques();
        if (tam <= 0) {
            tam = 1;
        }

        // Restricción de permisos por tipo de usuario
        if (tipoUsuario == TipoUsuario.USUARIO) {
            if ("CREATE".equalsIgnoreCase(operacion) || "DELETE".equalsIgnoreCase(operacion)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Modo USUARIO: no tiene permisos para crear o eliminar archivos.",
                        "Permiso denegado",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        GestorArchivos gestor = new GestorArchivos(disco, raiz, tabla);

        switch (operacion.toUpperCase()) {
            case "CREATE": {
                Archivo creado = gestor.crearArchivo(nombreArchivo, tam, usuarioId, "#FFCC66");
                if (creado == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No hay espacio suficiente para crear el archivo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Archivo creado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            }
            case "DELETE": {
                boolean ok = gestor.eliminarArchivo(nombreArchivo);
                if (!ok) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se encontró el archivo a eliminar.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Archivo eliminado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;
            }
            case "READ": {
                JOptionPane.showMessageDialog(
                        this,
                        "Simulación de lectura del archivo: " + nombreArchivo,
                        "READ",
                        JOptionPane.INFORMATION_MESSAGE
                );
                break;
            }
            case "UPDATE": {
                JOptionPane.showMessageDialog(
                        this,
                        "Simulación de actualización del archivo: " + nombreArchivo,
                        "UPDATE",
                        JOptionPane.INFORMATION_MESSAGE
                );
                break;
            }
            default:
                JOptionPane.showMessageDialog(
                        this,
                        "Operación no reconocida: " + operacion,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                break;
        }

        // Actualizar vistas
        panelArbol.actualizar();
        panelDisco.actualizar();
        panelTabla.actualizar();
    }

    // ===================== PERSISTENCIA =======================

    /**
     * Guarda el estado del sistema de archivos en un archivo de texto.
     * Formato:
     *   DISCO <numBloques>
     *   DIR /root/dir1/subdir
     *   FILE /root/archivo 3 7 #FFCC66
     */
    private void guardarEstado() {
        File f = new File(ARCHIVO_ESTADO);
        try (PrintWriter out = new PrintWriter(new FileWriter(f))) {

            // Información del disco
            Bloque[] bloques = disco.getTodosLosBloques();
            out.println("DISCO " + bloques.length);

            // Directorios (excluyendo la raíz)
            guardarDirectoriosRec(raiz, out);

            // Archivos
            guardarArchivosRec(raiz, out);

            JOptionPane.showMessageDialog(
                    this,
                    "Estado guardado en: " + f.getAbsolutePath(),
                    "Guardar estado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar estado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void guardarDirectoriosRec(Directorio dir, PrintWriter out) {
        // No escribimos la raíz explícitamente, solo sus hijos
        int numHijos = dir.getNumHijos();
        for (int i = 0; i < numHijos; i++) {
            filesystem.NodoFS hijo = dir.getHijo(i);
            if (hijo == null) continue;

            if (!hijo.esArchivo()) {
                Directorio sub = (Directorio) hijo;
                // Guardamos su ruta completa
                out.println("DIR " + sub.getRutaCompleta());
                // Recursión
                guardarDirectoriosRec(sub, out);
            }
        }
    }

    private void guardarArchivosRec(Directorio dir, PrintWriter out) {
        int numHijos = dir.getNumHijos();
        for (int i = 0; i < numHijos; i++) {
            filesystem.NodoFS hijo = dir.getHijo(i);
            if (hijo == null) continue;

            if (hijo.esArchivo()) {
                Archivo a = (Archivo) hijo;
                out.println("FILE " + a.getRutaCompleta()
                        + " " + a.getTamanoEnBloques()
                        + " " + a.getIdProcesoCreador()
                        + " " + a.getColor());
            } else {
                guardarArchivosRec((Directorio) hijo, out);
            }
        }
    }

    /**
     * Carga el estado previamente guardado desde ARCHIVO_ESTADO.
     */
    private void cargarEstado() {
        File f = new File(ARCHIVO_ESTADO);
        if (!f.exists()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el archivo de estado: " + f.getAbsolutePath(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Limpiamos estructuras actuales
        limpiarSistema();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                String[] partes = linea.split(" ");
                if (partes.length == 0) continue;

                String tipo = partes[0];

                if ("DISCO".equals(tipo)) {
                    // Podríamos verificar tamaño, pero asumimos que es correcto
                    continue;
                } else if ("DIR".equals(tipo)) {
                    if (partes.length < 2) continue;
                    String ruta = partes[1];
                    crearDirectorioPorRuta(ruta);
                } else if ("FILE".equals(tipo)) {
                    if (partes.length < 5) continue;
                    String ruta = partes[1];
                    int tam;
                    int idCreador;
                    try {
                        tam = Integer.parseInt(partes[2]);
                        idCreador = Integer.parseInt(partes[3]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    String color = partes[4];

                    String nombreArchivo = extraerNombreDeRuta(ruta);

                    GestorArchivos gestor = new GestorArchivos(disco, raiz, tabla);
                    gestor.crearArchivo(nombreArchivo, tam, idCreador, color);
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Estado cargado desde: " + f.getAbsolutePath(),
                    "Cargar estado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar estado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Refrescamos vistas
        panelArbol.actualizar();
        panelDisco.actualizar();
        panelTabla.actualizar();
    }

    /**
     * Limpia disco, directorios (hijos de root) y tabla de asignación.
     * Usa métodos limpiar() que agregaremos en Directorio y TablaAsignacion.
     */
    private void limpiarSistema() {
        // Limpiar disco
        Bloque[] bloques = disco.getTodosLosBloques();
        for (int i = 0; i < bloques.length; i++) {
            Bloque b = bloques[i];
            if (b != null) {
                b.setOcupado(false);
                b.setSiguiente(-1);
                b.setArchivo(null);
            }
        }

        // Limpiar directorios (hijos de la raíz)
        raiz.limpiar();

        // Limpiar tabla de asignación
        tabla.limpiar();
    }

    /**
     * Crea todos los directorios necesarios para que exista la ruta dada,
     * por ejemplo: /root/docs/2025/proyecto
     */
    private void crearDirectorioPorRuta(String ruta) {
        if (ruta == null || ruta.isEmpty()) {
            return;
        }
        // Esperamos rutas tipo /root/sub1/sub2
        String normalizada = ruta.trim();
        if (!normalizada.startsWith("/")) {
            return;
        }

        String[] partes = normalizada.split("/");
        if (partes.length < 2) {
            return;
        }

        // partes[0] = "" (antes del primer /)
        // partes[1] = "root"
        Directorio actual = raiz;

        for (int i = 2; i < partes.length; i++) {
            String nombreDir = partes[i];
            if (nombreDir == null || nombreDir.isEmpty()) continue;

            // Buscamos si ya existe un hijo con ese nombre
            filesystem.NodoFS existente = actual.buscarPorNombre(nombreDir);
            if (existente != null && !existente.esArchivo()) {
                actual = (Directorio) existente;
            } else if (existente == null) {
                Directorio nuevo = new Directorio(nombreDir, actual);
                boolean ok = actual.agregarHijo(nuevo);
                if (!ok) {
                    // Directorio lleno, dejamos de crear esta rama
                    return;
                }
                actual = nuevo;
            } else {
                // Existe un archivo con ese nombre, no podemos convertirlo en directorio
                return;
            }
        }
    }

    private String extraerNombreDeRuta(String ruta) {
        if (ruta == null || ruta.isEmpty()) {
            return "";
        }
        int idx = ruta.lastIndexOf('/');
        if (idx == -1 || idx == ruta.length() - 1) {
            return ruta;
        }
        return ruta.substring(idx + 1);
    }
}
