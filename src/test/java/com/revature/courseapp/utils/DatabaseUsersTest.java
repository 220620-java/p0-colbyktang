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

    static PostgreSQL db;

    @BeforeAll
    public static void OpenDatabase () {
        // Try to use AWS DB
        db = new PostgreSQL ("aws_db.json");

        // If AWS does not work use local database
        if (db.getConnection() == null) db = new PostgreSQL();
    }

    @AfterAll
    public static void afterAll () {
        if (db != null && db.getConnection() != null)
            db.closeConnection();
    }

    @Test
    public void testGetAllUsers() {
        List<User> allUsers = DatabaseUsers.getAllUsers(db.getConnection());
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
        assertTrue (DatabaseUsers.doesUserExist(db.getConnection(), student.getUsername()));
        Student noStudent = new Student (600, "Nobody", "Test", "ntest", "ntest@email.com");
        assertFalse (DatabaseUsers.doesUserExist(db.getConnection(), noStudent.getUsername()));
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
        DatabaseUtils.truncateTable(db.getConnection(), "users");
        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com");
        Student student2 = new Student ("John", "Test", "jotest", "jtest@email.com");
        Student student3 = new Student ("Jack", "Test", "jatest", "jtest@email.com");
        FacultyMember facultyMember = new FacultyMember("Fact", "Culty", "fculty", "fculty@email.com");
        byte[] salt = Encryption.generateSalt();
        String pass = Encryption.generateEncryptedPassword("pass", salt);
        DatabaseUsers.insertUser(db.getConnection(), student, pass, salt);
        salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword("pass", salt);
        DatabaseUsers.insertUser(db.getConnection(), student2, pass, salt);
        salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword("pass", salt);
        DatabaseUsers.insertUser(db.getConnection(), student3, pass, salt);
        salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword("pass", salt);
        DatabaseUsers.insertUser(db.getConnection(), facultyMember, pass, salt);
    }

    @Test
    public void testValidatePassword () {
        Student student = new Student (202, "Vladimir", "Password", "vpass", "vpass@email.com");
        byte[] salt = Encryption.generateSalt();
        String pass = Encryption.generateEncryptedPassword("pass", salt);
        DatabaseUsers.insertUser(db.getConnection(), student, pass, salt);
        assertTrue (DatabaseUsers.validatePassword(db.getConnection(), student.getUsername(), "pass"));
        assertFalse (DatabaseUsers.validatePassword(db.getConnection(), student.getUsername(), "pas"));
        assertFalse (DatabaseUsers.validatePassword(db.getConnection(), "notreal", "pas"));
    }

    @Test
    void testGetAllEnrolledStudents() {

    }

}
