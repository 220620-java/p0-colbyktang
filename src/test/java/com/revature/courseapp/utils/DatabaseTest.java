package com.revature.courseapp.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.junit.Test;
import com.revature.courseapp.user.*;

public class DatabaseTest {
    @Test
    public void testGetAllUsers() {
        PostgreSQL db = new PostgreSQL();
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
        PostgreSQL db = new PostgreSQL();
        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com");
        assertTrue (db.DoesUserExist(student));
        assertTrue (db.DoesUserExist(student.getUsername()));
        Student noStudent = new Student (600, "Nobody", "Test", "ntest", "ntest@email.com");
        assertFalse (db.DoesUserExist(noStudent));
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
        PostgreSQL db = new PostgreSQL();
        db.truncateTable("users");
        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com");
        Student student2 = new Student ("John", "Test", "jotest", "jtest@email.com");
        Student student3 = new Student ("Jack", "Test", "jatest", "jtest@email.com");
        FacultyMember facultyMember = new FacultyMember("Fact", "Culty", "fculty", "fculty@email.com");
        try {
            byte[] salt = Encryption.generateSalt();
            String pass = Encryption.generateEncryptedPassword("pass", salt);
            db.InsertUser(student, pass, salt);
            salt = Encryption.generateSalt();
            pass = Encryption.generateEncryptedPassword("pass", salt);
            db.InsertUser(student2, pass, salt);
            salt = Encryption.generateSalt();
            pass = Encryption.generateEncryptedPassword("pass", salt);
            db.InsertUser(student3, pass, salt);
            salt = Encryption.generateSalt();
            pass = Encryption.generateEncryptedPassword("pass", salt);
            db.InsertUser(facultyMember, pass, salt);
            try {
                db.getConnection().close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValidatePassword () {
        PostgreSQL db = new PostgreSQL();
        Student student = new Student ("Joe", "Test", "jtest", "jtest@email.com");
        assertTrue (db.validatePassword(student.getUsername(), "pass"));
        assertFalse (db.validatePassword(student.getUsername(), "pas"));
        assertFalse (db.validatePassword("notreal", "pas"));
    }
}
