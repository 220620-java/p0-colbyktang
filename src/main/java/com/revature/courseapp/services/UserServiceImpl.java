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
        System.out.println();
        System.out.println("Viewing Available Classes...");
        List<Course> courses = courseDAO.findAllAvailable();
        System.out.println();
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
                courseDAO.getSize(course.getId()), 
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
        System.out.println();
        System.out.println("Viewing Registered Classes...");
        System.out.println();
        List<Course> courses = courseDAO.getAllEnrolledCourses(CourseAppDriver.getLoggedUser().getId());
        System.out.println();
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
                courseDAO.getSize(course.getId()), 
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
        System.out.println();
        System.out.println("Displaying all classes...");
        for (int i = 0; i < allCourses.size(); i++) {
            System.out.println(allCourses.get(i));
        }
    }

    @Override
    public void facultyAddNewClass () {
        Scanner scanner = CourseAppDriver.getScanner();
        System.out.println("Adding new class...");
        String input;

        boolean isCourseNameValid = false;
        String course_name = "";
        do {
            System.out.print("Enter a course name: ");
            input = scanner.nextLine();
            course_name = input;
            isCourseNameValid = Validation.isCourseNameValid(course_name);

            if (!isCourseNameValid) {
                System.out.println("Enter a valid course name!");
            }
        } while (!isCourseNameValid);

        boolean isSemesterValid = false;
        String semester = "";
        do {
            System.out.print("Enter a semester  followed by year (FALL, SPRING, SUMMER 2022): ");
            input = scanner.nextLine();
            semester = input;
            isSemesterValid = Validation.isSemesterValid(semester);
            if (!isSemesterValid) {
                System.out.println("Semester is not valid! Choose a season followed by year!");
            }
        } while (!isSemesterValid);

        boolean isCapacityValid = false;
        int capacity = 0;
        do {
            System.out.print("Enter a capacity: ");
            input = scanner.nextLine();
            isCapacityValid = Validation.isCapacityValid(input);
            if (!isCapacityValid) {
                System.out.println("Capacity is not VALID!");
            }
            else {
                capacity = Integer.parseInt(input);
            }
        } while (!isCapacityValid);

        boolean isValid = false;
        boolean isAvailable = false;
        do {
            System.out.print("Is course available (Y/N): ");
            input = scanner.nextLine();
            isValid = Validation.isAvailableValid(input);
            if (!isValid) { System.out.println("Input not valid!");}
            else { 
                if (input.toUpperCase().equals("Y")) isAvailable = true;
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
        
        boolean isIdValid = false;
        int course_id = 0;
        do {
            System.out.print("Select a class to change (course id): ");
            String input = scanner.nextLine();
            isIdValid = Validation.isIdValid(input);
            course_id = Integer.parseInt(input);
            if (!isIdValid) {
                System.out.println("ID IS NOT VALID! (Integer)");
            }
        } while (!isIdValid);

        Course course = courseDAO.findById(course_id);

        boolean isCourseNameValid = false;
        String course_name = "";
        do {
            System.out.print("Enter a course name: ");
            String input = scanner.nextLine();
            course_name = input;
            isCourseNameValid = Validation.isCourseNameValid(course_name);

            if (!isCourseNameValid) {
                System.out.println("Enter a valid course name!");
            }
            else {
                course.setCourseName(course_name);
            }
        } while (!isCourseNameValid);


        boolean isSemesterValid = false;
        String semester = "";
        do {
            System.out.print("Enter a semester  followed by year (FALL, SPRING, SUMMER 2022): ");
            String input = scanner.nextLine();
            semester = input;
            isSemesterValid = Validation.isSemesterValid(semester);
            if (!isSemesterValid) {
                System.out.println("Semester is not valid! Choose a season followed by year!");
            }
            else {
                course.setSemester(semester);
            }
        } while (!isSemesterValid);

        boolean isCapacityValid = false;
        int capacity = 0;
        do {
            System.out.print("Enter a capacity: ");
            String input = scanner.nextLine();
            if (!isCapacityValid) {
                System.out.println("Capacity is not VALID!");
            }
            else {
                capacity = Integer.parseInt(input);
                course.setCapacity(capacity);
            }
        } while (!isCapacityValid);

        boolean isAvailableValid = false;
        boolean availability = false;
        do {
            System.out.print("Is course available? (Y/N): ");
            String input = scanner.nextLine();
            isAvailableValid = Validation.isAvailableValid(input);
            if (input.toUpperCase().equals("Y")) {availability = true;}
            course.setIsAvailable(availability);
        } while (!isAvailableValid);

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
        boolean isFirstNameValid = false;
        do {
            System.out.print ("Enter your first name: ");
            firstName = scanner.nextLine();
            isFirstNameValid = Validation.isNameValid(firstName);
            if (!isFirstNameValid) {
                System.out.println("First name is not valid!");
            }
        } while (!isFirstNameValid);

        String lastName = "";
        boolean isLastNameValid = false;
        do {
            System.out.print ("Enter your last name: ");
            lastName = scanner.nextLine();
            isLastNameValid = Validation.isNameValid(lastName);
            if (!isLastNameValid) {
                System.out.println("Last name is not valid!");
            }
        } while (!isLastNameValid);
        
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
        Scanner scanner = CourseAppDriver.getScanner();

        String firstName = "";
        boolean isFirstNameValid = false;
        do {
            System.out.print ("Enter your first name: ");
            firstName = scanner.nextLine();
            isFirstNameValid = Validation.isNameValid(firstName);
            if (!isFirstNameValid) {
                System.out.println("First name is not valid!");
            }
        } while (!isFirstNameValid);

        String lastName = "";
        boolean isLastNameValid = false;
        do {
            System.out.print ("Enter your last name: ");
            lastName = scanner.nextLine();
            isLastNameValid = Validation.isNameValid(lastName);
            if (!isLastNameValid) {
                System.out.println("Last name is not valid!");
            }
        } while (!isLastNameValid);

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

        Student newStudent = new Student (student.getId(), firstName, lastName, student.getUsername(), email, major, student.getGpa());

        return newStudent;
    }
}