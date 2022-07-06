package com.revature.courseapp;
import java.io.Console;
import java.util.Scanner;

import com.revature.courseapp.utils.Validation;
import com.revature.courseapp.data.ConnectionUtil;
import com.revature.courseapp.exceptions.UserAlreadyExistsException;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;
import com.revature.courseapp.services.UserService;
import com.revature.courseapp.services.UserServiceImpl;

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

/** CourseApp Console Application is an application that lets students view, enroll, and withdraw from courses
 *  as well as letting faculty manage courses. This application encrypts passwords in SHA-512.
 * @author Colby Tang
 * @version 1.0
 */
public class CourseAppDriver {
    private static ConnectionUtil db;
    private static UserService userService = new UserServiceImpl();
    private static boolean isLoggedIn = false;
    private static User loggedUser = null;
    private static Scanner scanner = new Scanner(System.in);

    /** 
     * @return PostgreSQL
     */
    public static ConnectionUtil getDB () {
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

    
    /** Set the logged in user after logging in.
     * @param user
     */
    public static void setLoggedUser (User user) {
        loggedUser = user;
    }

    
    /** 
     * @return User
     */
    public static User getLoggedUser () {
        return loggedUser;
    }

    
    /** 
     * @param args
     */
    public static void main (String[] args) {
        System.out.println(
            "Welcome to Course Registration by Colby Tang!"
        );
        String input = "0";
        while (input != "-1") {
            if (loggedUser == null) {
                input = loginMenu();
            }
            else {
                if (loggedUser.getUserType() == User.UserType.STUDENT) {
                    input = studentMenu();
                }
                else if (loggedUser.getUserType() == User.UserType.FACULTY) {
                    input = facultyMenu();
                }
            }
        }
        System.out.println("EXITING APPLICATION!");
        scanner.close();
    }
    
    /** 
     * @return int
     */
    public static String loginMenu () {
        String input = "0";
        
        while (input != "3") {
            System.out.println("Login Menu");
            System.out.println("1. Login User");
            System.out.println("2. Register As A Student");
            System.out.println("3. Exit");
            boolean isValid = false;
            do {
                System.out.print(
                    "Please choose an option: "
                );
                input = scanner.nextLine();
                isValid = Validation.isMenuSelectionValid(input);
                if (!isValid) { System.out.println("Invalid option!");}
            } while (!isValid);
            switch (input) {
                case "1":
                    isLoggedIn = userLogin();
                    if (isLoggedIn) {
                        System.out.println(String.format ("Logged in as %s", loggedUser.getUsername()));
                        return "3";
                    }
                    else {
                        System.out.println(String.format ("Could not login as %s", loggedUser.getUsername()));
                    }
                case "2":
                    try {
                        loggedUser = userService.registerStudent();
                        isLoggedIn = loggedUser != null;
                        if (isLoggedIn) {
                            System.out.println(String.format ("Logged in as %s", loggedUser.getUsername()));
                            return "3";
                        }
                        else {
                            System.out.println(String.format ("Could not login as %s", loggedUser.getUsername()));
                        }
                    }
                    catch (UserAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                case "3":
                    return "-1";
            }
        }
        return "-1";
    }

    /**
     * @return boolean
     */
    public static boolean userLogin () {
        Scanner scanner = CourseAppDriver.getScanner();
        String username = "";
        
        boolean isInputValid = false;
        do {
            System.out.print ("Enter your username: ");
            username = scanner.nextLine();
            isInputValid = Validation.isUsernameValid(username);
        } while (username == "" || !isInputValid);

        Console console = System.console();

        String password;
        do {
            password = new String (console.readPassword("Enter your password: "));
        } while (password == "");

        loggedUser = userService.userLogin(username, password);

        return loggedUser != null;
    }
    
    /** 
     * @return int
     */
    public static String studentMenu () {
        String input = "0";
        while (isLoggedIn) {
            System.out.println("Student Menu");
            System.out.println("1. View Available Classes");
            System.out.println("2. Enroll Class");
            System.out.println("3. Display Registered Classes");
            System.out.println("4. Cancel Class Enrollment");
            System.out.println("5. Profile Menu");
            System.out.println("6. Logout");
            boolean isValid = false;
            do {
                System.out.print(
                    "Please choose an option: "
                );
                input = scanner.nextLine();
                isValid = Validation.isMenuSelectionValid(input);
                if (!isValid) { System.out.println("Invalid option!");}
            } while (!isValid);
            switch (input) {
                case "1":
                    userService.studentViewAvailableClasses();
                    break;
                case "2":
                    userService.studentEnrollClass ();
                    break;
                case "3":
                    userService.studentViewRegisteredClasses();
                    break;
                case "4":
                    userService.studentCancelClass();
                    break;
                case "5":
                    studentProfileMenu();
                    break;
                case "6":
                    isLoggedIn = false;
                    System.out.println("Logging out of user!");
                    loggedUser = null;
                    break;
            }
        }
        return "0";
    }

    public static String studentProfileMenu() {
        String input = "0";
        printDividerLine();
        while (input != "3") {
            System.out.println("Student Profile Menu");
            System.out.println("1. View Profile");
            System.out.println("2. Change Profile");
            System.out.println("3. Go back to Student Menu");
            boolean isValid = false;
            do {
                System.out.print(
                    "Please choose an option: "
                );
                input = scanner.nextLine();
                isValid = Validation.isMenuSelectionValid(input);
                if (!isValid) { System.out.println("Invalid option!");}
            } while (!isValid);
            switch (input) {
                case "1":
                    userService.studentViewProfile((Student)loggedUser);
                    break;
                case "2":
                    loggedUser = userService.studentChangeProfile((Student)loggedUser);
                    break;
                case "3":
                    System.out.println("Returning to Student Menu...");
                    break;
            }
            printDividerLine();
        }
        return "0";
    }
    
    /** 
     * @return int
     */
    public static String facultyMenu () {
        String input = "0";
        while (isLoggedIn) {
            System.out.println("Faculty Menu");
            System.out.println("1. View All Classes");
            System.out.println("2. Add New Class");
            System.out.println("3. Change Class Details");
            System.out.println("4. Remove a Class");
            System.out.println("5. Logout");
            boolean isValid = false;
            do {
                System.out.print(
                    "Please choose an option: "
                );
                input = scanner.nextLine();
                isValid = Validation.isMenuSelectionValid(input);
                if (!isValid) { System.out.println("Invalid option!");}
            } while (!isValid);
            switch (input) {
                case "1":
                    userService.facultyViewClasses();
                    break;
                case "2":
                    userService.facultyAddNewClass();
                    break;
                case "3":
                    userService.facultyChangeClassDetails ();
                    break;
                case "4":
                    userService.facultyRemoveClass ();
                    break;
                case "5":
                    isLoggedIn = false;
                    System.out.println("Logging out of user!");
                    loggedUser = null;
                    break;
            }
        }
        return "0";
    }

    public static void printDividerLine () {
        System.out.println("---------------------------");
    }
}
