package src.com.revature.test;

// Junit imports
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.*;

// Student imports
import src.com.revature.user.Student;
import src.com.revature.user.FacultyMember;

public class UserTesting {
    
    @Test
    public void StudentInstantiation() {
        Student student = new Student(
            "John", 
            "Smith", 
            "jsmith", 
            "pass", 
            "jsmith@email.com"
        );
        
        assertEquals(1, student.getId());                       // id
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
        
        String expected = "StudentID: = 1, First Name = John, Last Name = Smith, Username = jsmith, Email = jsmith@email.com";
        System.out.println(student);
        assertEquals(expected, student.toString());
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
        
        // assertEquals(2, faculty.getId());
        assertEquals("Joe", faculty.getFirstName());           // first name
        assertEquals("Shmoe", faculty.getLastName());           // last name
        assertEquals("jshmoe", faculty.getUsername());          // username
        assertTrue(faculty.validatePassword("pass")); // password
        assertEquals("jshmoe@email.com", faculty.getEmail());   // email
    }
}
