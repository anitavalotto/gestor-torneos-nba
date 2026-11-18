package gestor;

import excepciones.EmpateNoPermitidoException;
import excepciones.EntidadNoEncontradaException;
import excepciones.PartidoYaJugadoException;
import modelo.Equipo;
import modelo.Partido;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de partidos: hereda de Gestor&lt;Partido&gt; la administracion en memoria
 * y agrega las operaciones propias de los partidos. Los partidos no se guardan
 * en la base de datos (el enunciado pide JDBC en las 2 o 3 clases principales).
 */
public class GestorPartidos extends Gestor<Partido> {

    public GestorPartidos() {
        super("partido");
    }

    /**
     * Carga el resultado de un partido ya programado.
     *
     * @throws EntidadNoEncontradaException si no existe el partido
     * @throws PartidoYaJugadoException     si el partido ya tenia resultado
     * @throws EmpateNoPermitidoException   si los puntajes son iguales
     */
    public void registrarResultado(int partidoId, int puntosLocal, int puntosVisitante)
            throws EntidadNoEncontradaException, PartidoYaJugadoException, EmpateNoPermitidoException {
        Partido partido = buscar(partidoId);
        partido.registrarResultado(puntosLocal, puntosVisitante);
    }

    /** @return los partidos que todavia no tienen resultado */
    public List<Partido> listarPendientes() {
        List<Partido> resultado = new ArrayList<>();
        for (Partido partido : elementos) {
            if (!partido.estaFinalizado()) {
                resultado.add(partido);
            }
        }
        return resultado;
    }

    /** @return los partidos que ya tienen resultado */
    public List<Partido> listarFinalizados() {
        List<Partido> resultado = new ArrayList<>();
        for (Partido partido : elementos) {
            if (partido.estaFinalizado()) {
                resultado.add(partido);
            }
        }
        return resultado;
    }

    /** @return los partidos en los que juega el equipo, como local o visitante */
    public List<Partido> listarPorEquipo(Equipo equipo) {
        List<Partido> resultado = new ArrayList<>();
        for (Partido partido : elementos) {
            if (partido.participa(equipo)) {
                resultado.add(partido);
            }
        }
        return resultado;
    }
}
