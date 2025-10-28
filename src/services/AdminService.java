package services;

import java.util.*;
import models.*;
import utils.InputUtils;

public class AdminService {
    private LibraryService library;

    public AdminService(LibraryService library) {
        this.library = library;
    }

    public void menu(Scanner sc) {
        System.out.println("\n--- ورود مدیر سیستم ---");
        System.out.print("نام کاربری: ");
        String user = sc.nextLine();
        System.out.print("رمز عبور: ");
        String pass = sc.nextLine();
        if (!library.admin.login(user, pass)) {
            System.out.println("ورود نامعتبر!");
            return;
        }

        while (true) {
            System.out.println("\n--- منوی مدیر ---");
            System.out.println("1. تعریف کارمند جدید");
            System.out.println("2. مشاهده عملکرد کارمندان");
            System.out.println("3. مشاهده آمار امانت‌ها");
            System.out.println("4. مشاهده آمار دانشجویان");
            System.out.println("0. خروج");
            System.out.print("انتخاب: ");
            int ch = InputUtils.getInt(sc);

            switch (ch) {
                case 1 -> addEmployee(sc);
                case 2 -> showEmployeeStats();
                case 3 -> showBorrowStats();
                case 4 -> showStudentStats();
                case 0 -> { return; }
                default -> System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private void addEmployee(Scanner sc) {
        System.out.print("نام کاربری کارمند: ");
        String user = sc.nextLine();
        System.out.print("رمز عبور: ");
        String pass = sc.nextLine();
        library.employees.add(new Employee(user, pass));
        System.out.println("کارمند جدید ثبت شد.");
    }

    private void showEmployeeStats() {
        System.out.println("--- گزارش عملکرد کارمندان ---");
        for (Employee e : library.employees) {
            long booksAdded = library.books.stream()
                    .filter(b -> b.getRegisteredByEmployeeId() == e.getId())
                    .count();
            System.out.printf("%s → تعداد کتاب ثبت‌شده: %d\n", e, booksAdded);
        }
    }

    private void showBorrowStats() {
        System.out.println("--- آمار امانت‌ها ---");
        long totalReq = library.requests.size();
        long approved = library.requests.stream()
                .filter(r -> r.getStatus().equals("Approved")).count();
        System.out.println("کل درخواست‌ها: " + totalReq);
        System.out.println("کل امانت‌های تأییدشده: " + approved);
    }

    private void showStudentStats() {
        System.out.println("--- آمار دانشجویان ---");
        System.out.println("تعداد کل دانشجویان: " + library.students.size());
        System.out.println("لیست 10 دانشجوی برتر بر اساس تعداد درخواست:");
        library.students.stream()
                .sorted((a,b)-> Long.compare(
                        countRequests(b.getId()), countRequests(a.getId())
                ))
                .limit(10)
                .forEach(s -> System.out.println(s + " | تعداد درخواست: " + countRequests(s.getId())));
    }

    private long countRequests(int studentId) {
        return library.requests.stream().filter(r -> r.getStudentId() == studentId).count();
    }
}
