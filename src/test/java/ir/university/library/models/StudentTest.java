package ir.university.library.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    @Test
    void testCreateStudentDefaultActive() {
        Student s = new Student("ali", "1234");

        assertNotNull(s);
        assertEquals("ali", s.getUsername());
        assertEquals("1234", s.getPassword());
        assertTrue(s.isActive());
    }

    @Test
    void testActivateDeactivateStudent() {
        Student s = new Student("reza", "9999");

        s.setActive(false);
        assertFalse(s.isActive());

        s.setActive(true);
        assertTrue(s.isActive());
    }

    @Test
    void testToStringHasStatus() {
        Student s = new Student("mina", "pass");
        String text = s.toString();
        assertTrue(text.contains("mina"));
        assertTrue(text.contains("فعال") || text.toLowerCase().contains("active"));
    }
}
