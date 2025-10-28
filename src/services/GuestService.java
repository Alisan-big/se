package services;

import java.util.*;
import models.*;
import utils.InputUtils;

public class GuestService {
    private LibraryService library;

    public GuestService(LibraryService library) {
        this.library = library;
    }

    public void menu(Scanner sc) {
        while (true) {
            System.out.println("\n--- منوی کاربر مهمان ---");
            System.out.println("1. مشاهده تعداد دانشجویان");
            System.out.println("2. جستجوی کتاب بر اساس نام");
            System.out.println("3. مشاهده آمار کلی");
            System.out.println("0. بازگشت");
            System.out.print("انتخاب شما: ");
            int ch = InputUtils.getInt(sc);

            switch (ch) {
                case 1 -> System.out.println("تعداد دانشجویان ثبت‌نام‌شده: " + library.students.size());
                case 2 -> searchBooks(sc);
                case 3 -> showStats();
                case 0 -> { return; }
                default -> System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private void searchBooks(Scanner sc) {
        System.out.print("نام کتاب: ");
        String title = sc.nextLine();
        library.books.stream()
                .filter(b -> b.getTitle().contains(title))
                .forEach(System.out::println);
    }

    private void showStats() {
        System.out.println("تعداد کل دانشجویان: " + library.students.size());
        System.out.println("تعداد کل کتاب‌ها: " + library.books.size());
        System.out.println("تعداد کل درخواست‌ها: " + library.requests.size());
        long borrowed = library.requests.stream()
                .filter(r -> r.getStatus().equals("Approved"))
                .count();
        System.out.println("تعداد کتاب‌های در امانت: " + borrowed);
    }
}
