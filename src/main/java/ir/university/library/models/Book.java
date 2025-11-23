package ir.university.library.models;

public class Book {
    private static int counter = 1;
    private int id;
    private String title;
    private String author;
    private int year;
    private boolean available = true;
    private int registeredByEmployeeId;

    public Book(String title, String author, int year, int empId) {
        this.id = counter++;
        this.title = title;
        this.author = author;
        this.year = year;
        this.registeredByEmployeeId = empId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public int getRegisteredByEmployeeId() { return registeredByEmployeeId; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%d) author: %s | %s",
                id, title, year, author, available ? "available" : "borrowed");
    }
}
