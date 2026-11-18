package enums;

public enum Posicion {
    BASE("Base", "PG"),
    ESCOLTA("Escolta", "SG"),
    ALERO("Alero", "SF"),
    ALA_PIVOT("Ala-pivot", "PF"),
    PIVOT("Pivot", "C");

    private final String nombre;
    private final String abreviatura;

    // El constructor de un enum siempre es privado
    Posicion(String nombre, String abreviatura) {
        this.nombre = nombre;
        this.abreviatura = abreviatura;
    }

    public String getNombre() { return nombre; }

    public String getAbreviatura() { return abreviatura; }

    @Override
    public String toString() { return nombre + " (" + abreviatura + ")"; }
}
