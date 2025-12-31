package ir.university.library.services;

import ir.university.library.models.Student;

public class AuthService {

    private LibraryService library;

    public AuthService(LibraryService library) {
        this.library = library;
    }

    // سناریو 1-1 و 1-2
    public boolean register(String username, String password) {
        if (library.findStudentByUsername(username) != null) {
            return false;
        }
        library.students.add(new Student(username, password));
        return true;
    }

    // سناریو 1-3 تا 1-5
    public boolean login(String username, String password) {
        Student s = library.findStudentByUsername(username);
        if (s == null) return false;
        if (!s.getPassword().equals(password)) return false;
        return true;
    }
}
