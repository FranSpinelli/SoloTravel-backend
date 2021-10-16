package ar.edu.unq.solotravel.backend.api.exceptions;

public class LogInException extends RuntimeException {
    public LogInException(String mssg) {
        super(mssg);
    }
}
