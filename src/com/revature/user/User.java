// Project 0
// Made by Colby Tang for Revature

package src.com.revature.user;

public class User {
    private static int nextId = 0;
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    protected String email;

    // Pre-Increment nextId whenever a user is created
    {
        nextId++;
    }

    // Getters
    public int getId () {
        return id;
    }

    public String getFirstName () {
        return firstName;
    }

    public String getLastName () {
        return lastName;
    }

    public String getUsername () {
        return username;
    }

    public String getEmail () {
        return email;
    }

    protected User (String first, String last, String username, String password, String email) {
        id = nextId;
        this.firstName = first;
        this.lastName = last;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public boolean validatePassword (String password) {
        return this.password.equals(password);
    }
}
