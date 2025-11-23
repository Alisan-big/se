package ir.university.library.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BorrowRecordTest {

    @Test
    void testCreateBorrowRecord() {
        LocalDate start = LocalDate.of(2024, 11, 1);
        LocalDate end   = LocalDate.of(2024, 11, 5);

        BorrowRecord rec = new BorrowRecord(1, 10, start, end);

        assertEquals(1, rec.getStudentId());
        assertEquals(10, rec.getBookId());
        assertEquals(start, rec.getStartDate());
        assertEquals(end, rec.getEndDate());
        assertNull(rec.getReceivedDate());
        assertNull(rec.getReturnedDate());
        assertFalse(rec.isReturned());
        assertFalse(rec.isDelayed());
    }

    @Test
    void testReturnOnTime() {
        LocalDate start = LocalDate.of(2024, 11, 1);
        LocalDate end   = LocalDate.of(2024, 11, 5);

        BorrowRecord rec = new BorrowRecord(1, 10, start, end);
        rec.setReceivedDate(LocalDate.of(2024, 11, 1));
        rec.setReturnedDate(LocalDate.of(2024, 11, 5));

        assertTrue(rec.isReturned());
        assertFalse(rec.isDelayed());
    }

    @Test
    void testReturnWithDelay() {
        LocalDate start = LocalDate.of(2024, 11, 1);
        LocalDate end   = LocalDate.of(2024, 11, 5);

        BorrowRecord rec = new BorrowRecord(1, 10, start, end);
        rec.setReceivedDate(LocalDate.of(2024, 11, 2));
        rec.setReturnedDate(LocalDate.of(2024, 11, 7)); // after end date

        assertTrue(rec.isReturned());
        assertTrue(rec.isDelayed());
    }
}
