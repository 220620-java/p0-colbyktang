package com.revature.courseapp.utils;

import java.sql.*;

import com.revature.courseapp.user.User;
import com.revature.courseapp.user.Student;
import com.revature.courseapp.user.FacultyMember;

public class DatabaseUsers extends DatabaseUtils {

    /** Checks the users table to see if user exists using both user id and username.
     * @param userid
     * @param username
     * @return boolean
     */
    public static boolean doesUserExist (Connection conn, int userid, String username) {
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE user_id=%d AND username='%s'", userid, username);
        return doesUserExistQuery (conn, selectSQLQuery);
    }

    /** Checks the users table to see if user exists using the username.
     * @param username
     * @return boolean
     */
    public static boolean doesUserExist (Connection conn, String username) {
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE username='%s'", username);
        return doesUserExistQuery (conn, selectSQLQuery);
    }

    /** Checks the users table to see if user exists using the id.
     * @param userid
     * @return boolean
     */
    public static boolean doesUserExist (Connection conn, int userid) {
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE user_id='%s'", userid);
        return doesUserExistQuery (conn, selectSQLQuery);
    }

    /** This is to reduce redundant code for doesUserExist 
     *  @param selectSQLQuery
     *  @return boolean
     */
    public static boolean doesUserExistQuery (Connection conn, String selectSQLQuery) {
        Statement statement = null;
        ResultSet result = null;
        // Make sure user doesn't already exist
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(selectSQLQuery);
            if (result.next()) {
                System.out.println("User " + result.getInt("user_id") + " exists!");
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
            closeQuietly(result);
        }
        return false;
    }

    /** Retrieves the entire table of users.
     * @return List<User>
     */
    public static List<User> getAllUsers (Connection conn) {
        String sqlQuery = "SELECT * FROM users";
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sqlQuery);
            List<User> allUsers = new LinkedList<User>();
            while (result.next()) {
                // Convert result into User object
                int id = result.getInt ("user_id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String username = result.getString("username");
                String email = result.getString("email");
                String usertype = result.getString ("usertype");

                switch (usertype) {
                    case "STUDENT":
                    allUsers.add(new Student(id, firstName, lastName, username, email));
                    break;
                    
                    case "FACULTY":
                    allUsers.add(new FacultyMember(id, firstName, lastName, username, email));
                    break;
                }
            }
            return allUsers;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly (statement);
            closeQuietly (result);
        }
        return null;
    }

    
    /** Get a list of all enrolled students of a particular course.
     * @param course_id 
     * @return List<Student>
     */
    public static List<Student> getAllEnrolledStudents (Connection conn, int course_id) {
        List<Student> enrolledStudents = new LinkedList<>();
        String query = String.format ("SELECT user_id FROM coursesusers WHERE course_id=%d", course_id);
        Statement statement = null;
        ResultSet result = null;
        ResultSet userResult = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery (query);
            while (result.next()) {
                int user_id = result.getInt ("user_id");
                query = String.format ("SELECT user_id, first_name, last_name, username, email, usertype FROM users WHERE user_id=%d", user_id);
                userResult = statement.executeQuery(query);
                while (userResult.next()) {
                    int id = result.getInt ("user_id");
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String username = result.getString("username");
                    String email = result.getString("email");
                    String usertype = result.getString ("usertype");

                    switch (usertype) {
                        case "STUDENT":
                        enrolledStudents.add(new Student(id, firstName, lastName, username, email));
                        break;
                    }
                }
                userResult.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
            closeQuietly(result);
            closeQuietly(userResult);
        }
        return enrolledStudents;
    }
    
    /** Inserts a user into the database, checks if the user already exists first.
     * @param user
     * @param pass
     * @param salt
     */
    public static void insertUser (Connection conn, User user, String pass, byte[] salt) {
        // Check if user is already in the database
        if (doesUserExist (conn, user.getId(), user.getUsername())) {
            System.out.println("User already exists!");
            return;
        }

        String insertSQLQuery = "INSERT INTO users" +
        "  (user_id, first_name, last_name, username, email, usertype, password, salt) VALUES " +
        " (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            // Prepare the statement for execution by filling user object fields
            preparedStatement = conn.prepareStatement(insertSQLQuery);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getUserType().toString());
            preparedStatement.setString(7, pass);
            preparedStatement.setBytes(8, salt);

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

    
    /** Retrieves the user from the database using a username
     * @param username
     * @return User
     */
    public static User getUserFromDB (Connection conn, String username) {
        User user = null;
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE username='%s'", username);
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(selectSQLQuery);
            while (result.next()) {
                int userid = result.getInt("user_id");
                String firstname = result.getString("first_name");
                String lastname = result.getString("last_name");
                String email = result.getString("email");
                String usertype = result.getString("usertype");
                if (usertype.equals("STUDENT")) {
                    user = new Student (userid, firstname, lastname, username, email);
                }
                if (usertype.equals("FACULTY")) {
                    user = new FacultyMember (userid, firstname, lastname, username, email);
                }

                System.out.println("RETURNING " + user);
                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
            closeQuietly(result);
        }
        return user;
    }

    
    /** Validates a password using a username and password. Password will be encrypted and salted to match the given password.
     * @param username
     * @param password
     * @return boolean
     */
    public static boolean validatePassword (Connection conn, String username, String password) {
        String selectSQLQuery = String.format ("SELECT password, salt FROM users WHERE username='%s'", username);
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(selectSQLQuery);
            if (result.next()) {
                String dbPass = result.getString("password");
                byte[] salt = result.getBytes("salt");
                String ePass = Encryption.generateEncryptedPassword(password, salt);

                if (!ePass.equals(dbPass)) {
                    System.out.println("Passwords do not match! ");
                    return false;
                }
                return ePass.equals(dbPass);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(statement);
            closeQuietly(result);
        }
        return false;
    }

    
    /** Removes a user from the database based on the username
     * @param username
     * @return boolean
     */
    public static boolean removeUser (Connection conn, String username) {
        if (!doesUserExist(conn, username)) {
            return false;
        }

        String removeSQLQuery = String.format ("DELETE FROM users WHERE username='%s'", username);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(removeSQLQuery);
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
}
