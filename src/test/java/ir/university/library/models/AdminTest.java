package ir.university.library.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    @Test
    void testCorrectLogin() {
        Admin admin = new Admin();
        assertTrue(admin.login("admin", "admin"));
    }

    @Test
    void testWrongLogin() {
        Admin admin = new Admin();
        assertFalse(admin.login("admin", "wrong"));
        assertFalse(admin.login("wrong", "admin"));
        assertFalse(admin.login("x", "y"));
    }
}
