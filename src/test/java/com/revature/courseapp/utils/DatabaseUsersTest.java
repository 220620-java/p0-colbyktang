package com.revature.courseapp.utils;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revature.courseapp.user.*;

public class DatabaseUsersTest {

    static DatabaseUsers db;

    @BeforeAll
    public static void OpenDatabase () {
        // Try to use AWS DB
        db = new DatabaseUsers ("aws_db.json");

        // If AWS does not work use local database
        if (db.getConnection() == null) db = new DatabaseUsers();
    }

    @AfterAll
    public static void afterAll () {
        if (db != null && db.getConnection() != null)
            db.closeConnection();
    }

    @Test
    public void testGetAllUsers() {
        List<User> allUsers = db.getAllUsers();
        System.out.println(allUsers);
        try {
            db.getConnection().close();
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
        assertTrue (db.doesUserExist(student));
        assertTrue (db.doesUserExist(student.getUsername()));
        Student noStudent = new Student (600, "Nobody", "Test", "ntest", "ntest@email.com");
        assertFalse (db.doesUserExist(noStudent));
        try {
            db.getConnection().close();
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
        db.truncateTable("users");
        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com");
        Student student2 = new Student ("John", "Test", "jotest", "jtest@email.com");
        Student student3 = new Student ("Jack", "Test", "jatest", "jtest@email.com");
        FacultyMember facultyMember = new FacultyMember("Fact", "Culty", "fculty", "fculty@email.com");
        try {
            byte[] salt = Encryption.generateSalt();
            String pass = Encryption.generateEncryptedPassword("pass", salt);
            db.insertUser(student, pass, salt);
            salt = Encryption.generateSalt();
            pass = Encryption.generateEncryptedPassword("pass", salt);
            db.insertUser(student2, pass, salt);
            salt = Encryption.generateSalt();
            pass = Encryption.generateEncryptedPassword("pass", salt);
            db.insertUser(student3, pass, salt);
            salt = Encryption.generateSalt();
            pass = Encryption.generateEncryptedPassword("pass", salt);
            db.insertUser(facultyMember, pass, salt);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValidatePassword () {
        Student student = new Student (202, "Vladimir", "Password", "vpass", "vpass@email.com");
        try {
            byte[] salt = Encryption.generateSalt();
            String pass = Encryption.generateEncryptedPassword("pass", salt);
            db.insertUser(student, pass, salt);
            assertTrue (db.validatePassword(student.getUsername(), "pass"));
            assertFalse (db.validatePassword(student.getUsername(), "pas"));
            assertFalse (db.validatePassword("notreal", "pas"));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllEnrolledStudents() {

    }
}
