package ir.university.library.services;

import ir.university.library.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private LibraryService library;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
        library.students.clear();  // start clean
        library.requests.clear();
        studentService = new StudentService(library);
    }

    @Test
    void testSignUpAndMenuExit() {
        // 1 (sign up)
        // username: ali
        // password: 123
        // then in student menu: 0 (exit)
        String input =
                "1\n" +
                        "ali\n" +
                        "123\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        studentService.menu(sc);

        assertEquals(1, library.students.size());
        Student s = library.students.get(0);
        assertEquals("ali", s.getUsername());
        assertEquals("123", s.getPassword());
        assertTrue(s.isActive());
    }

    @Test
    void testSignUpDuplicateUsername() {
        library.students.add(new Student("ali", "111"));

        String input =
                "1\n" +
                        "ali\n" +    // duplicate username
                        "123\n";     // won’t be used
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        studentService.menu(sc);

        // still only the original student
        assertEquals(1, library.students.size());
    }

    @Test
    void testLoginSuccess() {
        Student s = new Student("ali", "123");
        library.students.add(s);

        // 2 (login), username=ali, pass=123, then 0 exit
        String input =
                "2\n" +
                        "ali\n" +
                        "123\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        studentService.menu(sc);

        // nothing to assert except that it didn’t reject
        assertEquals(1, library.students.size());
    }

    @Test
    void testLoginInvalidCredentials() {
        Student s = new Student("ali", "123");
        library.students.add(s);

        String input =
                "2\n" +
                        "ali\n" +
                        "wrong\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        studentService.menu(sc);

        // still just one student, and no requests
        assertEquals(1, library.students.size());
        assertTrue(library.requests.isEmpty());
    }

    @Test
    void testLoginInactiveStudent() {
        Student s = new Student("ali", "123");
        s.setActive(false);
        library.students.add(s);

        String input =
                "2\n" +
                        "ali\n" +
                        "123\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        studentService.menu(sc);

        // inactive student should not enter menu, so no requests
        assertTrue(library.requests.isEmpty());
    }

    @Test
    void testSearchBooks() {
        // add a book that we can search
        library.books.clear();
        library.books.add(new Book("Data Structures", "Mark", 2010, 1));
        library.books.add(new Book("Algorithms", "CLRS", 2009, 1));

        Student s = new Student("ali", "123");
        library.students.add(s);

        // login path (2), then search (1), then 0 exit
        String input =
                "2\n" +
                        "ali\n" +
                        "123\n" +
                        "1\n" +
                        "Data\n" +  // search term
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        studentService.menu(sc);

        // only checking no exceptions; more detailed verification would require capturing System.out
        assertEquals(2, library.books.size());
    }

    @Test
    void testRequestBorrow() {
        // prepare: one student, one available book
        Student s = new Student("ali", "123");
        library.students.add(s);

        library.books.clear();
        Book b = new Book("Networks", "Tanenbaum", 2011, 1);
        library.books.add(b);
        b.setAvailable(true);

        // 2 (login)→ ali / 123
        // 2 (add borrow request)
        // book id
        // start date
        // end date
        // then 0 exit
        String input =
                "2\n" +
                        "ali\n" +
                        "123\n" +
                        "2\n" +
                        b.getId() + "\n" +
                        LocalDate.now().toString() + "\n" +
                        LocalDate.now().plusDays(7).toString() + "\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        studentService.menu(sc);

        assertEquals(1, library.requests.size());
        BorrowRequest req = library.requests.get(0);
        assertEquals(s.getId(), req.getStudentId());
        assertEquals(b.getId(), req.getBookId());
        assertEquals("Pending", req.getStatus());
    }
}
