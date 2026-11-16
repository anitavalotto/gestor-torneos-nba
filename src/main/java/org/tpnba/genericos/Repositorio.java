package org.tpnba.genericos;

import org.tpnba.excepciones.EntidadDuplicadaException;
import org.tpnba.excepciones.EntidadNoEncontradaException;
import org.tpnba.interfaces.Identificable;

import java.util.ArrayList;
import java.util.List;

public class Repositorio <T extends Identificable> {

    private final List<T>  elementos;
    private final String nombreEntidad;  // para msj de error

    public Repositorio (String nombreEntidad) throws IllegalArgumentException{
        if (nombreEntidad == null || nombreEntidad.isBlank()){
            throw new IllegalArgumentException("El nombre de la entidad no puede estar vacio");
        }
        this.nombreEntidad = nombreEntidad.trim();
        this.elementos = new ArrayList<>();
    }

    //agregar elemento

    public void agregar (T elemento) throws EntidadDuplicadaException{
        if (elemento == null){
            throw new IllegalArgumentException("El elemento no puede ser null");
        }
        if (encontrar(elemento.getId()) != null){
            throw new EntidadDuplicadaException(nombreEntidad, elemento.getId());
        }
        elementos.add(elemento);
    }

    //buscar elemento por id

    public T buscarPorId (int id) throws EntidadNoEncontradaException{
        T elemento = encontrar (id);
        if (elemento == null){
            throw new EntidadNoEncontradaException(nombreEntidad, id);
        }
        return elemento;
    }

    // elimina elemento segun id

    public void eliminar (int id) throws EntidadNoEncontradaException{
        elementos.remove(buscarPorId(id)); // buscaPorId lanza la exception si fuese necesario
    }

    // devuelve copia de la lista de elementos

    public List <T> listar (){
        return new ArrayList<>(elementos);
    }

    // devuelve cant de elementos guardados en la lista

    public int cantidad (){
        return elementos.size();
    }

    // metodo aux privado, devuelve elemento por id o null si no esta
    private T encontrar (int id){
        for (T elemento : elementos){
            if (elemento.getId() == id){
                return elemento;
            }
        }
        return null;
    }
}
