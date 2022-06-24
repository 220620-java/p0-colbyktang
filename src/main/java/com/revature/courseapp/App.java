package com.revature.courseapp;
import java.util.Scanner;
import com.revature.courseapp.utils.PostgreSQL;

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
    public static void main (String[] args) {
        System.out.println(
            "Welcome to Course Registration by Colby Tang!"
        );
        boolean isLoggedIn = false;
        int input = 0;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!isLoggedIn) {
                LoginMenu();
                System.out.print(
                    "Please choose an option: "
                );
                input = scanner.nextInt();
                switch (input) {
                    case 1:
                        isLoggedIn = true;
                        break;
                    case 2:
                        break;
                    case 3:
                        scanner.close();
                        return;
                }
            }
            else {

            }
        }
    }

    public static void LoginMenu () {
        System.out.println("Login Menu");
        System.out.println("1. Login");
        System.out.println("2. Register As A Student");
        System.out.println("3. Exit");
    }

    public static void StudentMenu () {
        System.out.println("Student Menu");
        System.out.println("1. View Available Classes");
        System.out.println("2. Enroll Class");
        System.out.println("3. Display Registered Classes");
        System.out.println("4. Cancel Class Enrollment");
        System.out.println("5. Logout");
    }

    public static void FacultyMenu () {
        System.out.println("Faculty Menu");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }
}
