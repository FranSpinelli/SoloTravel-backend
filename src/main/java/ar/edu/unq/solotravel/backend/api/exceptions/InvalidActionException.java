package ar.edu.unq.solotravel.backend.api.exceptions;

public class InvalidActionException extends RuntimeException{

    public InvalidActionException(String mssg) {
        super(mssg);
    }
}
