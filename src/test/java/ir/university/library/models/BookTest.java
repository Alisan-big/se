package ir.university.library.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void testCreateBookDefaults() {
        Book b = new Book("Software Engineering", "Pressman", 2015, 1);

        assertNotNull(b);
        assertEquals("Software Engineering", b.getTitle());
        assertEquals("Pressman", b.getAuthor());
        assertEquals(2015, b.getYear());
        assertTrue(b.isAvailable());
        assertEquals(1, b.getRegisteredByEmployeeId());
    }

    @Test
    void testChangeAvailability() {
        Book b = new Book("SE", "X", 2010, 1);
        assertTrue(b.isAvailable());

        b.setAvailable(false);
        assertFalse(b.isAvailable());

        b.setAvailable(true);
        assertTrue(b.isAvailable());
    }

    @Test
    void testToStringContainsTitle() {
        Book b = new Book("Clean Code", "Robert C. Martin", 2008, 1);
        String s = b.toString();
        assertTrue(s.contains("Clean Code"));
        assertTrue(s.contains("2008"));
    }
}
