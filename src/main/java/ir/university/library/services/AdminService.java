package ir.university.library.services;

import java.util.*;
import ir.university.library.models.*;
import ir.university.library.utils.InputUtils;

public class AdminService {
    private LibraryService library;

    public AdminService(LibraryService library) {
        this.library = library;
    }

    public void menu(Scanner sc) {
        System.out.println("\n--- system admin login ---");
        System.out.print("username: ");
        String user = sc.nextLine();
        System.out.print("password: ");
        String pass = sc.nextLine();
        if (!library.admin.login(user, pass)) {
            System.out.println("invalid credentials!");
            return;
        }

        while (true) {
            System.out.println("\n--- admin menu ---");
            System.out.println("1. create a new employee");
            System.out.println("2. see employees' performance");
            System.out.println("3. see borrows' stats");
            System.out.println("4. see students' stats");
            System.out.println("0. exit");
            System.out.print("choice: ");
            int ch = InputUtils.getInt(sc);

            switch (ch) {
                case 1 -> addEmployee(sc);
                case 2 -> showEmployeeStats();
                case 3 -> showBorrowStats();
                case 4 -> showStudentStats();
                case 0 -> { return; }
                default -> System.out.println("invalid choice!");
            }
        }
    }

    private void addEmployee(Scanner sc) {
        System.out.print("username of employee to add: ");
        String user = sc.nextLine();
        System.out.print("password of employee to add: ");
        String pass = sc.nextLine();
        library.employees.add(new Employee(user, pass));
        System.out.println("new employee added.");
    }

    private void showEmployeeStats() {
        System.out.println("--- employees stats report ---");
        for (Employee e : library.employees) {
            long booksAdded = library.books.stream()
                    .filter(b -> b.getRegisteredByEmployeeId() == e.getId())
                    .count();
            System.out.printf("%s → books added: %d\n", e, booksAdded);
        }
    }

    private void showBorrowStats() {
        System.out.println("--- borrow stats ---");
        long totalReq = library.requests.size();
        long approved = library.requests.stream()
                .filter(r -> r.getStatus().equals("Approved")).count();
        System.out.println("total requests: " + totalReq);
        System.out.println("approved requests: " + approved);
    }

    private void showStudentStats() {
        System.out.println("--- students stats report ---");
        System.out.println("total students: " + library.students.size());
        System.out.println("top 10 students based on number of requests: ");
        library.students.stream()
                .sorted((a,b)-> Long.compare(
                        countRequests(b.getId()), countRequests(a.getId())
                ))
                .limit(10)
                .forEach(s -> System.out.println(s + " | number of requests: " + countRequests(s.getId())));
    }

    private long countRequests(int studentId) {
        return library.requests.stream().filter(r -> r.getStudentId() == studentId).count();
    }
}
