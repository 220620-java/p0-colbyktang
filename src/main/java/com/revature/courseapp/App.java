package com.revature.courseapp;
import java.io.Console;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/*
 * Minimum features
 * Use of custom data structures (do not use java.util Collection types!)
 * Basic validation of user input (e.g. no registration for classes outside of registration window, no negative deposits/withdrawals, no overdrafting, etc.)
 * Unit tests for all business-logic classes
 * All exceptions are properly caught and handled
 * Proper use of OOP principles
 * Documentation (all classes and methods have basic documentation)
 * Database is 3rd Normal Form Compliant
 * Referential integrity (e.g. if a class is removed from the catalog, no students should be registered for it)
 * Logging messages and exceptions to a file using a custom logger
 * Generation of basic design documents (e.g. relational diagram, class diagram, flows, etc.)
 */

/*
 *  As any kind of user, I can:
    - login with my existing credentials

    As a faculty member I can:
    - add new classes to the registration catalog
    - change the registration details for a class
    - remove a class from the registration catalog (this should unregister all registered students)
    
    As a student, I can:
    - register a new account with the system (must be secured with a password)
    - view classes available for registration
    - register for an open and available class
    - cancel my registration for a class (if within window)
    - view the classes that I have registered for
 */

import com.revature.courseapp.utils.DatabaseCourses;
import com.revature.courseapp.utils.DatabaseUsers;
import com.revature.courseapp.utils.Encryption;
import com.revature.courseapp.utils.PostgreSQL;
import com.revature.courseapp.user.Student;
import com.revature.courseapp.user.User;

public class App {
    private static PostgreSQL db;
    private static boolean isLoggedIn = false;
    private static User loggedUser = null;
    private static Scanner scanner;

    /** 
     * @return PostgreSQL
     */
    public static PostgreSQL getDB () {
        return db;
    }
    
    /** 
     * @return boolean
     */
    public static boolean getIsLoggedIn () {
        return isLoggedIn;
    }

    
    /** 
     * @return Scanner
     */
    public static Scanner getScanner() {
        return scanner;
    }

    
    /** 
     * @param user
     */
    public static void setLoggedUser (User user) {
        loggedUser = user;
    }
    public static void main (String[] args) {
        db = new PostgreSQL("aws_db.json");
        scanner = new Scanner(System.in);
        System.out.println(
            "Welcome to Course Registration by Colby Tang!"
        );
        int input = 0;
        while (input != 3) {
            if (!isLoggedIn) {
                input = LoginMenu();
                switch (input) {
                    // Login
                    case 1:
                    isLoggedIn = userLogin();
                    break;
    
                    // Register
                    case 2:
                    RegisterStudent();
                    break;
    
                    // Exit
                    case 3:
                    return;
                }
            }
            else {
                System.out.println(String.format ("Logged in as %s", loggedUser.getUsername()));
                if (loggedUser.getUserType() == User.UserType.STUDENT) {
                    input = StudentMenu();
                }
                if (loggedUser.getUserType() == User.UserType.FACULTY) {
                    input = FacultyMenu();
                }
            }


        }
        System.out.println("EXITING APPLICATION!");
        scanner.close();
        getDB().closeConnection();
    }

    
    /** 
     * @return int
     */
    public static int LoginMenu () {
        System.out.println("Login Menu");
        System.out.println("1. Login User");
        System.out.println("2. Register As A Student");
        System.out.println("3. Exit");
        System.out.print(
            "Please choose an option: "
        );
        int input = 0;
        
        input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                isLoggedIn = true;
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
        }
        return 3;
    }

    
    /** 
     * @return int
     */
    public static int StudentMenu () {
        System.out.println("Student Menu");
        System.out.println("1. View Available Classes");
        System.out.println("2. Enroll Class");
        System.out.println("3. Display Registered Classes");
        System.out.println("4. Cancel Class Enrollment");
        System.out.println("5. Logout");

        int input = 0;
        input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return -1;
        }
        return 3;
    }

    
    /** 
     * @return int
     */
    public static int FacultyMenu () {
        System.out.println("Faculty Menu");
        System.out.println("1. Add New Classes");
        System.out.println("2. Change Class Details");
        System.out.println("3. Remove a Class");
        System.out.println("4. Logout");

        int input = 0;
        input = scanner.nextInt();
        switch (input) {
            case 1:
                scanner.close();
                return 1;
            case 2:
                scanner.close();
                return 2;
            case 3:
                scanner.close();
                return -1;
        }
        return 3;
    }

    public static boolean userLogin () {
        Scanner scanner = App.getScanner();
        String username = "";
        do {
            System.out.print ("Enter your username: ");
            username = scanner.nextLine();
        } while (username == "");

        Console console = System.console();

        String password;
        do {
            password = new String (console.readPassword("Enter your password: "));
        } while (password == "");

        // Check user from the database
        boolean isPasswordValid = DatabaseUsers.validatePassword(db.getConnection(), username, password);
        if (isPasswordValid) {
            App.setLoggedUser(DatabaseUsers.getUserFromDB(db.getConnection(), username));
        }
        else {
            System.out.println("Password is not correct!");
        }
        return isPasswordValid;
    }

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
            DatabaseUsers.insertUser(db.getConnection(), student, pass, salt);
            System.out.println(String.format ("Created student %s %s. ID: %d", firstName, lastName, student.getId()));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
