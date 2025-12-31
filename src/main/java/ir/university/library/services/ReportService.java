package ir.university.library.services;

import ir.university.library.models.*;
import ir.university.library.reports.*;

import java.time.temporal.ChronoUnit;

public class ReportService {

    private LibraryService library;

    public ReportService(LibraryService library) {
        this.library = library;
    }


    public StudentReport generateStudentReport(int studentId) {
        StudentReport r = new StudentReport();

        for (BorrowRecord br : library.records) {
            if (br.getStudentId() == studentId) {
                r.totalBorrows++;

                if (!br.isReturned())
                    r.notReturned++;

                if (br.isDelayed())
                    r.delayed++;
            }
        }
        return r;
    }


    public LibraryStats calculateStats() {
        LibraryStats s = new LibraryStats();

        long totalDays = 0;
        int count = 0;

        for (BorrowRecord br : library.records) {
            if (br.isReturned()) {
                long days = ChronoUnit.DAYS.between(
                        br.getStartDate(),
                        br.getReturnedDate()
                );
                totalDays += days;
                count++;
            }
        }

        s.avgBorrowDays = count == 0 ? 0 : (double) totalDays / count;
        return s;
    }
}
