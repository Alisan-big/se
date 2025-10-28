package models;

public class Admin {
    private String username;
    private String password;

    public Admin() {
        this.username = "admin";
        this.password = "admin";
    }

    public boolean login(String user, String pass) {
        return username.equals(user) && password.equals(pass);
    }
}
