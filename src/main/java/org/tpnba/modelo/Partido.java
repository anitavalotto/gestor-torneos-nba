package org.tpnba.modelo;

import org.tpnba.enums.EstadoPartido;
import org.tpnba.enums.Fase;
import org.tpnba.excepciones.EmpateNoPermitidoException;
import org.tpnba.excepciones.PartidoYaJugadoException;
import org.tpnba.interfaces.Identificable;

import java.time.LocalDate;
import java.util.Objects;

public class Partido implements Identificable {

    //Partido entre 2 EQUIPOS, empieza como PROGRAMADO sin resultado y pasa a FINALIZADO cuando se registra el resul//
    private int id;
    private final Equipo local;
    private final Equipo visitante;
    private LocalDate fecha;
    private final Fase fase;
    private int puntosLocal;
    private int puntosVisitante;
    private EstadoPartido estado;

    public Partido (int id, Equipo local, Equipo visitante, LocalDate fecha, Fase fase){
        if (local == null || visitante == null){
            throw new IllegalArgumentException("El partido necesita obligatoriamente 2 equipos");
        }
        if (local.equals(visitante)){
            throw new IllegalArgumentException("Un equipo no puede jugar contra si mismo");
        }
        if (fase == null){
            throw new IllegalArgumentException("La fase es obligatoria");
        }
        this.id = id;
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        this.fase = fase;
        this.estado = EstadoPartido.PROGRAMADO;
    }

    // registra resultado final del partido, es decir, cambia a Estado Finalizado

    public void registrarResultado (int puntosLocal, int puntosVisitante) throws PartidoYaJugadoException, EmpateNoPermitidoException {
        if (estaFinalizado ()){
            throw new PartidoYaJugadoException(id);
        }
        if (puntosLocal < 0 || puntosVisitante < 0){
            throw new IllegalArgumentException("Los puntos no pueden ser negativos");
        }
        if (puntosLocal == puntosVisitante){
            throw new EmpateNoPermitidoException(puntosLocal);
        }
        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;
        this.estado = EstadoPartido.FINALIZADO;
    }

    //retorna equipo ganador

    public Equipo getGanador (){
        verificarFinalizado();
        return puntosLocal > puntosVisitante ? local : visitante;
    }

    //retorna equipo perdedor
    public Equipo getPerdedor (){
        verificarFinalizado();
        return puntosLocal > puntosVisitante ? visitante : local;
    }

    // retorna si el equipo enviado participa tanto como local o visitante
    public boolean participa (Equipo equipo) {
        return local.equals(equipo) || visitante.equals(equipo);
    }


    //true si el partido ya tiene resultado cargado
    public boolean estaFinalizado (){
        return estado == EstadoPartido.FINALIZADO;
    }

    // metodo aux privado para no repetir validacion//
    private void verificarFinalizado (){
        if (!estaFinalizado()){
            throw new IllegalStateException("El partido " +id+ " todavia no tiene resultado");
        }
    }

    // Getters y Setters

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Fase getFase() {
        return fase;
    }

    public int getPuntosLocal() {
        return puntosLocal;
    }

    public int getPuntosVisitante() {
        return puntosVisitante;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    // equals, hashcode y toString


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partido partido)) return false;
        return id == partido.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String fechaTexto = (fecha != null) ? fecha + " | " : "";
        if (estaFinalizado()) {
            return fechaTexto + local.getNombre() + " " + puntosLocal + " - " + puntosVisitante + " " + visitante.getNombre();
        }
        return fechaTexto + local.getNombre() + " vs " + visitante.getNombre() + " (programado)";
    }
}
