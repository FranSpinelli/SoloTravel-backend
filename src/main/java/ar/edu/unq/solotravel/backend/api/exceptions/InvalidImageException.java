package ar.edu.unq.solotravel.backend.api.exceptions;

public class InvalidImageException extends RuntimeException {
    public InvalidImageException() {
        super("The given image is not valid");
    }
}
