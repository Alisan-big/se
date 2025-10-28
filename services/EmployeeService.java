package services;

import java.time.LocalDate;
import java.util.*;
import models.*;
import utils.InputUtils;

public class EmployeeService {
    private LibraryService library;

    public EmployeeService(LibraryService library) {
        this.library = library;
    }

    public void menu(Scanner sc) {
        System.out.println("\n--- ورود کارمند ---");
        System.out.print("نام کاربری: ");
        String user = sc.nextLine();
        System.out.print("رمز عبور: ");
        String pass = sc.nextLine();
        Employee emp = library.findEmployeeByUsername(user);
        if (emp == null || !emp.getPassword().equals(pass)) {
            System.out.println("ورود نامعتبر!");
            return;
        }

        while (true) {
            System.out.println("\n--- منوی کارمند ---");
            System.out.println("1. تغییر رمز عبور");
            System.out.println("2. ثبت کتاب جدید");
            System.out.println("3. جستجو و ویرایش کتاب");
            System.out.println("4. بررسی و تأیید درخواست‌ها");
            System.out.println("5. گزارش امانت دانشجو");
            System.out.println("6. فعال/غیرفعال کردن دانشجو");
            System.out.println("7. ثبت تحویل یا بازگشت کتاب");
            System.out.println("0. خروج");
            System.out.print("انتخاب: ");
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
                default -> System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private void changePassword(Scanner sc, Employee emp) {
        System.out.print("رمز جدید: ");
        emp.setPassword(sc.nextLine());
        System.out.println("رمز عبور تغییر کرد.");
    }

    private void addBook(Scanner sc, Employee emp) {
        System.out.print("عنوان کتاب: ");
        String title = sc.nextLine();
        System.out.print("نام نویسنده: ");
        String author = sc.nextLine();
        System.out.print("سال نشر: ");
        int year = InputUtils.getInt(sc);
        Book b = new Book(title, author, year, emp.getId());
        library.books.add(b);
        System.out.println("کتاب ثبت شد.");
    }

    private void editBook(Scanner sc) {
        System.out.print("کد کتاب: ");
        int id = InputUtils.getInt(sc);
        Book b = library.findBookById(id);
        if (b == null) {
            System.out.println("کتاب یافت نشد!");
            return;
        }
        System.out.print("عنوان جدید: ");
        b.setAvailable(true); // در صورت ویرایش، دوباره فعال
        System.out.println("ویرایش انجام شد (در این نسخه ساده فقط وضعیت بازنشانی می‌شود).");
    }

    private void handleBorrowReturn(Scanner sc) {
        System.out.println("1. ثبت تحویل کتاب به دانشجو");
        System.out.println("2. ثبت برگشت کتاب از دانشجو");
        System.out.print("انتخاب: ");
        int ch = InputUtils.getInt(sc);

        if (ch == 1) {
            System.out.print("کد درخواست امانت تأیید شده: ");
            int id = InputUtils.getInt(sc);
            BorrowRequest req = library.requests.stream()
                    .filter(r -> r.getId() == id && r.getStatus().equals("Approved"))
                    .findFirst().orElse(null);
            if (req == null) {
                System.out.println("درخواست معتبر یافت نشد!");
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
            System.out.println("کتاب تحویل دانشجو شد و رکورد ثبت گردید.");

        } else if (ch == 2) {
            System.out.print("کد رکورد امانت: ");
            int id = InputUtils.getInt(sc);
            BorrowRecord rec = library.records.stream()
                    .filter(r -> r.getId() == id && !r.isReturned())
                    .findFirst().orElse(null);
            if (rec == null) {
                System.out.println("رکورد یافت نشد یا قبلاً برگشت داده شده.");
                return;
            }
            rec.setReturnedDate(LocalDate.now());
            Book b = library.findBookById(rec.getBookId());
            if (b != null) b.setAvailable(true);
            System.out.println("کتاب با موفقیت بازگردانده شد.");
            if (rec.isDelayed())
                System.out.println("⚠️ این امانت با تأخیر بازگردانده شده است!");
        }
    }

    private void approveRequests(Scanner sc, Employee emp) {
        LocalDate today = LocalDate.now();
        for (BorrowRequest r : library.requests) {
            if (r.getStatus().equals("Pending") &&
                    (r.getStartDate().equals(today) || r.getStartDate().equals(today.minusDays(1)))) {
                System.out.println(r);
                System.out.print("تأیید این درخواست؟ (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    r.setStatus("Approved");
                    Book b = library.findBookById(r.getBookId());
                    if (b != null) b.setAvailable(false);
                    System.out.println("درخواست تأیید شد.");
                }
            }
        }
    }

    private void reportStudent(Scanner sc) {
        System.out.print("نام کاربری دانشجو: ");
        String user = sc.nextLine();
        Student s = library.findStudentByUsername(user);
        if (s == null) {
            System.out.println("دانشجو یافت نشد!");
            return;
        }

        List<BorrowRecord> recs = library.records.stream()
                .filter(r -> r.getStudentId() == s.getId())
                .toList();

        long total = recs.size();
        long unreturned = recs.stream().filter(r -> !r.isReturned()).count();
        long delayed = recs.stream().filter(BorrowRecord::isDelayed).count();

        System.out.println("📘 گزارش امانت‌های دانشجو:");
        System.out.println("تعداد کل امانت‌ها: " + total);
        System.out.println("تعداد تحویل‌داده‌نشده‌ها: " + unreturned);
        System.out.println("تعداد با تأخیر بازگشتی: " + delayed);

        recs.forEach(System.out::println);
    }

    private void toggleStudent(Scanner sc) {
        System.out.print("نام کاربری دانشجو: ");
        String user = sc.nextLine();
        Student s = library.findStudentByUsername(user);
        if (s == null) {
            System.out.println("دانشجو یافت نشد!");
            return;
        }
        s.setActive(!s.isActive());
        System.out.println("وضعیت جدید: " + (s.isActive() ? "فعال" : "غیرفعال"));
    }
}
