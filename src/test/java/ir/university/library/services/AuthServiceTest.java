package ir.university.library.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private LibraryService library;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
        library.students.clear(); // حالت تمیز برای هر تست
        authService = new AuthService(library);
    }

    @Test
    void testRegisterUniqueUser() { // سناریو 1-1
        boolean result = authService.register("ali", "123");
        assertTrue(result);
        assertEquals(1, library.students.size());
    }

    @Test
    void testRegisterDuplicateUser() { // سناریو 1-2
        authService.register("ali", "123");
        boolean result = authService.register("ali", "456");
        assertFalse(result);
        assertEquals(1, library.students.size());
    }

    @Test
    void testLoginCorrectCredentials() { // سناریو 1-3
        authService.register("ali", "123");
        boolean result = authService.login("ali", "123");
        assertTrue(result);
    }

    @Test
    void testLoginWrongPassword() { // سناریو 1-4
        authService.register("ali", "123");
        boolean result = authService.login("ali", "wrong");
        assertFalse(result);
    }

    @Test
    void testLoginNonExistentUser() { // سناریو 1-5
        boolean result = authService.login("ghost", "123");
        assertFalse(result);
    }
}
