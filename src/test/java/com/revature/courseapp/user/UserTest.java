package com.revature.courseapp.user;

// Junit imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class UserTest {
    
    @Test
    public void StudentInstantiation() {
        Student student = new Student(
            "John", 
            "Smith", 
            "jsmith", 
            "jsmith@email.com"
        );
        
        student.resetNextID();
        assertEquals(101, student.getId());                       // id
        assertEquals("John", student.getFirstName());           // first name
        assertEquals("Smith", student.getLastName());           // last name
        assertEquals("jsmith", student.getUsername());          // username
        assertEquals("jsmith@email.com", student.getEmail());   // email
    }

    @Test 
    public void StudentToString() {
        Student student = new Student(
            101,
            "John", 
            "Smith", 
            "jsmith", 
            "jsmith@email.com"
        );
        
        student.resetNextID();
        String expected = "StudentID: = 101, First Name = John, Last Name = Smith, Username = jsmith, Email = jsmith@email.com";
        System.out.println(student);
        assertEquals(expected, student.toString());
        expected = "STUDENT";
        assertEquals(expected, student.userType.toString());
    }

    @Test 
    public void StudentHashCode() {
        Student student = new Student(
            "John", 
            "Smith", 
            "jsmith", 
            "jsmith@email.com"
        );
        student.resetNextID();

        Student student2 = new Student(
            "John", 
            "Smith", 
            "jsmith", 
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
            "jshmoe@email.com"
        );
        
        faculty.resetNextID();
        String expected = "FacultyID: = 101, First Name = Joe, Last Name = Shmoe, Username = jshmoe, Email = jshmoe@email.com";
        System.out.println(faculty);
        assertEquals(expected, faculty.toString());
    }
}
