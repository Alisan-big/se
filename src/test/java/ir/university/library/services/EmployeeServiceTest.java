package ir.university.library.services;

import ir.university.library.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private LibraryService library;
    private EmployeeService employeeService;
    private Employee emp; // the one we log in with

    @BeforeEach
    void setUp() {
        library = new LibraryService();
        // LibraryService constructor already adds:
        // books: ["software engineering", "system design"]
        // employees: [("emp1","123")]

        // we’ll log in with that default employee
        emp = library.employees.get(0);
        employeeService = new EmployeeService(library);
    }

    @Test
    void testInvalidLogin() {
        // username: wrong, password: 000 → menu should stop
        String input = "wrong\n000\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        employeeService.menu(sc);

        // nothing should change, just check we still have the default employee
        assertEquals(1, library.employees.size());
        assertEquals("emp1", library.employees.get(0).getUsername());
        assertEquals("123", library.employees.get(0).getPassword());
    }

    @Test
    void testChangePassword() {
        // login (emp1 / 123), then:
        // choice 1 (change password) → newPass
        // choice 0 (exit)
        String input =
                "emp1\n" +
                        "123\n" +
                        "1\n" +
                        "newPass\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        employeeService.menu(sc);

        assertEquals("newPass", emp.getPassword());
    }

    @Test
    void testAddBook() {
        int initialSize = library.books.size();

        // login → choice 2 (add book)
        // title, author, year, then 0 to exit
        String input =
                "emp1\n" +
                        "123\n" +
                        "2\n" +
                        "Clean Code\n" +
                        "Robert Martin\n" +
                        "2008\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        employeeService.menu(sc);

        assertEquals(initialSize + 1, library.books.size());
        Book added = library.books.get(library.books.size() - 1);
        assertEquals("Clean Code", added.getTitle());
        assertEquals("Robert Martin", added.getAuthor());
        assertEquals(2008, added.getYear());
        assertEquals(emp.getId(), added.getRegisteredByEmployeeId());
    }

    @Test
    void testApproveRequests() {
        // prepare: one pending request for today, and a corresponding book in library
        Book b = library.books.get(0);
        b.setAvailable(true);

        BorrowRequest req = new BorrowRequest(
                1,                     // studentId (doesn't matter here)
                b.getId(),             // bookId
                LocalDate.now(),       // start today
                LocalDate.now().plusDays(7)
        );
        library.requests.add(req);

        // login → option 4 (approve requests) → answer 'y' to approve → 0 exit
        String input =
                "emp1\n" +
                        "123\n" +
                        "4\n" +
                        "y\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        employeeService.menu(sc);

        assertEquals("Approved", req.getStatus());
        assertFalse(b.isAvailable());
    }

    @Test
    void testToggleStudent() {
        // add one student
        Student s = new Student("stu1", "pwd");
        library.students.add(s);
        assertTrue(s.isActive());

        // login → option 6 (toggle student) → username=stu1 → 0 exit
        String input =
                "emp1\n" +
                        "123\n" +
                        "6\n" +
                        "stu1\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        employeeService.menu(sc);

        assertFalse(s.isActive());

        // toggle back to active:
        String input2 =
                "emp1\n" +
                        "123\n" +
                        "6\n" +
                        "stu1\n" +
                        "0\n";
        Scanner sc2 = new Scanner(new ByteArrayInputStream(input2.getBytes()));

        employeeService.menu(sc2);

        assertTrue(s.isActive());
    }

    @Test
    void testHandleBorrow_registerBorrow() {
        // approved request must exist
        Book b = library.books.get(0);
        b.setAvailable(false); // after approval it should be false

        BorrowRequest req = new BorrowRequest(
                10,                    // studentId
                b.getId(),
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );
        req.setStatus("Approved");
        library.requests.add(req);

        int requestId = req.getId();

        // login → option 7 → choose 1 (register borrow) → enter approved request id → 0 exit
        String input =
                "emp1\n" +
                        "123\n" +
                        "7\n" +
                        "1\n" +               // 1. register a borrow
                        requestId + "\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        int initialRecords = library.records.size();

        employeeService.menu(sc);

        assertEquals(initialRecords + 1, library.records.size());
        BorrowRecord rec = library.records.get(library.records.size() - 1);
        assertEquals(req.getStudentId(), rec.getStudentId());
        assertEquals(req.getBookId(), rec.getBookId());
        assertEquals(req.getStartDate(), rec.getStartDate());
        assertEquals(req.getEndDate(), rec.getEndDate());
        assertNotNull(rec.getReceivedDate());
        assertFalse(rec.isReturned());
    }

    @Test
    void testHandleBorrow_registerReturn() {
        // create a borrow record that is not returned
        Book b = library.books.get(0);
        b.setAvailable(false);

        BorrowRecord rec = new BorrowRecord(
                10,
                b.getId(),
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(1)
        );
        library.records.add(rec);

        int recId = rec.getId();

        // login → option 7 → choose 2 (register return) → enter borrow id → 0 exit
        String input =
                "emp1\n" +
                        "123\n" +
                        "7\n" +
                        "2\n" +            // 2. register a return
                        recId + "\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        employeeService.menu(sc);

        assertTrue(rec.isReturned());
        assertNotNull(rec.getReturnedDate());
        assertTrue(b.isAvailable());
    }
}
