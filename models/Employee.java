package models;

public class Employee {
    private static int counter = 1;
    private int id;
    private String username;
    private String password;

    public Employee(String username, String password) {
        this.id = counter++;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return String.format("کارمند %s (id=%d)", username, id);
    }
}
