package ir.university.library.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    @Test
    void testCreateEmployee() {
        Employee e = new Employee("emp1", "123");

        assertNotNull(e);
        assertEquals("emp1", e.getUsername());
        assertEquals("123", e.getPassword());
        assertTrue(e.getId() > 0);
    }

    @Test
    void testChangePassword() {
        Employee e = new Employee("emp2", "old");
        e.setPassword("newpass");
        assertEquals("newpass", e.getPassword());
    }

    @Test
    void testToStringContainsUsername() {
        Employee e = new Employee("empX", "pw");
        String text = e.toString();
        assertTrue(text.contains("empX"));
    }
}
