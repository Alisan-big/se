package ir.university.library.services;

import java.util.*;
import ir.university.library.models.*;

public class LibraryService {
    public List<Book> books = new ArrayList<>();
    public List<Student> students = new ArrayList<>();
    public List<Employee> employees = new ArrayList<>();
    public List<BorrowRequest> requests = new ArrayList<>();
    public List<BorrowRecord> records = new ArrayList<>();
    public Admin admin = new Admin();

    public LibraryService() {
        books.add(new Book("software engineering", "Pressman", 2015, 1));
        books.add(new Book("system design", "Dennis", 2020, 1));
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

    public Student findStudentById(int id) {
        for (Student s : students)
            if (s.getId() == id) return s;
        return null;
    }

    public Employee findEmployeeByUsername(String username) {
        for (Employee e : employees)
            if (e.getUsername().equals(username)) return e;
        return null;
    }

    public BorrowRequest findBorrowRequestById(int id) {
        for (BorrowRequest r : requests)
            if (r.getId() == id) return r;
        return null;
    }

    public BorrowRecord findBorrowRecordById(int id) {
        for (BorrowRecord r : records)
            if (r.getId() == id) return r;
        return null;
    }
}
