// Project 0
// Made by Colby Tang for Revature

package com.revature.courseapp.user;

import java.util.Objects;

/**
* A user is a representation of the client. They contain personal information about the user such as their name and email.
* When creating a user without an id, an id will be automatically assigned to the user. 
* @author Colby Tang
* @version 1.0
*/
public abstract class User {
    public enum UserType {
        STUDENT,
        FACULTY
    }

    private static int nextId = 100;
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String email;
    protected UserType userType;
    protected byte[] salt;

    
    /** 
     * @return int
     */
    // Getters
    public int getId () {
        return id;
    }

    
    /** 
     * @return String
     */
    public String getFirstName () {
        return firstName;
    }

    
    /** 
     * @return String
     */
    public String getLastName () {
        return lastName;
    }

    
    /** 
     * @return String
     */
    public String getUsername () {
        return username;
    }

    
    /** 
     * @return String
     */
    public String getEmail () {
        return email;
    }

    
    /** 
     * @return UserType
     */
    public UserType getUserType () {
        return userType;
    }

    // For testing
    public static void resetNextID () {
        nextId = 100;
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, email);
    }

    
    /** 
     * @param obj
     * @return boolean
     */
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
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    // Constructor with a defined id
    protected User (int id, String first, String last, String username, String email) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.username = username;
        this.email = email;
    }

    // Constructor with an auto incrementing id
    protected User (String first, String last, String username, String email) {
        nextId++;
        id = nextId;
        this.firstName = first;
        this.lastName = last;
        this.username = username;
        this.email = email;
    }
}
