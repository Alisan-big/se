package ir.university.library.models;

import java.time.LocalDate;

public class BorrowRequest {
    private static int counter = 1;
    private int id;
    private int studentId;
    private int bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status = "Pending"; // Pending, Approved, Rejected

    public BorrowRequest(int studentId, int bookId, LocalDate startDate, LocalDate endDate) {
        this.id = counter++;
        this.studentId = studentId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public int getBookId() { return bookId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%d] book %d for student %d from %s until %s | status: %s",
                id, bookId, studentId, startDate, endDate, status);
    }
}
