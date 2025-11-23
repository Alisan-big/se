package ir.university.library.services;

import java.util.*;
import ir.university.library.models.*;
import ir.university.library.utils.InputUtils;

public class GuestService {
    private LibraryService library;

    public GuestService(LibraryService library) {
        this.library = library;
    }

    public void menu(Scanner sc) {
        while (true) {
            System.out.println("\n--- guest user menu ---");
            System.out.println("1. see the number of students");
            System.out.println("2. search book title");
            System.out.println("3. see stats");
            System.out.println("0. back");
            System.out.print("choice: ");
            int ch = InputUtils.getInt(sc);

            switch (ch) {
                case 1 -> System.out.println("number of registered students: " + library.students.size());
                case 2 -> searchBooks(sc);
                case 3 -> showStats();
                case 0 -> { return; }
                default -> System.out.println("invalid choice!");
            }
        }
    }

    private void searchBooks(Scanner sc) {
        System.out.print("book title: ");
        String title = sc.nextLine();
        library.books.stream()
                .filter(b -> b.getTitle().contains(title))
                .forEach(System.out::println);
    }

    private void showStats() {
        System.out.println("total students: " + library.students.size());
        System.out.println("total books: " + library.books.size());
        System.out.println("total requests: " + library.requests.size());
        long borrowed = library.requests.stream()
                .filter(r -> r.getStatus().equals("Approved"))
                .count();
        System.out.println("number of borrowed books: " + borrowed);
    }
}
