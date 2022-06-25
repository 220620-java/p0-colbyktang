package com.revature.courseapp;
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

public class App {
    static boolean isLoggedIn = false;

    public static void main (String[] args) {
        System.out.println(
            "Welcome to Course Registration by Colby Tang!"
        );
        int input = 0;
        while (true) {
            if (!isLoggedIn) {
                input = LoginMenu();
            }
            else {

            }

            // Exit the application
            if (input <= -1) return;
        }
    }

    public static int LoginMenu () {
        System.out.println("Login Menu");
        System.out.println("1. Login User");
        System.out.println("2. Register As A Student");
        System.out.println("3. Exit");
        System.out.print(
            "Please choose an option: "
        );
        int input = 0;
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextInt();
        switch (input) {
            case 1:
                isLoggedIn = true;
                scanner.close();
                return 1;
            case 2:
                scanner.close();
                return 2;
            case 3:
                scanner.close();
                return -1;
        }
        scanner.close();
        return 3;
    }

    public static int StudentMenu () {
        System.out.println("Student Menu");
        System.out.println("1. View Available Classes");
        System.out.println("2. Enroll Class");
        System.out.println("3. Display Registered Classes");
        System.out.println("4. Cancel Class Enrollment");
        System.out.println("5. Logout");

        int input = 0;
        Scanner scanner = new Scanner(System.in);
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
        scanner.close();
        return 3;
    }

    public static int FacultyMenu () {
        System.out.println("Faculty Menu");
        System.out.println("1. Add New Classes");
        System.out.println("2. Change Class Details");
        System.out.println("3. Remove a Class");
        System.out.println("4. Logout");

        int input = 0;
        Scanner scanner = new Scanner(System.in);
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
        scanner.close();
        return 3;
    }
}
