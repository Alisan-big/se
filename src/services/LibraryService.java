package services;

import java.util.*;
import models.*;

public class LibraryService {
    public List<Book> books = new ArrayList<>();
    public List<Student> students = new ArrayList<>();
    public List<Employee> employees = new ArrayList<>();
    public List<BorrowRequest> requests = new ArrayList<>();
    public List<BorrowRecord> records = new ArrayList<>();
    public Admin admin = new Admin();

    public LibraryService() {
        // چند داده اولیه برای تست
        books.add(new Book("مهندسی نرم‌افزار", "پرسمن", 2015, 1));
        books.add(new Book("طراحی سیستم", "Dennis", 2020, 1));
        employees.add(new Employee("emp1", "123"));
    }

    public Book findBookById(int id) {
        for (Book b : books)
            if (b.getId() == id) return b;
        return null;
    }

    public Student findStudentByUsername(String username) {
        for (Student s : students)
            if (s.getUsername().equals(username)) return s;
        return null;
    }

    public Employee findEmployeeByUsername(String username) {
        for (Employee e : employees)
            if (e.getUsername().equals(username)) return e;
        return null;
    }
}
