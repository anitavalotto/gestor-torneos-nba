package excepciones;
/** Se lanza al cargar un resultado empatado: en el basquet siempre hay un ganador. */
public class EmpateNoPermitidoException extends TorneoException {
    public EmpateNoPermitidoException(int puntos) {
        super("Resultado invalido " +puntos+ "-" +puntos+ ": en el basquet no hay empates." );
    }
}
