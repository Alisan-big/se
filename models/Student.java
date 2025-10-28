package models;

public class Student {
    private static int counter = 1;
    private int id;
    private String username;
    private String password;
    private boolean active = true;

    public Student(String username, String password) {
        this.id = counter++;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("دانشجو %s (id=%d) | وضعیت: %s",
                username, id, active ? "فعال" : "غیرفعال");
    }
}
