package excepciones;

//se intenta agregar un elemento con id ya existente en repo//

public class EntidadDuplicadaException extends TorneoException {
    public EntidadDuplicadaException(String tipo, int id) {

      super("Ya existe un " + tipo + " con id: " +id);
    }
}
