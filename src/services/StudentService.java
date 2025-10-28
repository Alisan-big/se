package services;

import java.time.LocalDate;
import java.util.*;
import models.*;
import utils.InputUtils;

public class StudentService {
    private LibraryService library;

    public StudentService(LibraryService library) {
        this.library = library;
    }

    public void menu(Scanner sc) {
        System.out.println("\n--- ورود دانشجو ---");
        System.out.println("1. ثبت‌نام");
        System.out.println("2. ورود");
        System.out.print("انتخاب شما: ");
        int ch = InputUtils.getInt(sc);

        Student student = null;
        if (ch == 1) student = register(sc);
        else if (ch == 2) student = login(sc);
        else return;

        if (student == null) return;

        while (true) {
            System.out.println("\n--- منوی دانشجو ---");
            System.out.println("1. جستجوی کتاب");
            System.out.println("2. ثبت درخواست امانت");
            System.out.println("0. خروج");
            System.out.print("انتخاب: ");
            int choice = InputUtils.getInt(sc);

            switch (choice) {
                case 1 -> searchBooks(sc);
                case 2 -> requestBorrow(sc, student);
                case 0 -> { return; }
                default -> System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private Student register(Scanner sc) {
        System.out.print("نام کاربری: ");
        String user = sc.nextLine();
        if (library.findStudentByUsername(user) != null) {
            System.out.println("این نام کاربری قبلاً ثبت شده است.");
            return null;
        }
        System.out.print("رمز عبور: ");
        String pass = sc.nextLine();
        Student s = new Student(user, pass);
        library.students.add(s);
        System.out.println("ثبت‌نام با موفقیت انجام شد.");
        return s;
    }

    private Student login(Scanner sc) {
        System.out.print("نام کاربری: ");
        String user = sc.nextLine();
        System.out.print("رمز عبور: ");
        String pass = sc.nextLine();
        Student s = library.findStudentByUsername(user);
        if (s == null || !s.getPassword().equals(pass)) {
            System.out.println("اطلاعات ورود اشتباه است.");
            return null;
        }
        if (!s.isActive()) {
            System.out.println("حساب شما غیرفعال است.");
            return null;
        }
        return s;
    }

    private void searchBooks(Scanner sc) {
        System.out.print("عبارت جستجو (عنوان یا نویسنده یا سال): ");
        String term = sc.nextLine().trim();
        library.books.stream()
                .filter(b -> b.getTitle().contains(term)
                        || b.getAuthor().contains(term)
                        || String.valueOf(b.getYear()).equals(term))
                .forEach(System.out::println);
    }

    private void requestBorrow(Scanner sc, Student s) {
        System.out.print("کد کتاب: ");
        int id = InputUtils.getInt(sc);
        Book book = library.findBookById(id);
        if (book == null) {
            System.out.println("کتاب یافت نشد!");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("کتاب در حال حاضر امانت داده شده است!");
            return;
        }
        System.out.print("تاریخ شروع (yyyy-mm-dd): ");
        LocalDate start = LocalDate.parse(sc.nextLine());
        System.out.print("تاریخ پایان (yyyy-mm-dd): ");
        LocalDate end = LocalDate.parse(sc.nextLine());
        BorrowRequest req = new BorrowRequest(s.getId(), id, start, end);
        library.requests.add(req);
        System.out.println("درخواست امانت ثبت شد و در انتظار تأیید است.");
    }
}
