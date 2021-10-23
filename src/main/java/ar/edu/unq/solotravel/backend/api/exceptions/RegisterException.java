package ar.edu.unq.solotravel.backend.api.exceptions;

public class RegisterException extends RuntimeException {
    public RegisterException(String mssg) {
        super(mssg);
    }
}
