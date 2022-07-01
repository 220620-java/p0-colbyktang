package com.revature.courseapp.models;

/**
* A Faculty Member is a user with the ability to add, change, and remove courses.
* They can do the following: <ul>
* <li> Add new classes to the registration catalog </li>
* <li> Change the registration details for a class </li>
* <li> Remove a class from the registration catalog (this should unregister all registered students) </li>
* </ul>
* @author Colby Tang
* @version 1.0
*/
public class FacultyMember extends User {

    // Constructor with an auto incrementing id
    public FacultyMember (String first, String last, String username, String email) {
        super(first, last, username, email);
        userType = UserType.FACULTY;
    }

    // Constructor with a defined id
    public FacultyMember (int id, String first, String last, String username, String email) {
        super(id, first, last, username, email);
        userType = UserType.FACULTY;
    }
    
    /**
     * A String template for printing out the fields of a FacultyMember
     */
    private static final String ToStringTemplate = "FacultyID: = %1s, First Name = %2s, Last Name = %3s, Username = %4s, Email = %5s";

    /** 
     * @return String
     */
    public String toString () {
        return String.format (ToStringTemplate, this.id, this.firstName, this.lastName, this.username, this.email);
    }
}