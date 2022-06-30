package com.revature.courseapp.utils;

import java.sql.*;

import com.revature.courseapp.course.*;
import com.revature.courseapp.user.Student;

public class DatabaseCourses extends DatabaseUtils {

    /** Checks if course already exists in the table.
     * @param course
     * @return boolean
     */
    // Make sure course doesn't already exist
    public static boolean doesCourseExist (Connection conn, Course course) {
        String query = String.format (
            "SELECT * FROM courses WHERE course_name='%s'", 
            course.getCourseName()
        );

        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);

            // Found result, return true
            if (resultSet.next()) {
                System.out.println(resultSet.getInt("course_id") + " already exists!");
                return true;
            }
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            closeQuietly(statement);
            closeQuietly(resultSet);
        }
        return false;
    }
    
    
    /** 
     * @param course
     */
    public static void insertCourse (Connection conn, Course course) {
        // Check if user is already in the database
        if (doesCourseExist(conn, course)) {
            System.out.println("Course already exists!");
            return;
        }

        String query = "INSERT INTO courses" +
        "  (course_id, course_name, semester, capacity, size) VALUES " +
        " (?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = null;
        try {
            // Prepare the statement for execution by filling user object fields
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course.getId());
            preparedStatement.setString(2, course.getCourseName());
            preparedStatement.setString(3, course.getSemester());
            preparedStatement.setInt(4, course.getCapacity());
            preparedStatement.setInt(5, course.getNumberOfStudents());

            // Execute statement
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(preparedStatement);
        }
    }

    
    /** 
     * @param course
     * @return boolean
     */
    public static boolean removeCourse (Connection conn, Course course) {
        if (!doesCourseExist(conn, course)) {
            return false;
        }

        String query = String.format ("DELETE FROM courses WHERE course_name='%s'", course.getCourseName());
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(query);
            statement.close();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
        }
        return false;
    }

    
    /** 
     * @param course
     * @param student
     */
    public static void enrollCourse (Connection conn, Course course, Student student) {
        if (!doesCourseExist(conn, course)) {
            return;
        }

        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet courseResult = null;
        ResultSet userResult = null;
        try {;
            statement = conn.createStatement();

            String courseQuery = String.format ("SELECT course_id FROM courses WHERE course_id='%d'", course.getId());
            courseResult = statement.executeQuery(courseQuery);
            int courseId = -1;
            while (courseResult.next()) {
                courseId = courseResult.getInt("course_id");
            }
            if (courseId == -1) { return; }

            String userQuery = String.format ("SELECT user_id FROM users WHERE user_id='%d'", student.getId());
            userResult = statement.executeQuery(userQuery);
            int userId = -1;
            while (userResult.next()) {
                userId = userResult.getInt("user_id");
            }
            if (userId == -1) { return; }

            String courseUserQuery = "INSERT INTO coursesusers (course_id, user_id) VALUES (?, ?)";
            preparedStatement = conn.prepareStatement(courseUserQuery);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, userId);

            // Execute statement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
            closeQuietly(preparedStatement);
            closeQuietly(courseResult);
            closeQuietly(userResult);
        }
    }

    
    /** 
     * @param course
     * @param student
     * @return boolean
     */
    public static boolean withdrawCourse (Connection conn, Course course, Student student) {
        String query = String.format ("DELETE FROM coursesusers WHERE course_id='%d' AND user_id='%d'", course.getId(), student.getId());
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(query);
            statement.close();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
        }
        return false;
    }

    
    /** 
     * @param course_id
     * @return Course
     */
    public static Course getCourse (Connection conn, int course_id) {
        String query = String.format ("SELECT * FROM courses WHERE course_id=%d", course_id);
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery (query);
            while (result.next()) {
                String course_name = result.getString("course_name");
                String semester = result.getString("semester");
                int capacity = result.getInt("capacity");

                return new Course(course_id, course_name, semester, capacity);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
            closeQuietly(result);
        }
        return null;
    }

    
    /** 
     * @param student_id
     * @return List<Course>
     */
    public static List<Course> getAllEnrolledCourses (Connection conn, int student_id) {
        List<Course> enrolledCourses = new LinkedList<>();
        String query = String.format ("SELECT course_id FROM coursesusers WHERE user_id=%d", student_id);

        Statement statement = null;
        ResultSet result = null;
        Statement userStatement = null;
        ResultSet userResult = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery (query);
            while (result.next()) {
                int course_id = result.getInt ("course_id");
                query = String.format ("SELECT * FROM courses WHERE course_id=%d", course_id);
                userStatement = conn.createStatement();
                userResult = userStatement.executeQuery(query);
                while (userResult.next()) {
                    String course_name = userResult.getString("course_name");
                    String semester = userResult.getString("semester");
                    int capacity = userResult.getInt("capacity");

                    enrolledCourses.add(new Course(course_id, course_name, semester, capacity));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
            closeQuietly(result);
            closeQuietly(statement);
            closeQuietly(userResult);
        }
        return enrolledCourses;
    }

    public static List<Course> getAllAvailableCourses (Connection conn) {
        List<Course> availableCourses = new LinkedList<>();
        String query = String.format ("SELECT course_id FROM courses order by course_id");

        Statement statement = null;
        ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery (query);
            while (result.next()) {
                int course_id = result.getInt ("course_id");
                String course_name = result.getString("course_name");
                String semester = result.getString("semester");
                int capacity = result.getInt("capacity");
                availableCourses.add(new Course(course_id, course_name, semester, capacity));
        }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
            closeQuietly(result);
        }
        return availableCourses;
    }
}
