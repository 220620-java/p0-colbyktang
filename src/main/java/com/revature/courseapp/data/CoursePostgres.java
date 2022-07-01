package com.revature.courseapp.data;

import java.sql.*;

import com.revature.courseapp.models.Course;
import com.revature.courseapp.utils.LinkedList;
import com.revature.courseapp.utils.List;

/** This class handles PostgresSQL queries for the table courses and courses_users.
 *  
 * @author Colby Tang
 * @version 1.0
 */
public class CoursePostgres extends DatabaseUtils implements CourseDAO  {

    /** Inserts a course into the database if it doesn't already exist.
     * @param course
     */
    @Override
    public Course create(Course course) {
        // Check if user is already in the database
        if (doesCourseExist(course.getId())) {
            System.out.println("Course already exists!");
            return null;
        }

        String query = "INSERT INTO courses" +
        "  (course_id, course_name, semester, capacity, size) VALUES " +
        " (?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            // Prepare the preparedStatement for execution by filling user object fields
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course.getId());
            preparedStatement.setString(2, course.getCourseName());
            preparedStatement.setString(3, course.getSemester());
            preparedStatement.setInt(4, course.getCapacity());
            preparedStatement.setInt(5, course.getNumberOfStudents());

            // Execute preparedStatement
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return course;
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
        return null;
    }

    /** Retrieve a course using the course_id.
     * @param course_id
     * @return Course
     */
    @Override
    public Course findById(int course_id) {
        String query = "SELECT * FROM courses WHERE course_id=?";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            result = preparedStatement.executeQuery ();
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
            closeQuietly(preparedStatement);
            closeQuietly(result);
        }
        return null;
    }

    /** Updates a field in the courses table based on the course's id
     * @param Course
     */
    @Override
    public void update(Course course) {
        if (!doesCourseExist(course.getId())) {
            return;
        }
        // (course_id, course_name, semester, capacity, size)
        String query = "UPDATE courses SET " +
        "course_name=?, " +
        "semester=?," + 
        "capacity=?," +
        "size=?" +
        "WHERE course_id=?";
        
        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, course.getCourseName());
            preparedStatement.setString(2, course.getSemester());
            preparedStatement.setInt(3, course.getCapacity());
            preparedStatement.setInt(4, course.getNumberOfStudents());
            preparedStatement.setInt(5, course.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(preparedStatement);
        }
    }

    /** Removes a course from the catalog. It should also delete any students enrolled into the class.
     * @param course
     */
    @Override
    public void delete(Course course) {
        if (!doesCourseExist(course.getId())) {
            return;
        }

        String query = "DELETE FROM courses WHERE course_id=?";
        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course.getId());
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

    /** Removes a course from the catalog. It should also delete any students enrolled into the class.
     * @param course
     */
    @Override
    public void delete(int course_id) {
        if (!doesCourseExist(course_id)) {
            return;
        }

        String query = "DELETE FROM courses WHERE course_id=?";
        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
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
     * Retrieve all available courses for the semester.
     * @param conn
     * @return
     */
    @Override
    public List<Course> findAll() {
        List<Course> availableCourses = new LinkedList<>();
        String query = String.format ("SELECT course_id FROM courses order by course_id");

        Statement statement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
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

    /** Checks if course already exists in the table.
     * @param course
     * @return boolean
     */
    public boolean doesCourseExist (int course_id) {
        String query = "SELECT * FROM courses WHERE course_id=?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            resultSet = preparedStatement.executeQuery();

            // Found result, return true
            if (resultSet.next()) {
                System.out.println(resultSet.getInt("course_id") + " found!");
                return true;
            }
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            closeQuietly(resultSet);
        }
        return false;
    }
    
    /** Retrieve all the enrolled courses a student has using their student id.
     * @param student_id
     * @return List<Course>
     */
    public List<Course> getAllEnrolledCourses (int student_id) {
        List<Course> enrolledCourses = new LinkedList<>();
        String query = "SELECT course_id FROM coursesusers WHERE user_id=?";

        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        Statement courseStatement = null;
        ResultSet courseResult = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, student_id);
            result = preparedStatement.executeQuery ();

            while (result.next()) {
                int course_id = result.getInt ("course_id");
                query = String.format ("SELECT * FROM courses WHERE course_id=%d", course_id);
                courseStatement = conn.createStatement();
                courseResult = courseStatement.executeQuery(query);
                while (courseResult.next()) {
                    String course_name = courseResult.getString("course_name");
                    String semester = courseResult.getString("semester");
                    int capacity = courseResult.getInt("capacity");

                    enrolledCourses.add(new Course(course_id, course_name, semester, capacity));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(preparedStatement);
            closeQuietly(result);
            closeQuietly(courseStatement);
            closeQuietly(courseResult);
        }
        return enrolledCourses;
    }
   
    /** Have a student enroll in a course by adding it to the courses_users table.
     * @param course
     * @param student
     */
    public void enrollCourse (int course_id, int student_id) {
        if (!doesCourseExist(course_id)) {
            return;
        }

        PreparedStatement preparedStatement = null;
        // ResultSet courseResult = null;
        // ResultSet userResult = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {;
            /*
            preparedStatement = conn.prepareStatement(query);

           
            String courseQuery = String.format ("SELECT course_id FROM courses WHERE course_id='%d'", course.getId());
            courseResult = preparedStatement.executeQuery(courseQuery);
            int courseId = -1;
            while (courseResult.next()) {
                courseId = courseResult.getInt("course_id");
            }
            if (courseId == -1) { return; }

            String userQuery = String.format ("SELECT user_id FROM users WHERE user_id='%d'", student.getId());
            userResult = preparedStatement.executeQuery(userQuery);
            int userId = -1;
            while (userResult.next()) {
                userId = userResult.getInt("user_id");
            }
            if (userId == -1) { return; }

            */

            String courseUserQuery = "INSERT INTO courses_users (course_id, user_id) VALUES (?, ?)";
            preparedStatement = conn.prepareStatement(courseUserQuery);
            preparedStatement.setInt(1, course_id);
            preparedStatement.setInt(2, student_id);

            // Execute preparedStatement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(preparedStatement);
            closeQuietly(preparedStatement);
            // closeQuietly(courseResult);
            // closeQuietly(userResult);
        }
    }
    
    /** Withdraw from a course. Returns true if successful.
     * @param course
     * @param student
     * @return boolean
     */
    public boolean withdrawFromCourse (int course_id, int student_id) {
        String query = "DELETE FROM coursesusers WHERE course_id=? AND user_id=?";
        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            preparedStatement.setInt(2, student_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
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
        return false;
    }
}
