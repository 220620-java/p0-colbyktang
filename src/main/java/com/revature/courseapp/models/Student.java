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

package com.revature.courseapp.models;

/**
* A Student is a user that enroll in courses at a university.
* They can do the following: <ul> 
* <li>Register a new account with the system.</li>
* <li>View classes available for registration.</li>
* <li>Register for open and available courses.</li>
* <li>Cancel registration for a class if within a window.</li>
* <li>View registered classes.</li>
* </ul>
* @author Colby Tang
* @version 1.0
*/
public class Student extends User {
    private String major;
    private float gpa;

    public String getMajor() {
        return major;
    }

    public void setMajor (String major) {
        this.major = major;
    }

    public float getGpa () {
        return gpa;
    }

    public void setGpa (float gpa) {
        this.gpa = gpa;
    }

    // Constructor with an auto incrementing id
    public Student (String first, String last, String username, String email, String major, float gpa) {
        super(first, last, username, email);
        userType = UserType.STUDENT;
        this.major = major;
        this.gpa = gpa;
    }
    // Constructor with a defined id
    public Student (int id, String first, String last, String username, String email, String major, float gpa) {
        super(id, first, last, username, email);
        userType = UserType.STUDENT;
        this.major = major;
        this.gpa = gpa;
    }
    
    /**
     * A String template for printing out the fields of a Student
     */
    private static final String ToStringTemplate = "StudentID: %1s, First Name: %2s, Last Name: %3s, Username: %4s, Email: %5s, Major: %6s, GPA: %7f";

    /** 
     * @return String
     */
    public String toString () {
        return String.format (ToStringTemplate, this.id, this.firstName, this.lastName, this.username, this.email, this.major, this.gpa);
    }
}