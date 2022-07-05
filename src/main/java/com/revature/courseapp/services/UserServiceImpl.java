package com.revature.courseapp.services;

import java.util.Scanner;

import com.revature.courseapp.CourseAppDriver;
import com.revature.courseapp.data.CourseDAO;
import com.revature.courseapp.data.CoursePostgres;
import com.revature.courseapp.data.UserDAO;
import com.revature.courseapp.data.UserPostgres;
import com.revature.courseapp.models.Course;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.utils.Encryption;
import com.revature.courseapp.utils.List;


public class UserServiceImpl implements UserService {
    private UserDAO userDAO = new UserPostgres();
    private CourseDAO courseDAO = new CoursePostgres();

    
    /** Logins in the user by first checking the password
     * @param username
     * @param password
     * @return boolean
     */
    @Override
    public boolean userLogin (String username, String password) {
        // Check user from the database
        boolean isPasswordValid = userDAO.validatePassword(username, password);
        if (isPasswordValid) {
            CourseAppDriver.setLoggedUser(userDAO.findByUsername(username));
        }
        else {
            System.out.println("Password is not correct!");
        }
        return isPasswordValid;
    }

    @Override
    public void studentViewAvailableClasses () {
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
        Course course = courseDAO.findById(course_id);
        course.setCourseName(course_name);
        course.setSemester(semester);
        course.setCapacity(capacity);
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

    
    /** 
     * @param student
     * @param password
     */
    @Override
    public void registerStudent(Student student, String password) {
        // Add student to the database
        byte[] salt = Encryption.generateSalt();
        String pass = Encryption.generateEncryptedPassword(password, salt);
        userDAO.create(student, pass, salt);
        System.out.println(String.format ("Created student %s %s. ID: %d", student.getFirstName(), student.getLastName(), student.getId()));
    }
}