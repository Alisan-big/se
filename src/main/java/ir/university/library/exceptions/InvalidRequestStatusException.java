package ir.university.library.exceptions;

public class InvalidRequestStatusException extends RuntimeException {
    public InvalidRequestStatusException(String msg) {
        super(msg);
    }
}
