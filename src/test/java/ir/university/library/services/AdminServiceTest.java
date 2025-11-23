package ir.university.library.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import ir.university.library.models.Employee;

class AdminServiceTest {

    private LibraryService library;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
        library.employees.clear(); // ensure clean state
        adminService = new AdminService(library);
    }

    @Test
    void testAddEmployee() {
        // input: admin login + add employee + exit
        String input = "admin\nadmin\n1\njohn\npass123\n0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        adminService.menu(sc);

        assertEquals(1, library.employees.size());
        Employee emp = library.employees.get(0);
        assertEquals("john", emp.getUsername());
        assertEquals("pass123", emp.getPassword());
    }

    @Test
    void testInvalidLogin() {
        String input = "wrong\nwrong\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        adminService.menu(sc);

        assertEquals(0, library.employees.size());
    }

    @Test
    void testShowEmployeeStats() {
        library.employees.add(new Employee("emp1", "123"));
        library.books.add(new ir.university.library.models.Book("Test Book", "Author", 2020, library.employees.get(0).getId()));

        String input = "admin\nadmin\n2\n0\n"; // view stats, then exit
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        adminService.menu(sc);
        // nothing to assert here, just ensure no exceptions
    }

    @Test
    void testShowBorrowStats() {
        library.requests.clear();
        String input = "admin\nadmin\n3\n0\n"; // borrow stats then exit
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        adminService.menu(sc);
        // test passes if no exceptions
    }

    @Test
    void testShowStudentStats() {
        library.students.clear();
        String input = "admin\nadmin\n4\n0\n"; // student stats then exit
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        adminService.menu(sc);
        // test passes if no exceptions
    }
}
