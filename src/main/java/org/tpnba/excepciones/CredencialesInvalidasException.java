package org.tpnba.excepciones;
/** Se lanza cuando el usuario o la contrasena ingresados en el login son incorrectos. */

public class CredencialesInvalidasException extends TorneoException {
    public CredencialesInvalidasException(String message) {
        super("Usuario o contrasena incorrectos.");
    }
}
