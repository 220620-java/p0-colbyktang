package com.revature.courseapp.utils;

import java.sql.*;

import com.revature.courseapp.course.*;
import com.revature.courseapp.user.Student;

public class DatabaseCourses extends PostgreSQL {

    // Make sure course doesn't already exist
    public boolean doesCourseExist (Course course) {
        String query = String.format (
            "SELECT * FROM courses WHERE course_name='%s'", 
            course.getCourseName()
        );

        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);

            // Found result, return true
            while (result.next()) {
                System.out.println(result.getInt("course_id") + " already exists!");
                result.close();
                statement.close();
                return true;
            }
            result.close();
            statement.close();
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void insertCourse (Course course) {
        // Check if user is already in the database
        if (doesCourseExist(course)) {
            System.out.println("Course already exists!");
            return;
        }

        String query = "INSERT INTO courses" +
        "  (course_id, course_name, semester, capacity, size) VALUES " +
        " (?, ?, ?, ?, ?);";

        try {
            // Prepare the statement for execution by filling user object fields
            PreparedStatement preparedStatement = conn.prepareStatement(query);
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
    }

    public boolean removeCourse (Course course) {
        if (!doesCourseExist(course)) {
            return false;
        }

        String query = String.format ("DELETE FROM courses WHERE course_name='%s'", course.getCourseName());
        try {
            Statement statement = conn.createStatement();
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
        return false;
    }

    public void enrollCourse (Course course, Student student) {
        try {
            Statement statement = conn.createStatement();

            String courseQuery = String.format ("SELECT course_id FROM courses WHERE course_id='%d'", course.getId());
            ResultSet courseResult = statement.executeQuery(courseQuery);
            int courseId = -1;
            while (courseResult.next()) {
                courseId = courseResult.getInt("course_id");
            }
            if (courseId == -1) { return; }

            courseResult.close();

            String userQuery = String.format ("SELECT user_id FROM users WHERE user_id='%d'", student.getId());
            ResultSet userResult = statement.executeQuery(userQuery);
            int userId = -1;
            while (userResult.next()) {
                userId = userResult.getInt("user_id");
            }
            if (userId == -1) { return; }

            userResult.close();
            statement.close();

            String courseUserQuery = "INSERT INTO coursesusers (course_id, user_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(courseUserQuery);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, userId);

            // Execute statement
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean WithdrawCourse (Course course, Student student) {
        String query = String.format ("DELETE FROM coursesusers WHERE course_id='%d' AND user_id='%d'", course.getId(), student.getId());
        try {
            Statement statement = conn.createStatement();
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
        return false;
    }

    public Course getCourse (int course_id) {
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
            try { if (result != null) result.close(); } catch (Exception e) {e.printStackTrace();};
            try { if (statement != null) statement.close(); } catch (Exception e) {e.printStackTrace();};
        }
        return null;
    }

    public List<Course> getAllEnrolledCourses (int student_id) {
        List<Course> enrolledCourses = new LinkedList<>();
        String query = String.format ("SELECT course_id FROM coursesusers WHERE user_id=%d", student_id);

        ResultSet result = null;
        ResultSet userResult = null;
        Statement statement = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery (query);
            while (result.next()) {
                int course_id = result.getInt ("course_id");
                query = String.format ("SELECT * FROM courses WHERE course_id=%d", course_id);
                Statement userStatement = conn.createStatement();
                userResult = userStatement.executeQuery(query);
                while (userResult.next()) {
                    String course_name = userResult.getString("course_name");
                    String semester = userResult.getString("semester");
                    int capacity = userResult.getInt("capacity");

                    enrolledCourses.add(new Course(course_id, course_name, semester, capacity));
                }
                if (!userResult.next()) userResult.close();
            }
            if (!result.next()) result.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try { if (result != null) result.close(); } catch (Exception e) {e.printStackTrace();};
            try { if (statement != null) statement.close(); } catch (Exception e) {e.printStackTrace();};
        }
        return enrolledCourses;
    }
}
