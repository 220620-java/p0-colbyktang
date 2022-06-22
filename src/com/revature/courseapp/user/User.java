// Project 0
// Made by Colby Tang for Revature

package src.com.revature.courseapp.user;

import java.util.Objects;

public abstract class User {
    enum UserType {
        STUDENT,
        FACULTY
    }

    private static int nextId = 100;
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    protected String email;
    protected UserType userType;

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
    
    // For testing
    public void resetNextID () {
        nextId = 100;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, password, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (id != other.id)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
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
