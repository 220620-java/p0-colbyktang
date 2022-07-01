package com.revature.courseapp.data;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revature.courseapp.models.FacultyMember;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;
import com.revature.courseapp.utils.Encryption;
import com.revature.courseapp.utils.List;

public class DatabaseUsersTest {

    static ConnectionUtil db;
    static UserDAO userDAO;

    @BeforeAll
    public static void OpenDatabase () {
        // Try to use AWS DB
        db = ConnectionUtil.getConnectionUtil();
    }

    @AfterAll
    public static void afterAll () {
        if (db != null && db.getCurrentConnection() != null)
            db.closeConnection();
    }

    @Test
    public void testGetAllUsers() {
        List<User> allUsers = userDAO.findAll();
        System.out.println(allUsers);
        try {
            db.getCurrentConnection().close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoesUserExist () {
        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com");
        assertTrue (userDAO.doesUserExist(student.getUsername()));
        Student noStudent = new Student (600, "Nobody", "Test", "ntest", "ntest@email.com");
        assertFalse (userDAO.doesUserExist(noStudent.getUsername()));
        try {
            db.getCurrentConnection().close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertUser() {
        DatabaseUtils.truncateTable("users");
        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com");
        Student student2 = new Student ("John", "Test", "jotest", "jtest@email.com");
        Student student3 = new Student ("Jack", "Test", "jatest", "jtest@email.com");
        FacultyMember facultyMember = new FacultyMember("Fact", "Culty", "fculty", "fculty@email.com");
        byte[] salt = Encryption.generateSalt();
        String pass = Encryption.generateEncryptedPassword("pass", salt);
        userDAO.create(student, pass, salt);
        salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword("pass", salt);
        userDAO.create(student2, pass, salt);
        salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword("pass", salt);
        userDAO.create(student3, pass, salt);
        salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword("pass", salt);
        userDAO.create(facultyMember, pass, salt);
    }

    @Test
    public void testValidatePassword () {
        Student student = new Student (202, "Vladimir", "Password", "vpass", "vpass@email.com");
        byte[] salt = Encryption.generateSalt();
        String pass = Encryption.generateEncryptedPassword("pass", salt);
        userDAO.create(student, pass, salt);
        assertTrue (userDAO.validatePassword(student.getUsername(), "pass"));
        assertFalse (userDAO.validatePassword(student.getUsername(), "pas"));
        assertFalse (userDAO.validatePassword("notreal", "pas"));
    }

    @Test
    void testGetAllEnrolledStudents() {

    }

}
