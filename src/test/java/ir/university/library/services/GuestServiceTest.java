package ir.university.library.services;

import ir.university.library.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuestServiceTest {

    private LibraryService library;
    private GuestService guestService;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
        guestService = new GuestService(library);
    }

    @Test
    void testSeeNumberOfStudents() {
        library.students.clear();
        library.students.add(new Student("s1", "p1"));
        library.students.add(new Student("s2", "p2"));

        // option 1, then 0 back
        String input =
                "1\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        guestService.menu(sc);

        assertEquals(2, library.students.size());
    }

    @Test
    void testSearchBookTitle() {
        library.books.clear();
        library.books.add(new Book("Clean Code", "Robert Martin", 2008, 1));
        library.books.add(new Book("Effective Java", "Joshua Bloch", 2017, 1));

        // option 2 → search for "Clean" → then 0 back
        String input =
                "2\n" +
                        "Clean\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        guestService.menu(sc);

        // main thing: no exceptions, data is present
        assertEquals(2, library.books.size());
    }

    @Test
    void testShowStats() {
        library.students.clear();
        library.books.clear();
        library.requests.clear();

        library.students.add(new Student("s1", "p1"));
        library.students.add(new Student("s2", "p2"));

        library.books.add(new Book("Book1", "A", 2000, 1));
        library.books.add(new Book("Book2", "B", 2001, 1));

        BorrowRequest r1 = new BorrowRequest(1, 1, LocalDate.now(), LocalDate.now().plusDays(3));
        BorrowRequest r2 = new BorrowRequest(2, 2, LocalDate.now(), LocalDate.now().plusDays(5));
        r2.setStatus("Approved");
        library.requests.add(r1);
        library.requests.add(r2);

        // option 3 (show stats), then 0
        String input =
                "3\n" +
                        "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        guestService.menu(sc);

        // sanity check: state unchanged, but shows stats without crashing
        assertEquals(2, library.students.size());
        assertEquals(2, library.books.size());
        assertEquals(2, library.requests.size());
    }
}
