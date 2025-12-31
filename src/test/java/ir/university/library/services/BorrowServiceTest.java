package ir.university.library.services;

import ir.university.library.models.*;
import ir.university.library.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {

    private LibraryService library;
    private BorrowService borrowService;
    private Student student;
    private Book book;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
        library.students.clear();
        library.books.clear();
        library.requests.clear();

        student = new Student("ali", "123");
        student.setActive(true);
        library.students.add(student);

        book = new Book("Clean Code", "Robert Martin", 2008, 1);
        library.books.add(book);

        borrowService = new BorrowService(library);
    }

    @Test
    void testCreateRequestActiveStudent() throws Exception { // سناریو 3-1
        BorrowRequest req = borrowService.createRequest(student.getId(), book.getId(),
                LocalDate.now(), LocalDate.now().plusDays(7));
        assertEquals("Pending", req.getStatus());
    }

    @Test
    void testCreateRequestInactiveStudent() { // سناریو 3-2
        student.setActive(false);
        assertThrows(InvalidStudentStatusException.class, () ->
                borrowService.createRequest(student.getId(), book.getId(),
                        LocalDate.now(), LocalDate.now().plusDays(7)));
    }

    @Test
    void testCreateRequestBorrowedBook() { // سناریو 3-3
        book.setAvailable(false);
        assertThrows(BookNotAvailableException.class, () ->
                borrowService.createRequest(student.getId(), book.getId(),
                        LocalDate.now(), LocalDate.now().plusDays(7)));
    }

    @Test
    void testApproveRequest() throws Exception { // سناریو 3-4
        BorrowRequest req = borrowService.createRequest(student.getId(), book.getId(),
                LocalDate.now(), LocalDate.now().plusDays(7));
        borrowService.approveRequest(req);
        assertEquals("Approved", req.getStatus());
        assertFalse(book.isAvailable());
    }

    @Test
    void testApproveRequestAgain() throws Exception { // سناریو 3-5
        BorrowRequest req = borrowService.createRequest(student.getId(), book.getId(),
                LocalDate.now(), LocalDate.now().plusDays(7));
        borrowService.approveRequest(req);
        assertThrows(InvalidRequestStatusException.class, () ->
                borrowService.approveRequestAgain(req));
    }
}
