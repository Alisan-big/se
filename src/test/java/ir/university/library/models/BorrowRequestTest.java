package ir.university.library.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BorrowRequestTest {

    @Test
    void testCreateBorrowRequestDefaults() {
        LocalDate start = LocalDate.of(2024, 11, 1);
        LocalDate end   = LocalDate.of(2024, 11, 10);

        BorrowRequest r = new BorrowRequest(1, 2, start, end);

        assertEquals(1, r.getStudentId());
        assertEquals(2, r.getBookId());
        assertEquals(start, r.getStartDate());
        assertEquals(end, r.getEndDate());
        assertEquals("Pending", r.getStatus());
        assertTrue(r.toString().contains("Pending"));
    }

    @Test
    void testChangeStatus() {
        BorrowRequest r = new BorrowRequest(1, 2, LocalDate.now(), LocalDate.now().plusDays(7));

        r.setStatus("Approved");
        assertEquals("Approved", r.getStatus());

        r.setStatus("Rejected");
        assertEquals("Rejected", r.getStatus());
    }
}
