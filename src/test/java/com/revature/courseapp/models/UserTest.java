package com.revature.courseapp.models;

// Junit imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class UserTest {
    @AfterEach
    public void resetNextStudentID () {
        User.resetNextID();
    }

    @Test
    public void StudentInstantiation() {
        Student student = new Student(
            "John", 
            "Smith", 
            "jsmith", 
            "jsmith@email.com",
            "Comp Sci", 
            3.0f
        );
        
        
        assertEquals(101, student.getId());                       // id
        assertEquals("John", student.getFirstName());           // first name
        assertEquals("Smith", student.getLastName());           // last name
        assertEquals("jsmith", student.getUsername());          // username
        assertEquals("jsmith@email.com", student.getEmail());   // email
    }

    @Test 
    public void StudentToString() {
        Student student = new Student(
            1,
            "Joe", 
            "Test", 
            "jtest", 
            "jtest@email.com",
            "Comp Sci", 
            3.0f
        );
        
        String expected = "StudentID: 1, First Name: Joe, Last Name: Test, Username: jtest, Email: jtest@email.com, Major: Comp Sci, GPA: 3.000000";
        System.out.println(student);
        assertEquals(expected, student.toString());
        expected = "STUDENT";
        assertEquals(expected, student.userType.toString());
    }

    @Test 
    public void StudentHashCode() {
        Student student = new Student(101,
            "John", 
            "Smith", 
            "jsmith", 
            "jsmith@email.com",
            "Comp Sci", 
            3.0f
        );

        Student student2 = new Student(101,
            "John", 
            "Smith", 
            "jsmith", 
            "jsmith@email.com",
            "Comp Sci", 
            3.0f
        );
        assertTrue(student.hashCode() == student2.hashCode());
    }

    @Test
    public void FacultyInstantiation() {
        FacultyMember faculty = new FacultyMember(
            "Joe", 
            "Shmoe", 
            "jshmoe", 
            "jshmoe@email.com",
            "Comp Sci"
        );
        
        String expected = "FacultyID: 101, First Name: Joe, Last Name: Shmoe, Username: jshmoe, Email: jshmoe@email.com, Department: Comp Sci";
        System.out.println(faculty);
        assertEquals(expected, faculty.toString());
    }
}
