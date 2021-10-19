package ar.edu.unq.solotravel.backend.api.exceptions;

public class ExpiredSessionException extends RuntimeException {
    public ExpiredSessionException() {
        super("Session expired");
    }
}
