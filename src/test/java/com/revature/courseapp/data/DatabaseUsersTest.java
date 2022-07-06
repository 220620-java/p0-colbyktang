package com.revature.courseapp.data;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.revature.courseapp.models.FacultyMember;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;
import com.revature.courseapp.utils.Encryption;
import com.revature.courseapp.utils.List;

// Mockito imports
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

public class DatabaseUsersTest {

    @InjectMocks
    private UserDAO mockUserDAO;

    static UserDAO userDAO;

    @BeforeAll
    public void OpenDatabase () {
        userDAO = new UserPostgres("local_db.json");
        mockUserDAO = Mockito.mock (userDAO.getClass());
    }


    @Test
    public void testGetAllUsers() {
        List<User> allUsers = userDAO.findAll();
        System.out.println(allUsers);
    }

    @Test
    public void testDoesUserExist () {
        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com", "Comp Sci", 3.0f);
        assertTrue (userDAO.doesUserExist(student.getUsername()));
        Student noStudent = new Student (600, "Nobody", "Test", "ntest", "ntest@email.com", "Comp Sci", 3.0f);
        assertFalse (userDAO.doesUserExist(noStudent.getUsername()));
    }

    @Test
    public void testInsertUser() {
        // Prepare the database for insertion.
        DatabaseUtils.truncateTable("users");

        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com", "Comp Sci", 3.0f);
        Student student2 = new Student ("John", "Test", "jotest", "jotest@email.com", "Comp Sci", 3.0f);
        Student student3 = new Student ("Jack", "Test", "jatest", "jatest@email.com", "Comp Sci", 3.0f);
        Student student4 = new Student ("Colby", "Tang", "ctang", "ctang@email.com", "Comp Sci", 3.0f);
        FacultyMember facultyMember = new FacultyMember("Colby", "Tang", "ctang2", "ctang2@email.com", "Comp Sci");
        String pass = "pass";
        assertNotNull(userDAO.create(student, pass));
        assertNotNull (userDAO.create(student2, pass));
        assertNotNull (userDAO.create(student3, pass));
        assertNotNull (userDAO.create(student4, pass));
        assertNotNull (userDAO.create(facultyMember, pass));

        // See if it catches that member is already inside the database.
        assertNull (userDAO.create(facultyMember, pass));
    }

    @Test
    public void testValidatePassword () {
        Student student = new Student (202, "Vladimir", "Password", "vpass", "vpass@email.com", "Comp Sci", 3.0f);
        String pass = "pass";
        userDAO.create(student, pass);
        assertTrue (userDAO.validatePassword(student.getUsername(), "pass"));
        assertFalse (userDAO.validatePassword(student.getUsername(), "pas"));
        assertFalse (userDAO.validatePassword("notreal", "pas"));
    }

    @Test
    void testGetAllEnrolledStudents() {

    }

}
