package com.revature.courseapp.data;

import java.sql.*;

import com.revature.courseapp.models.Course;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.utils.LinkedList;
import com.revature.courseapp.utils.List;

/** This class handles PostgresSQL queries for the table courses and courses_users.
 *  
 * @author Colby Tang
 * @version 1.0
 */
public class CoursePostgres extends DatabaseUtils implements CourseDAO  {
    private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();

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
        try (Connection conn = connUtil.openConnection()) {
            // Prepare the preparedStatement for execution by filling user object fields
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course.getId());
            preparedStatement.setString(2, course.getCourseName());
            preparedStatement.setString(3, course.getSemester());
            preparedStatement.setInt(4, course.getCapacity());
            preparedStatement.setInt(5, course.getNumberOfStudents());

            // Execute preparedStatement
            System.out.println(
                String.format("Creating new course: \n %s", course.toString())
            );
            preparedStatement.executeUpdate();
            System.out.println("Created new course!");
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
        try (Connection conn = connUtil.openConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            result = preparedStatement.executeQuery ();
            if (result.next()) {
                String course_name = result.getString("course_name");
                String semester = result.getString("semester");
                int capacity = result.getInt("capacity");

                List<Student> students = getAllEnrolledStudents (course_id);

                return new Course(course_id, course_name, semester, capacity, students);
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
        try (Connection conn = connUtil.openConnection()) {
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
        try (Connection conn = connUtil.openConnection()) {
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
        try (Connection conn = connUtil.openConnection()) {
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
        String query = String.format ("SELECT * FROM courses order by course_id");

        Statement statement = null;
        ResultSet result = null;
        try (Connection conn = connUtil.openConnection()) {
            statement = conn.createStatement();
            result = statement.executeQuery (query);
            while (result.next()) {
                int course_id = result.getInt ("course_id");
                String course_name = result.getString("course_name");
                String semester = result.getString("semester");
                int capacity = result.getInt("capacity");
                List<Student> students = getAllEnrolledStudents (course_id);

                availableCourses.add(new Course(course_id, course_name, semester, capacity, students));
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
        try (Connection conn = connUtil.openConnection()) {
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
        String query = "SELECT course_id FROM courses_users WHERE user_id=?";

        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        Statement courseStatement = null;
        ResultSet courseResult = null;
        try (Connection conn = connUtil.openConnection()) {

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
                    List<Student> students = getAllEnrolledStudents (course_id);

                    enrolledCourses.add(new Course(course_id, course_name, semester, capacity, students));
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

    /** Get a list of all enrolled students of a particular course.
     * @param course_id 
     * @return List<Student>
     */
    public List<Student> getAllEnrolledStudents (int course_id) {
        List<Student> enrolledStudents = new LinkedList<>();
        String query = "SELECT user_id FROM courses_users WHERE course_id=?";

        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        Statement userStatement = null;
        ResultSet userResult = null;
        try (Connection conn = connUtil.openConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            result = preparedStatement.executeQuery ();
            while (result.next()) {
                int user_id = result.getInt ("user_id");
                query = String.format ("SELECT user_id, first_name, last_name, username, email, usertype FROM users WHERE user_id=%d", user_id);
                userStatement = conn.createStatement();
                userResult = userStatement.executeQuery(query);
                while (userResult.next()) {
                    int id = userResult.getInt ("user_id");
                    String firstName = userResult.getString("first_name");
                    String lastName = userResult.getString("last_name");
                    String username = userResult.getString("username");
                    String email = userResult.getString("email");
                    String usertype = userResult.getString ("usertype");

                    switch (usertype) {
                        case "STUDENT":
                        enrolledStudents.add(new Student(id, firstName, lastName, username, email));
                        break;
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(preparedStatement);
            closeQuietly(userStatement);
            closeQuietly(result);
            closeQuietly(userResult);
        }
        return enrolledStudents;
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
        try (Connection conn = connUtil.openConnection()) {;
            // Is student already enrolled?
            String query = "SELECT * from courses_users where course_id=? and user_id=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            preparedStatement.setInt(2, student_id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                System.out.println("Student already enrolled!");
                return;
            }
            preparedStatement.close();
            
            // Is course full?
            query = "SELECT size, capacity from courses where course_id=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                int size = result.getInt("size");
                int capacity = result.getInt("capacity");
                if (size >= capacity) {
                    System.out.println("COURSE IS FULL!");
                    return;
                }
            }
            preparedStatement.close();

            query = "INSERT INTO courses_users (course_id, user_id) VALUES (?, ?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            preparedStatement.setInt(2, student_id);

            // Execute preparedStatement
            System.out.println(String.format("Enrolling user %d into course %d...", student_id, course_id));
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Enrolled user!");

            // Increase the size of the course
            query = "UPDATE courses SET " +
                "size=size+1" +
                "WHERE course_id=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(preparedStatement);
        }
    }
    
    /** Withdraw from a course. Returns true if successful.
     * @param course
     * @param student
     * @return boolean
     */
    public boolean withdrawFromCourse (int course_id, int student_id) {
        
        PreparedStatement preparedStatement = null;
        try (Connection conn = connUtil.openConnection()) {
            // Verify that the enrollment exists
            String query = "SELECT course_id from courses_users WHERE course_id=? AND user_id=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            preparedStatement.setInt(2, student_id);
            ResultSet result = preparedStatement.executeQuery();
            if (!result.next()) {
                System.out.println("COULD NOT FIND ENROLLMENT!");
                return false;
            }
            preparedStatement.close();

            query = "DELETE FROM courses_users WHERE course_id=? AND user_id=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            preparedStatement.setInt(2, student_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Decrease the size of the course
            query = "UPDATE courses SET " +
                "size=size-1" +
                "WHERE course_id=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
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
