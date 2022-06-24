package com.revature;

// Junit imports
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.*;

import com.revature.courseapp.user.*;

public class UserTest {
    
    @Test
    public void StudentInstantiation() {
        Student student = new Student(
            "John", 
            "Smith", 
            "jsmith", 
            "pass", 
            "jsmith@email.com"
        );
        
        student.resetNextID();
        assertEquals(101, student.getId());                       // id
        assertEquals("John", student.getFirstName());           // first name
        assertEquals("Smith", student.getLastName());           // last name
        assertEquals("jsmith", student.getUsername());          // username
        assertTrue(student.validatePassword("pass")); // password
        assertEquals("jsmith@email.com", student.getEmail());   // email
    }

    @Test 
    public void StudentToString() {
        Student student = new Student(
            "John", 
            "Smith", 
            "jsmith", 
            "pass", 
            "jsmith@email.com"
        );
        
        student.resetNextID();
        String expected = "StudentID: = 101, First Name = John, Last Name = Smith, Username = jsmith, Email = jsmith@email.com";
        System.out.println(student);
        assertEquals(expected, student.toString());
    }

    @Test 
    public void StudentHashCode() {
        Student student = new Student(
            "John", 
            "Smith", 
            "jsmith", 
            "pass", 
            "jsmith@email.com"
        );
        student.resetNextID();

        Student student2 = new Student(
            "John", 
            "Smith", 
            "jsmith", 
            "pass", 
            "jsmith@email.com"
        );
        student.resetNextID();
        
        assertTrue(student.hashCode() == student2.hashCode());
    }

    @Test
    public void FacultyInstantiation() {
        FacultyMember faculty = new FacultyMember(
            "Joe", 
            "Shmoe", 
            "jshmoe", 
            "pass", 
            "jshmoe@email.com"
        );
        
        faculty.resetNextID();
        String expected = "FacultyID: = 101, First Name = Joe, Last Name = Shmoe, Username = jshmoe, Email = jshmoe@email.com";
        System.out.println(faculty);
        assertEquals(expected, faculty.toString());
        assertTrue(faculty.validatePassword("pass")); // password
    }
}
