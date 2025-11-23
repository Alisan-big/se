package ir.university.library;

import java.time.LocalDate;
import java.util.*;
import ir.university.library.services.*;
import ir.university.library.utils.InputUtils;

public class Main {
    public static void main(String[] args) {
        LibraryService library = new LibraryService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== university library management system =====");
            System.out.println("1. login as guest");
            System.out.println("2. login as student");
            System.out.println("3. login as employee");
            System.out.println("4. login as admin");
            System.out.println("0. exit");
            System.out.print("your choice: ");
            int choice = InputUtils.getInt(sc);

            switch (choice) {
                case 1 -> new GuestService(library).menu(sc);
                case 2 -> new StudentService(library).menu(sc);
                case 3 -> new EmployeeService(library).menu(sc);
                case 4 -> new AdminService(library).menu(sc);
                case 0 -> {
                    System.out.println("exiting the app... good luck!");
                    return;
                }
                default -> System.out.println("invalid option!");
            }
        }
    }
}
