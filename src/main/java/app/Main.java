package app;

import enums.Conferencia;
import enums.Fase;
import enums.Posicion;
import excepciones.AccesoDatosException;
import excepciones.DorsalDuplicadoException;
import excepciones.EmpateNoPermitidoException;
import excepciones.EntidadDuplicadaException;
import excepciones.EntidadNoEncontradaException;
import excepciones.PartidoYaJugadoException;
import excepciones.PlantelCompletoException;
import gestor.GestorEquipos;
import gestor.GestorPartidos;
import modelo.Entrenador;
import modelo.Equipo;
import modelo.Jugador;
import modelo.Partido;
import modelo.Persona;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Punto de entrada y menu de pruebas del sistema.
 * <p>
 * Cada opcion ejecuta un escenario con casos normales y casos de error. Reglas del menu:
 * <ul>
 *   <li>Nunca se corta: ningun metodo declara throws; cada error esperado se captura,
 *       se informa y el programa sigue.</li>
 *   <li>Cada operacion independiente va en su propio try; los pasos que dependen
 *       unos de otros van juntos en el mismo try.</li>
 *   <li>Solo se capturan excepciones especificas, nunca Exception.</li>
 *   <li>El menu habla unicamente con los gestores: nunca con los DAO directamente.</li>
 * </ul>
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final GestorEquipos gestorEquipos = new GestorEquipos();
    private static final GestorPartidos gestorPartidos = new GestorPartidos();

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    altaEquipos();
                    break;
                case 2:
                    if (hayEquipos()) {
                        listadoGeneral();
                    }
                    break;
                case 3:
                    if (hayEquipos()) {
                        partidos();
                    }
                    break;
                case 4:
                    if (hayEquipos()) {
                        guardarEnBD();
                    }
                    break;
                case 5:
                    operacionesBD();
                    break;
                case 6:
                    altaEquipos();
                    listadoGeneral();
                    partidos();
                    guardarEnBD();
                    operacionesBD();
                    System.out.println("\n========== FIN DEL PROGRAMA ==========");
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n================ MENU DE PRUEBAS ================");
        System.out.println("1. Alta de equipos y jugadores");
        System.out.println("2. Listado general");
        System.out.println("3. Partidos");
        System.out.println("4. Guardar en BD y listar (JDBC)");
        System.out.println("5. Buscar / actualizar / eliminar en BD (JDBC)");
        System.out.println("6. Ejecutar todo");
        System.out.println("0. Salir");
        System.out.print("Opcion: ");
    }

    /** Lee la opcion como texto y la convierte: si no es un numero devuelve -1 (opcion invalida). */
    private static int leerOpcion() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static boolean hayEquipos() {
        if (gestorEquipos.cantidad() == 0) {
            System.out.println("No hay equipos cargados. Ejecuta primero la opcion 1.");
            return false;
        }
        return true;
    }

    // ----------------- 1. Alta -----------------

    private static void altaEquipos() {
        System.out.println("\n========== 1. ALTA DE EQUIPOS Y JUGADORES ==========");
        try {
            Equipo warriors = new Equipo(1, "Warriors", "Golden State", Conferencia.OESTE);
            warriors.setEntrenador(new Entrenador(101, "Steve", "Kerr", LocalDate.of(1965, 9, 27),
                    "Estados Unidos", 10));
            gestorEquipos.agregar(warriors);
            gestorEquipos.agregar(new Equipo(2, "Lakers", "Los Angeles", Conferencia.OESTE));
            gestorEquipos.agregar(new Equipo(3, "Celtics", "Boston", Conferencia.ESTE));

            gestorEquipos.agregarJugador(1, new Jugador(1, "Stephen", "Curry", LocalDate.of(1988, 3, 14),
                    "Estados Unidos", 30, Posicion.BASE, 1.88));
            gestorEquipos.agregarJugador(1, new Jugador(2, "Draymond", "Green", LocalDate.of(1990, 3, 4),
                    "Estados Unidos", 23, Posicion.ALA_PIVOT, 1.98));
            gestorEquipos.agregarJugador(2, new Jugador(3, "LeBron", "James", LocalDate.of(1984, 12, 30),
                    "Estados Unidos", 23, Posicion.ALERO, 2.06));
            gestorEquipos.agregarJugador(2, new Jugador(4, "Anthony", "Davis", LocalDate.of(1993, 3, 11),
                    "Estados Unidos", 3, Posicion.PIVOT, 2.08));
            gestorEquipos.agregarJugador(3, new Jugador(5, "Jayson", "Tatum", LocalDate.of(1998, 3, 3),
                    "Estados Unidos", 0, Posicion.ALERO, 2.03));
            gestorEquipos.agregarJugador(3, new Jugador(6, "Jaylen", "Brown", LocalDate.of(1996, 10, 24),
                    "Estados Unidos", 7, Posicion.ESCOLTA, 1.98));
            System.out.println("Equipos cargados: " + gestorEquipos.cantidad()
                    + " | Jugadores cargados: " + gestorEquipos.contarJugadores());
        } catch (EntidadDuplicadaException | EntidadNoEncontradaException
                 | PlantelCompletoException | DorsalDuplicadoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Id de equipo repetido
        try {
            gestorEquipos.agregar(new Equipo(1, "Knicks", "New York", Conferencia.ESTE));
        } catch (EntidadDuplicadaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Dato invalido: la excepcion sale del constructor, por eso el jugador se crea dentro del try
        try {
            Jugador invalido = new Jugador(50, "Novato", "Invalido", LocalDate.of(2000, 1, 1),
                    "Argentina", 150, Posicion.ALERO, 1.95);
            gestorEquipos.agregarJugador(1, invalido);
        } catch (IllegalArgumentException | EntidadNoEncontradaException
                 | PlantelCompletoException | DorsalDuplicadoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Regla del negocio: dorsal repetido dentro del mismo equipo
        try {
            gestorEquipos.agregarJugador(1, new Jugador(51, "Klay", "Thompson", LocalDate.of(1990, 2, 8),
                    "Estados Unidos", 30, Posicion.ESCOLTA, 1.98));
        } catch (EntidadNoEncontradaException | PlantelCompletoException | DorsalDuplicadoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Regla del negocio: plantel completo (se usa un equipo de prueba fuera del gestor)
        try {
            Equipo prueba = new Equipo(99, "Prueba", "Ciudad", Conferencia.ESTE);
            for (int dorsal = 1; dorsal <= Equipo.MAX_JUGADORES; dorsal++) {
                prueba.agregarJugador(new Jugador(200 + dorsal, "Jugador", "Numero" + dorsal,
                        LocalDate.of(2000, 1, 1), "Argentina", dorsal, Posicion.ALERO, 1.90));
            }
            System.out.println("Plantel de prueba completo: " + prueba.getCantidadJugadores() + " jugadores");
            prueba.agregarJugador(new Jugador(300, "Jugador", "Extra", LocalDate.of(2000, 1, 1),
                    "Argentina", 99, Posicion.ALERO, 1.90));
        } catch (PlantelCompletoException | DorsalDuplicadoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // ----------------- 2. Listado -----------------

    private static void listadoGeneral() {
        System.out.println("\n========== 2. LISTADO GENERAL ==========");
        mostrarEquipos("Todos los equipos", gestorEquipos.listar());

        System.out.println("--- Equipos de la Conferencia Oeste ---");
        for (Equipo equipo : gestorEquipos.listarPorConferencia(Conferencia.OESTE)) {
            System.out.println(equipo.getNombreCompleto());
        }

        // Polimorfismo: una lista de Persona con jugadores y entrenadores; cada uno responde a su manera
        List<Persona> personas = new ArrayList<>();
        for (Equipo equipo : gestorEquipos.listar()) {
            if (equipo.getEntrenador() != null) {
                personas.add(equipo.getEntrenador());
            }
            personas.addAll(equipo.getPlantel());
        }
        System.out.println("--- Jugadores y entrenadores (" + personas.size() + ") ---");
        for (Persona persona : personas) {
            System.out.println(persona.getDescripcion());
        }
    }

    // ----------------- 3. Partidos -----------------

    private static void partidos() {
        System.out.println("\n========== 3. PARTIDOS ==========");
        try {
            Equipo warriors = gestorEquipos.buscar(1);
            Equipo lakers = gestorEquipos.buscar(2);
            Equipo celtics = gestorEquipos.buscar(3);

            gestorPartidos.agregar(new Partido(1, warriors, lakers, LocalDate.of(2026, 11, 20), Fase.REGULAR));
            gestorPartidos.agregar(new Partido(2, celtics, warriors, LocalDate.of(2026, 11, 22), Fase.REGULAR));
            gestorPartidos.agregar(new Partido(3, lakers, celtics, LocalDate.of(2026, 11, 25), Fase.REGULAR));
            System.out.println("Partidos programados: " + gestorPartidos.cantidad());

            gestorPartidos.registrarResultado(1, 118, 112);
            System.out.println("Resultado cargado: " + gestorPartidos.buscar(1));
            gestorPartidos.registrarResultado(2, 99, 105);
            System.out.println("Resultado cargado: " + gestorPartidos.buscar(2));
        } catch (EntidadNoEncontradaException | EntidadDuplicadaException
                 | PartidoYaJugadoException | EmpateNoPermitidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Empate: el basquet no los permite
        try {
            gestorPartidos.registrarResultado(3, 100, 100);
        } catch (EntidadNoEncontradaException | PartidoYaJugadoException | EmpateNoPermitidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Partido ya jugado
        try {
            gestorPartidos.registrarResultado(1, 100, 90);
        } catch (EntidadNoEncontradaException | PartidoYaJugadoException | EmpateNoPermitidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Partido inexistente
        try {
            gestorPartidos.registrarResultado(99, 100, 90);
        } catch (EntidadNoEncontradaException | PartidoYaJugadoException | EmpateNoPermitidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Un equipo contra si mismo: dato invalido (no chequeada)
        try {
            Equipo warriors = gestorEquipos.buscar(1);
            gestorPartidos.agregar(new Partido(4, warriors, warriors, LocalDate.of(2026, 11, 30), Fase.REGULAR));
        } catch (IllegalArgumentException | EntidadNoEncontradaException | EntidadDuplicadaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Pedir el ganador de un partido que todavia no se jugo: uso incorrecto del objeto (no chequeada)
        try {
            System.out.println("Ganador del partido 3: " + gestorPartidos.buscar(3).getGanador().getNombreCompleto());
        } catch (IllegalStateException | EntidadNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        mostrarPartidos("Partidos finalizados", gestorPartidos.listarFinalizados());
        mostrarPartidos("Partidos pendientes", gestorPartidos.listarPendientes());
        try {
            Equipo warriors = gestorEquipos.buscar(1);
            mostrarPartidos("Partidos de " + warriors.getNombreCompleto(), gestorPartidos.listarPorEquipo(warriors));
        } catch (EntidadNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // ----------------- 4. Persistencia -----------------

    private static void guardarEnBD() {
        System.out.println("\n========== 4. PERSISTENCIA (JDBC) ==========");
        // El try va dentro del for: si falla un equipo, se intenta con los demas
        for (Equipo equipo : gestorEquipos.listar()) {
            try {
                gestorEquipos.guardarEnBD(equipo);
                System.out.println("Guardado en la BD: " + equipo.getNombreCompleto()
                        + " (" + equipo.getCantidadJugadores() + " jugadores)");
            } catch (AccesoDatosException e) {
                mostrarErrorBD(e);
            }
        }

        try {
            mostrarEquipos("Equipos en la base de datos", gestorEquipos.listarDesdeBD());
        } catch (AccesoDatosException e) {
            mostrarErrorBD(e);
        }
    }

    private static void operacionesBD() {
        System.out.println("\n========== 5. CONSULTAS Y MODIFICACIONES (JDBC) ==========");
        // Buscar -> modificar -> actualizar -> eliminar dependen entre si: van en el mismo try
        try {
            Equipo buscado = gestorEquipos.buscarEnBD(1);
            System.out.println("Encontrado: " + buscado.getNombreCompleto()
                    + " (" + buscado.getCantidadJugadores() + " jugadores)");

            buscado.setCiudad("San Francisco");
            gestorEquipos.actualizarEnBD(buscado);
            System.out.println("Actualizado: " + gestorEquipos.buscarEnBD(1).getNombreCompleto());

            gestorEquipos.eliminarJugadorDeBD(2);
            System.out.println("Jugador eliminado de la BD: id 2");
            System.out.println(gestorEquipos.buscarEnBD(1).generarReporte());

            gestorEquipos.eliminarDeBD(3);
            System.out.println("Equipo eliminado de la BD: id 3 (sus jugadores quedan sin equipo)");
            mostrarEquipos("Equipos en la BD luego de eliminar", gestorEquipos.listarDesdeBD());
        } catch (AccesoDatosException e) {
            mostrarErrorBD(e);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Las operaciones finales son independientes: un try cada una
        try {
            gestorEquipos.buscarEnBD(3);
        } catch (AccesoDatosException e) {
            mostrarErrorBD(e);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        try {
            gestorEquipos.eliminarDeBD(99);
        } catch (AccesoDatosException e) {
            mostrarErrorBD(e);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        try {
            gestorEquipos.eliminarJugadorDeBD(99);
        } catch (AccesoDatosException e) {
            mostrarErrorBD(e);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // ----------------- Metodos auxiliares de impresion -----------------

    private static void mostrarEquipos(String titulo, List<Equipo> lista) {
        System.out.println("--- " + titulo + " (" + lista.size() + ") ---");
        if (lista.isEmpty()) {
            System.out.println("(sin elementos)");
            return;
        }
        for (Equipo equipo : lista) {
            System.out.println(equipo.generarReporte());
        }
    }

    private static void mostrarPartidos(String titulo, List<Partido> lista) {
        System.out.println("--- " + titulo + " (" + lista.size() + ") ---");
        if (lista.isEmpty()) {
            System.out.println("(sin elementos)");
            return;
        }
        for (Partido partido : lista) {
            System.out.println(partido.getId() + " - " + partido);
        }
    }

    /** Informa un error de base de datos y, si la tiene, tambien la causa original (la SQLException). */
    private static void mostrarErrorBD(AccesoDatosException e) {
        System.out.println("ERROR BD: " + e.getMessage());
        if (e.getCause() != null) {
            System.out.println("   Causa: " + e.getCause().getMessage());
        }
    }
}
