package models;

import java.time.LocalDate;

public class BorrowRecord {
    private static int counter = 1;
    private int id;
    private int studentId;
    private int bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate receivedDate;   // زمانی که دانشجو کتاب را تحویل می‌گیرد
    private LocalDate returnedDate;   // زمانی که کتاب را پس می‌دهد

    public BorrowRecord(int studentId, int bookId, LocalDate startDate, LocalDate endDate) {
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
    public LocalDate getReceivedDate() { return receivedDate; }
    public LocalDate getReturnedDate() { return returnedDate; }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public boolean isReturned() {
        return returnedDate != null;
    }

    public boolean isDelayed() {
        if (returnedDate == null) return false;
        return returnedDate.isAfter(endDate);
    }

    @Override
    public String toString() {
        return String.format(
                "[%d] کتاب %d برای دانشجو %d از %s تا %s | تحویل: %s | برگشت: %s%s",
                id, bookId, studentId, startDate, endDate,
                receivedDate != null ? receivedDate : "-",
                returnedDate != null ? returnedDate : "-",
                (isDelayed() ? " (با تأخیر)" : "")
        );
    }
}
