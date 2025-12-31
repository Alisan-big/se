package ir.university.library.exceptions;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String msg) {
        super(msg);
    }
}
