package com.revature.courseapp.services;

import java.io.Console;
import java.util.Scanner;

import com.revature.courseapp.CourseAppDriver;
import com.revature.courseapp.data.CourseDAO;
import com.revature.courseapp.data.CoursePostgres;
import com.revature.courseapp.data.UserDAO;
import com.revature.courseapp.data.UserPostgres;
import com.revature.courseapp.exceptions.UserAlreadyExistsException;
import com.revature.courseapp.models.Course;
import com.revature.courseapp.models.User;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.utils.List;
import com.revature.courseapp.utils.Logger;
import com.revature.courseapp.utils.Validation;


public class UserServiceImpl implements UserService {
    private UserDAO userDAO = new UserPostgres();
    private CourseDAO courseDAO = new CoursePostgres();

    /** Logins in the user by first checking the password
     * @param username
     * @param password
     * @return boolean
     */
    @Override
    public User userLogin (String username, String password) {
        // Check user from the database
        boolean isPasswordValid = userDAO.validatePassword(username, password);
        if (isPasswordValid) {
            return userDAO.findByUsername(username);
        }
        System.out.println("Password is not correct!");
        return null;
    }

    /**
     * Views available classes a student can register for.
     */
    @Override
    public void studentViewAvailableClasses () {
        System.out.println("Viewing Available Classes...");
        List<Course> courses = courseDAO.findAllAvailable();
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

    @Override
    public void studentEnrollClass () {
        System.out.print("Choose a class to enroll (Course ID): ");
        Scanner scanner = CourseAppDriver.getScanner();
        String input = scanner.nextLine();
        try {
            System.out.println("Enrolling in Course " + input + ".");
            courseDAO.enrollCourse(Integer.parseInt(input), CourseAppDriver.getLoggedUser().getId());
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            Logger.logMessage(e.getStackTrace());
        }
    }

    @Override
    public void studentViewRegisteredClasses () {
        System.out.println("Viewing Registered Classes...");
        List<Course> courses = courseDAO.getAllEnrolledCourses(CourseAppDriver.getLoggedUser().getId());
        if (courses.size() == 0) {
            System.out.println("NO REGISTERED CLASSES!");
            return;
        }
        printDividerLine ();
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
        printDividerLine ();
        return;
    }

    @Override
    public void studentCancelClass () {
        System.out.print("Choose a class to CANCEL (Course ID): ");
        Scanner scanner = CourseAppDriver.getScanner();
        String input = scanner.nextLine();
        try {
            System.out.println("Cancelling enrollment in + Course " + input + ".");
            boolean isWithdrawn = courseDAO.withdrawFromCourse(Integer.parseInt(input), CourseAppDriver.getLoggedUser().getId());
            if (isWithdrawn) System.out.println("Withdrawn from Course " + input + ".");
            else System.out.println("Could not withdraw from Course " + input + ".");
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            Logger.logMessage(e.getStackTrace());
        }
    }

    @Override
    public void facultyViewClasses() {
        List<Course> allCourses = courseDAO.findAll();
        System.out.println("Displaying all classes...");
        for (int i = 0; i < allCourses.size(); i++) {

            System.out.println(allCourses.get(i));
        }
    }

    @Override
    public void facultyAddNewClass () {
        Scanner scanner = CourseAppDriver.getScanner();
        System.out.println("Adding new class...");

        System.out.print("Enter a course name: ");
        String input = scanner.nextLine();
        String course_name = input;

        System.out.print("Enter a semester  followed by year (FALL, SPRING, SUMMER 2022): ");
        input = scanner.nextLine();
        String semester = input;

        System.out.print("Enter a capacity: ");
        input = scanner.nextLine();
        int capacity = Integer.parseInt(input);

        boolean isValid = false;
        boolean isAvailable = false;
        do {
            System.out.print("Is course available (Y/N): ");
            input = scanner.nextLine();
            isValid = Validation.isAvailableValid(input);
            if (!isValid) { System.out.println("Input not valid!");}
            else { 
                if (input.toUpperCase() == "Y") isAvailable = true;
                else { isAvailable = false;}
            }
        } while (!isValid);

        Course course = new Course (course_name, semester, capacity, isAvailable);
        System.out.println("Adding new course " + course_name + "...");
        courseDAO.create(course);
    }

    @Override
    public void facultyChangeClassDetails () {
        Scanner scanner = CourseAppDriver.getScanner();
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

        System.out.print("Is course available?: ");
        input = scanner.nextLine();
        boolean availability = true;

        Course course = courseDAO.findById(course_id);

        course.setCourseName(course_name);
        course.setSemester(semester);
        course.setCapacity(capacity);
        course.setIsAvailable(availability);
        courseDAO.update(course);
    }

    @Override
    public void facultyRemoveClass () {
        Scanner scanner = CourseAppDriver.getScanner();
        System.out.print("Which class is being removed? (course id): ");
        String input = scanner.nextLine();
        int course_id = Integer.parseInt(input);
        courseDAO.delete(course_id);
    }

    public static void printDividerLine () {
        System.out.println("---------------------------");
    }
    
    /** Generates a salt and an encrypted password to register the student.
     */
    @Override
    public Student registerStudent() throws UserAlreadyExistsException {
        Scanner scanner = CourseAppDriver.getScanner();
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
        boolean isUsernameValid = false;
        do {
            System.out.println("Username (5-30 characters, start with a letter)");
            System.out.print ("Enter your username: ");
            username = scanner.nextLine();
            isUsernameValid = Validation.isUsernameValid(username);
            if (!isUsernameValid) {
                System.out.println("Username is not valid!");
            }
        } while (!isUsernameValid);

        String email = "";
        boolean isEmailValid = false;
        do {
            System.out.print ("Enter your email: ");
            email = scanner.nextLine();
            isEmailValid = Validation.isEmailValid(email);
            if (!isEmailValid) {
                System.out.println("Email is not in valid form: xxx@xxx.com");
            }
        } while (!isEmailValid);

        String major = "";
        boolean isMajorValid = false;
        do {
            System.out.print ("Enter your major: ");
            major = scanner.nextLine();
            isMajorValid = Validation.isMajorValid(major);
            if (!isMajorValid) {
                System.out.println("Major is not long enough or does not start with a letter!");
            }
        } while (!isMajorValid);

        Console console = System.console();
        String password;
        String verifyPassword;
        boolean isPasswordValid = false;
        do {
            do {
                System.out.println("Password (4-32 characters)");
                password = new String (console.readPassword("Enter your password: "));
                isPasswordValid = Validation.isPasswordValid(password);
                if (!isPasswordValid) {
                    System.out.println("Password is not valid!");
                }
            } while (!isPasswordValid);

            verifyPassword = new String (console.readPassword("Enter your password again: "));

            if (!password.equals(verifyPassword)) {
                System.out.println("Passwords do not match");
            }
        } while (!password.equals(verifyPassword));

        System.out.println("Passwords match!");
        
        // Add student to the database
        Student student = new Student(firstName, lastName, username, email, major, 3.0f);
        try {
            // Add student to the database
            Student returnedStudent = userDAO.create(student, password);
            if (returnedStudent == null) {
                throw new UserAlreadyExistsException();
            }
            System.out.println(String.format ("Created student %s %s. ID: %d", student.getFirstName(), student.getLastName(), student.getId()));
            return returnedStudent;
        }
        catch (UserAlreadyExistsException e) {
            e.printStackTrace();
            Logger.logMessage(e.getStackTrace());
        }
        catch (Exception e) {
            e.printStackTrace();
            Logger.logMessage(e.getStackTrace());
        }
        return null;
    }

    public void studentViewProfile (Student student) {
        printDividerLine();
        System.out.println("Viewing student profile...");
        String nameString = String.format ("Name: %s, %s", student.getLastName(), student.getFirstName());
        System.out.println(nameString);
        String majorString = String.format ("Major: %s", student.getMajor());
        System.out.println(majorString);
        String emailString = String.format ("Email: %s", student.getEmail());
        System.out.println(emailString);
        String gpaString = String.format ("GPA: %f", student.getGpa());
        System.out.println(gpaString);
        
    }

    public Student studentChangeProfile (Student student) {
        Student newStudent = student;
        
        return newStudent;
    }
}