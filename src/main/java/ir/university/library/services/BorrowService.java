package ir.university.library.services;

import ir.university.library.models.*;
import ir.university.library.exceptions.*;

import java.time.LocalDate;

public class BorrowService {

    private LibraryService library;

    public BorrowService(LibraryService library) {
        this.library = library;
    }

    // سناریو 3-1: ثبت درخواست امانت
    public BorrowRequest createRequest(int studentId, int bookId, LocalDate start, LocalDate end)
            throws InvalidStudentStatusException, BookNotAvailableException {

        Student s = library.findStudentById(studentId);
        if (s == null || !s.isActive()) {
            throw new InvalidStudentStatusException("Student is not active");
        }

        Book b = library.findBookById(bookId);
        if (b == null || !b.isAvailable()) {
            throw new BookNotAvailableException("Book is not available");
        }

        BorrowRequest req = new BorrowRequest(studentId, bookId, start, end);
        library.requests.add(req);
        return req;
    }

    // سناریو 3-4: تایید درخواست
    public void approveRequest(BorrowRequest req) throws InvalidRequestStatusException {
        if (!req.getStatus().equals("Pending")) {
            throw new InvalidRequestStatusException("Request is not pending");
        }
        req.setStatus("Approved");
        Book b = library.findBookById(req.getBookId());
        if (b != null) b.setAvailable(false);
    }

    // سناریو 3-5: تلاش برای تایید دوباره همان درخواست
    public void approveRequestAgain(BorrowRequest req) throws InvalidRequestStatusException {
        approveRequest(req); // همین متد خطا میده اگر قبلاً تایید شده باشد
    }
}
