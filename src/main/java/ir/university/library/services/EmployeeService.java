package ir.university.library.services;

import java.time.LocalDate;
import java.util.*;
import ir.university.library.models.*;
import ir.university.library.utils.InputUtils;

public class EmployeeService {
    private LibraryService library;

    public EmployeeService(LibraryService library) {
        this.library = library;
    }

    public void menu(Scanner sc) {
        System.out.println("\n--- employee login ---");
        System.out.print("username: ");
        String user = sc.nextLine();
        System.out.print("password: ");
        String pass = sc.nextLine();
        Employee emp = library.findEmployeeByUsername(user);
        if (emp == null || !emp.getPassword().equals(pass)) {
            System.out.println("invalid credentials!");
            return;
        }

        while (true) {
            System.out.println("\n--- employee menu ---");
            System.out.println("1. change password");
            System.out.println("2. add a new book");
            System.out.println("3. search and edit book");
            System.out.println("4. check and approve requests");
            System.out.println("5. students borrow report");
            System.out.println("6. make a student active/inactive");
            System.out.println("7. register a borrow or return");
            System.out.println("0. exit");
            System.out.print("choice: ");
            int ch = InputUtils.getInt(sc);

            switch (ch) {
                case 1 -> changePassword(sc, emp);
                case 2 -> addBook(sc, emp);
                case 3 -> editBook(sc);
                case 4 -> approveRequests(sc, emp);
                case 5 -> reportStudent(sc);
                case 6 -> toggleStudent(sc);
                case 7 -> handleBorrowReturn(sc);
                case 0 -> { return; }
                default -> System.out.println("invalid choice!");
            }
        }
    }

    private void changePassword(Scanner sc, Employee emp) {
        System.out.print("new password: ");
        emp.setPassword(sc.nextLine());
        System.out.println("password has been changed!");
    }

    private void addBook(Scanner sc, Employee emp) {
        System.out.print("book title: ");
        String title = sc.nextLine();
        System.out.print("author: ");
        String author = sc.nextLine();
        System.out.print("year of publishing: ");
        int year = InputUtils.getInt(sc);
        Book b = new Book(title, author, year, emp.getId());
        library.books.add(b);
        System.out.println("book added.");
    }

    private void editBook(Scanner sc) {
        System.out.print("book id: ");
        int id = InputUtils.getInt(sc);
        Book b = library.findBookById(id);
        if (b == null) {
            System.out.println("book not found!");
            return;
        }
        System.out.print("new title: ");
        b.setAvailable(true); // در صورت ویرایش، دوباره فعال
        System.out.println("book edited successfully!.");
    }

    private void handleBorrowReturn(Scanner sc) {
        System.out.println("1. register a borrow");
        System.out.println("2. register a return");
        System.out.print("choice: ");
        int ch = InputUtils.getInt(sc);

        if (ch == 1) {
            System.out.print("approved borrow request id: ");
            int id = InputUtils.getInt(sc);
            BorrowRequest req = library.requests.stream()
                    .filter(r -> r.getId() == id && r.getStatus().equals("Approved"))
                    .findFirst().orElse(null);
            if (req == null) {
                System.out.println("valid request not found!");
                return;
            }
            BorrowRecord rec = new BorrowRecord(
                    req.getStudentId(),
                    req.getBookId(),
                    req.getStartDate(),
                    req.getEndDate()
            );
            rec.setReceivedDate(LocalDate.now());
            library.records.add(rec);
            System.out.println("the book got borrowed and the record registered successfully!.");

        } else if (ch == 2) {
            System.out.print("borrow id: ");
            int id = InputUtils.getInt(sc);
            BorrowRecord rec = library.records.stream()
                    .filter(r -> r.getId() == id && !r.isReturned())
                    .findFirst().orElse(null);
            if (rec == null) {
                System.out.println("record not found or already returned.");
                return;
            }
            rec.setReturnedDate(LocalDate.now());
            Book b = library.findBookById(rec.getBookId());
            if (b != null) b.setAvailable(true);
            System.out.println("book returned successfully!.");
            if (rec.isDelayed())
                System.out.println("⚠️ book returned with delay!");
        }
    }

    private void approveRequests(Scanner sc, Employee emp) {
        LocalDate today = LocalDate.now();
        for (BorrowRequest r : library.requests) {
            if (r.getStatus().equals("Pending") &&
                    (r.getStartDate().equals(today) || r.getStartDate().equals(today.minusDays(1)))) {
                System.out.println(r);
                System.out.print("approve this request? (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    r.setStatus("Approved");
                    Book b = library.findBookById(r.getBookId());
                    if (b != null) b.setAvailable(false);
                    System.out.println("request approved successfully!.");
                }
            }
        }
    }

    private void reportStudent(Scanner sc) {
        System.out.print("student username: ");
        String user = sc.nextLine();
        Student s = library.findStudentByUsername(user);
        if (s == null) {
            System.out.println("student not found!");
            return;
        }

        List<BorrowRecord> recs = library.records.stream()
                .filter(r -> r.getStudentId() == s.getId())
                .toList();

        long total = recs.size();
        long unreturned = recs.stream().filter(r -> !r.isReturned()).count();
        long delayed = recs.stream().filter(BorrowRecord::isDelayed).count();

        System.out.println("📘 student's borrow report:");
        System.out.println("total borrows: " + total);
        System.out.println("number of unreturned: " + unreturned);
        System.out.println("number of delayed returns: " + delayed);

        recs.forEach(System.out::println);
    }

    private void toggleStudent(Scanner sc) {
        System.out.print("student username: ");
        String user = sc.nextLine();
        Student s = library.findStudentByUsername(user);
        if (s == null) {
            System.out.println("student not found!");
            return;
        }
        s.setActive(!s.isActive());
        System.out.println("new status: " + (s.isActive() ? "active" : "inactive"));
    }
}
