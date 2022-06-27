package com.revature.courseapp;

import java.io.Console;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

import com.revature.courseapp.utils.Encryption;
import com.revature.courseapp.user.User;
import com.revature.courseapp.user.Student;

public class Register {
    public static void RegisterStudent () {
        Scanner scanner = App.getScanner();
        System.out.println(
            "Registering as a new student..."
        );

        String firstName = "";
        do {
            
            System.out.print ("Enter your first name: ");
            firstName = scanner.nextLine();
        } while (firstName == "");

        String lastName = "";
        do {
            System.out.print ("Enter your last name: ");
            lastName = scanner.nextLine();
        } while (lastName == "");
        
        String username = "";
        do {
            System.out.print ("Enter your username: ");
            username = scanner.nextLine();
        } while (username == "");

        String email = "";
        do {
            System.out.print ("Enter your email: ");
            email = scanner.nextLine();
        } while (email == "");

        Console console = System.console();

        String password;
        String verifyPassword;
        do {
            password = new String (console.readPassword("Enter your password: "));
            verifyPassword = new String (console.readPassword("Enter your password again: "));

            if (!password.equals(verifyPassword)) {
                System.out.println("Passwords do not match");
            }
        } while (!password.equals(verifyPassword) && password != "");

        System.out.println("Passwords match!");
        Student student = new Student(firstName, lastName, username, email);
        System.out.println(student);

        // Add student to the database
        
        try {
            byte[] salt = Encryption.generateSalt();
            String pass = Encryption.generateEncryptedPassword(password, salt);
            App.getUserDB().insertUser(student, pass, salt);
            System.out.println(String.format ("Created student %s %s. ID: %d", firstName, lastName, student.getId()));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
