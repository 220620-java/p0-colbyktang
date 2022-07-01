package com.revature.courseapp;
import java.io.Console;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.w3c.dom.UserDataHandler;

import com.revature.courseapp.utils.Encryption;
import com.revature.courseapp.utils.List;
import com.revature.courseapp.data.CoursePostgres;
import com.revature.courseapp.data.UserDAO;
import com.revature.courseapp.data.UserPostgres;
import com.revature.courseapp.data.ConnectionUtil;
import com.revature.courseapp.data.CourseDAO;
import com.revature.courseapp.models.Course;
import com.revature.courseapp.models.FacultyMember;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;

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
 *  as well as letting faculty manage courses. This application encrypts passwords in SHA-512 and uses JWT to
 *  temporarily keep your login session.
 * @author Colby Tang
 * @version 1.0
 */
public class App {
    private static ConnectionUtil db;
    private static UserDAO userDAO;
    private static CourseDAO courseDAO;
    private static boolean isLoggedIn = false;
    private static User loggedUser = null;
    private static Scanner scanner;

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

    public static void main (String[] args) {
        userDAO = new UserPostgres();
        courseDAO = new CoursePostgres();
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
            case 4:
                FacultyMember user = new FacultyMember(201, "Colby", "Tang", "ctang2", "ctang2@email.com");
                byte[] salt = Encryption.generateSalt();
                String pass = Encryption.generateEncryptedPassword("pass", salt);
                userDAO.create(user, pass, salt);
        }
        return -1;
    }

    /**
     * @return boolean
     */
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
        boolean isPasswordValid = userDAO.validatePassword(username, password);
        if (isPasswordValid) {
            App.setLoggedUser(userDAO.findByUsername(username));
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
        byte[] salt = Encryption.generateSalt();
        String pass = Encryption.generateEncryptedPassword(password, salt);
        userDAO.create(student, pass, salt);
        System.out.println(String.format ("Created student %s %s. ID: %d", firstName, lastName, student.getId()));
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
                studentViewAvailableClasses();
                return 1;
            case 2:
                studentEnrollClass ();
                return 2;
            case 3:
                studentViewRegisteredClasses();
                return 3;
            case 4:
                studentCancelClass();
                return 4;
            case 5:
                isLoggedIn = false;
                return -1;
        }
        return -1;
    }

    public static void studentViewAvailableClasses () {
        System.out.println("Viewing Available Classes...");
        List<Course> courses = courseDAO.findAll();
        if (courses.size() == 0) {
            System.out.println("NO AVAILABLE CLASSES!");
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            String printString = String.format(
                "%d: %s [%d/%d]", 
                course.getId(), 
                course.getCourseName(), 
                course.getNumberOfStudents(), 
                course.getCapacity()
                );
            System.out.println(printString);
        }
        return;
    }

    public static void studentEnrollClass () {
        System.out.print("Choose a class to enroll (Course ID): ");
        Scanner scanner = App.getScanner();
        String input = scanner.nextLine();
        scanner.close();
        try {
            System.out.println("Enrolling in + Course " + input + ".");
            courseDAO.enrollCourse(Integer.parseInt(input), loggedUser.getId());
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void studentViewRegisteredClasses () {
        System.out.println("Viewing Registered Classes...");
        List<Course> courses = courseDAO.getAllEnrolledCourses(loggedUser.getId());
        if (courses.size() == 0) {
            System.out.println("NO REGISTERED CLASSES!");
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            String printString = String.format(
                "%d: %s [%d/%d]", 
                course.getId(), 
                course.getCourseName(), 
                course.getNumberOfStudents(), 
                course.getCapacity()
                );
            System.out.println(printString);
        }
        return;
    }

    public static void studentCancelClass () {
        System.out.print("Choose a class to CANCEL (Course ID): ");
        Scanner scanner = App.getScanner();
        String input = scanner.nextLine();
        scanner.close();
        try {
            System.out.println("Cancelling enrollment in + Course " + input + ".");
            boolean isWithdrawn = courseDAO.withdrawFromCourse(Integer.parseInt(input), loggedUser.getId());
            if (isWithdrawn) System.out.println("Withdrawn from Course " + input + ".");
            else System.out.println("Could not withdraw from Course " + input + ".");
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
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
                facultyAddNewClass();
                return 1;
            case 2:
                facultyChangeClassDetails ();
                return 2;
            case 3:
            facultyRemoveClass ();
                return 3;
            case 4:
                isLoggedIn = false;
                return -1;
        }
        return -1;
    }

    public static void facultyAddNewClass () {
        Scanner scanner = App.getScanner();
        System.out.println("Adding new class...");
        System.out.print("Enter a course id: ");
        String input = scanner.nextLine();
        int course_id = Integer.parseInt(input);

        System.out.print("Enter a course name: ");
        input = scanner.nextLine();
        String course_name = input;

        System.out.print("Enter a semester  followed by year (FALL, SPRING, SUMMER 2022): ");
        input = scanner.nextLine();
        String semester = input;

        System.out.print("Enter a capacity: ");
        input = scanner.nextLine();
        int capacity = Integer.parseInt(input);

        Course course = new Course (course_id, course_name, semester, capacity);
        System.out.println("Adding new course " + course_id + "...");
        courseDAO.create(course);
        scanner.close();
    }

    public static void facultyChangeClassDetails () {
        Scanner scanner = App.getScanner();
        System.out.print("Select a class to change (course id): ");
        String input = scanner.nextLine();
        int course_id = Integer.parseInt(input);

        System.out.print("Enter a course name: ");
        input = scanner.nextLine();
        String course_name = input;

        System.out.print("Enter a semester  followed by year (FALL, SPRING, SUMMER 2022): ");
        input = scanner.nextLine();
        String semester = input;

        System.out.print("Enter a capacity: ");
        input = scanner.nextLine();
        int capacity = Integer.parseInt(input);
        courseDAO.update(courseDAO.findById(course_id));
        scanner.close();
    }

    public static void facultyRemoveClass () {
        Scanner scanner = App.getScanner();
        System.out.print("Which class is being removed? (course id): ");
        String input = scanner.nextLine();
        int course_id = Integer.parseInt(input);
        courseDAO.delete(course_id);

        scanner.close();
    }
}
