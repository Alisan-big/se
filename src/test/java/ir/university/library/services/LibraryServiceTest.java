package ir.university.library.services;

import ir.university.library.models.Book;
import ir.university.library.models.Employee;
import ir.university.library.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {

    private LibraryService library;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
    }

    @Test
    void testConstructorInitialData() {
        // from your constructor:
        // books.add(new Book("software engineering", "Pressman", 2015, 1));
        // books.add(new Book("system design", "Dennis", 2020, 1));
        // employees.add(new Employee("emp1", "123"));

        assertEquals(2, library.books.size(), "Library should start with 2 books");
        assertEquals(1, library.employees.size(), "Library should start with 1 employee");

        Book b1 = library.books.get(0);
        Book b2 = library.books.get(1);

        assertEquals("software engineering", b1.getTitle());
        assertEquals("Pressman", b1.getAuthor());
        assertEquals(2015, b1.getYear());

        assertEquals("system design", b2.getTitle());
        assertEquals("Dennis", b2.getAuthor());
        assertEquals(2020, b2.getYear());

        Employee e = library.employees.get(0);
        assertEquals("emp1", e.getUsername());
        assertEquals("123", e.getPassword());
    }

    @Test
    void testFindBookById_found() {
        Book any = library.books.get(0);
        int id = any.getId();

        Book result = library.findBookById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindBookById_notFound() {
        Book result = library.findBookById(999999); // id that doesn't exist
        assertNull(result);
    }

    @Test
    void testFindStudentByUsername_found() {
        Student s = new Student("ali", "123");
        library.students.add(s);

        Student res = library.findStudentByUsername("ali");

        assertNotNull(res);
        assertEquals("ali", res.getUsername());
    }

    @Test
    void testFindStudentByUsername_notFound() {
        Student res = library.findStudentByUsername("ghost");
        assertNull(res);
    }

    @Test
    void testFindEmployeeByUsername_found() {
        // constructor already added emp1
        Employee e = library.findEmployeeByUsername("emp1");

        assertNotNull(e);
        assertEquals("emp1", e.getUsername());
    }

    @Test
    void testFindEmployeeByUsername_notFound() {
        Employee e = library.findEmployeeByUsername("someone");
        assertNull(e);
    }
}
