// Project 0
// Made by Colby Tang for Revature

/*
 *   As a student, I can:
 * - register a new account with the system (must be secured with a password)
 * - view classes available for registration
 * - register for an open and available class
 * - cancel my registration for a class (if within window)
 * - view the classes that I have registered for
 */

package com.revature.courseapp.user;

public class Student extends User {
    // Constructor with an auto incrementing id
    public Student (String first, String last, String username, String email) {
        super(first, last, username, email);
        userType = UserType.STUDENT;
    }
    // Constructor with a defined id
    public Student (int id, String first, String last, String username, String email) {
        super(id, first, last, username, email);
        userType = UserType.STUDENT;
    }
    
    private static final String ToStringTemplate = "StudentID: = %1s, First Name = %2s, Last Name = %3s, Username = %4s, Email = %5s";

    
    /** 
     * @return String
     */
    public String toString () {
        return String.format (ToStringTemplate, this.id, this.firstName, this.lastName, this.username, this.email);
    }
}