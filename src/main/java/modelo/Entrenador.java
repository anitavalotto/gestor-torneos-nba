package modelo;
import java.time.LocalDate;
/** Entrenador principal de un equipo. Agrega los anios de experiencia a los datos de Persona. */


public class Entrenador extends Persona {

    private int aniosExperiencia;

    public Entrenador(int id, String nombre, String apellido, LocalDate fechaNacimiento,
                      String nacionalidad, int aniosExperiencia) {
        super(id, nombre, apellido, fechaNacimiento, nacionalidad);
        setAniosExperiencia(aniosExperiencia);
    }

    @Override
    public String getDescripcion() {
        return "Entrenador: " + getNombreCompleto() + " - " + aniosExperiencia + " anios de experiencia";
    }

    public int getAniosExperiencia() { return aniosExperiencia; }

    public void setAniosExperiencia(int aniosExperiencia) {
        if (aniosExperiencia < 0) {
            throw new IllegalArgumentException("Los anios de experiencia no pueden ser negativos.");
        }
        this.aniosExperiencia = aniosExperiencia;
    }
}
