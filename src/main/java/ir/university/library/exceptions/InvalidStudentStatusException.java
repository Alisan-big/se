package ir.university.library.exceptions;

public class InvalidStudentStatusException extends RuntimeException {
    public InvalidStudentStatusException(String msg) {
        super(msg);
    }
}
