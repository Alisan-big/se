package ir.university.library.services;

import java.time.LocalDate;
import java.util.*;
import ir.university.library.models.*;
import ir.university.library.utils.InputUtils;

public class StudentService {
    private LibraryService library;

    public StudentService(LibraryService library) {
        this.library = library;
    }

    public void menu(Scanner sc) {
        System.out.println("\n--- student login ---");
        System.out.println("1. sign up");
        System.out.println("2. login");
        System.out.println("0. back");
        System.out.print("choice: ");
        int ch = InputUtils.getInt(sc);

        Student student = null;
        if (ch == 1) student = register(sc);
        else if (ch == 2) student = login(sc);
        else if (ch == 0) return;
        else System.out.println("invalid choice!");

        if (student == null) return;

        while (true) {
            System.out.println("\n--- student menu ---");
            System.out.println("1. search book");
            System.out.println("2. add a new borrow request");
            System.out.println("0. exit");
            System.out.print("choice: ");
            int choice = InputUtils.getInt(sc);

            switch (choice) {
                case 1 -> searchBooks(sc);
                case 2 -> requestBorrow(sc, student);
                case 0 -> { return; }
                default -> System.out.println("invalid choice!");
            }
        }
    }

    private Student register(Scanner sc) {
        System.out.print("username: ");
        String user = sc.nextLine();
        if (library.findStudentByUsername(user) != null) {
            System.out.println("this username already exists!");
            return null;
        }
        System.out.print("password: ");
        String pass = sc.nextLine();
        Student s = new Student(user, pass);
        library.students.add(s);
        System.out.println("sign up successful!.");
        return s;
    }

    private Student login(Scanner sc) {
        System.out.print("username: ");
        String user = sc.nextLine();
        System.out.print("password: ");
        String pass = sc.nextLine();
        Student s = library.findStudentByUsername(user);
        if (s == null || !s.getPassword().equals(pass)) {
            System.out.println("invalid credentials!.");
            return null;
        }
        if (!s.isActive()) {
            System.out.println("your account is inactive.");
            return null;
        }
        return s;
    }

    private void searchBooks(Scanner sc) {
        System.out.print("search term (title or author or year): ");
        String term = sc.nextLine().trim();
        library.books.stream()
                .filter(b -> b.getTitle().contains(term)
                        || b.getAuthor().contains(term)
                        || String.valueOf(b.getYear()).equals(term))
                .forEach(System.out::println);
    }

    private void requestBorrow(Scanner sc, Student s) {
        System.out.print("book id: ");
        int id = InputUtils.getInt(sc);
        Book book = library.findBookById(id);
        if (book == null) {
            System.out.println("book not found!");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("book is currently borrowed to a student!");
            return;
        }
        System.out.print("start date (yyyy-mm-dd): ");
        LocalDate start = LocalDate.parse(sc.nextLine());
        System.out.print("end date (yyyy-mm-dd): ");
        LocalDate end = LocalDate.parse(sc.nextLine());
        BorrowRequest req = new BorrowRequest(s.getId(), id, start, end);
        library.requests.add(req);
        System.out.println("borrow request registered and is waiting for approval.");
    }
}
