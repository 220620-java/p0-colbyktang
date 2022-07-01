package com.revature.courseapp.data;

import java.sql.*;

import com.revature.courseapp.models.FacultyMember;
import com.revature.courseapp.models.Student;
import com.revature.courseapp.models.User;
import com.revature.courseapp.utils.Encryption;
import com.revature.courseapp.utils.LinkedList;
import com.revature.courseapp.utils.List;

/** This class handles PostgresSQL queries for the table users.
 *  
 * @author Colby Tang
 * @version 1.0
 */
public class UserPostgres extends DatabaseUtils implements UserDAO {

    /** Inserts a user into the table users.
     *  @param user The object to be added to the data source
     *  @param pass
     *  @param byte[]
     *  @return The object that was added or null if the object was unable to be added
     */
    @Override
    public User create(User user) {
        // Check if user is already in the database
        if (doesUserExist(user.getId(), user.getUsername())) {
            System.out.println("User already exists!");
            return null;
        }

        String query = "INSERT INTO users" +
        "  (user_id, first_name, last_name, username, email, usertype) VALUES " +
        " (?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            
            // Prepare the statement for execution by filling user object fields
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getUserType().toString());

            // Execute statement
            preparedStatement.executeUpdate();
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

    /** Inserts a user into the table users with password and salt.
     *  @param user The object to be added to the data source
     *  @param pass
     *  @param byte[]
     *  @return The object that was added or null if the object was unable to be added
     */
    @Override
    public User create(User user, String pass, byte[] salt) {
        // Check if user is already in the database
        if (doesUserExist(user.getId(), user.getUsername())) {
            System.out.println("User already exists!");
            return null;
        }

        String query = "INSERT INTO users" +
        "  (user_id, first_name, last_name, username, email, usertype, password, salt) VALUES " +
        " (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {

            // Prepare the statement for execution by filling user object fields
            preparedStatement = conn.prepareStatement(query);
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

    /** Retrieves the user from the database using a id
     * @param user_id
     * @return User
     */
    @Override
    public User findById(int user_id) {
        User user = null;
        String query = "SELECT * FROM users WHERE user_id=?";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                String firstname = result.getString("first_name");
                String lastname = result.getString("last_name");
                String username = result.getString("username");
                String email = result.getString("email");
                String usertype = result.getString("usertype");
                if (usertype.equals("STUDENT")) {
                    user = new Student (user_id, firstname, lastname, username, email);
                }
                if (usertype.equals("FACULTY")) {
                    user = new FacultyMember (user_id, firstname, lastname, username, email);
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
            closeQuietly(preparedStatement);
            closeQuietly(result);
        }
        return user;
    }

    /** Retrieves the user from the database using a username
     * @param username
     * @return User
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        String query = "SELECT * FROM users WHERE user_id=?";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                int user_id = result.getInt ("user_id");
                String firstname = result.getString("first_name");
                String lastname = result.getString("last_name");
                String email = result.getString("email");
                String usertype = result.getString("usertype");
                if (usertype.equals("STUDENT")) {
                    user = new Student (user_id, firstname, lastname, username, email);
                }
                if (usertype.equals("FACULTY")) {
                    user = new FacultyMember (user_id, firstname, lastname, username, email);
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
            closeQuietly(preparedStatement);
            closeQuietly(result);
        }
        return user;
    }

    /** Retrieves the entire table of users.
     * @return List<User>
     */
    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM users";
        Statement statement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
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
        finally {
            closeQuietly (statement);
            closeQuietly (result);
        }
        return null;
    }

    /** Updates a user in the users table based on the user's id
     * @param User
     */
    @Override
    public void update(User user) {
        if (!doesUserExist(user.getId())) {
            return;
        }

        // (user_id, first_name, last_name, username, email, usertype)
        String query = "UPDATE users SET " +
        "first_name=?, " +
        "last_name=?," + 
        "username=?," +
        "email=?," +
        "usertype=?" +
        "WHERE user_id=?";
        
        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getUserType().toString());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly(preparedStatement);
        }
    }

    /** Removes a user from the database based on the username
     * @param username
     * @return boolean
     */
    @Override
    public void delete(User user) {
        if (!doesUserExist(user.getUsername())) {
            return;
        }
        PreparedStatement preparedStatement = null;

        String query = "DELETE FROM users WHERE username=?";
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly (preparedStatement);
        }
    }

    /** Checks the users table to see if user exists using both user id and username.
     * @param userid
     * @param username
     * @return boolean
     */
    public boolean doesUserExist (int user_id, String username) {
        String query = "SELECT * FROM users WHERE user_id=? AND username=?";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setString(2, username);
            result = preparedStatement.executeQuery(query);
            if (result.next()) {
                System.out.println("User " + result.getInt("user_id") + " exists!");
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            closeQuietly(preparedStatement);
            closeQuietly(result);
        }
        return false;
    }

    /** Checks the users table to see if user exists using the username.
     * @param username
     * @return boolean
     */
    public boolean doesUserExist (String username) {
        String query = "SELECT * FROM users WHERE username=?";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery(query);
            if (result.next()) {
                System.out.println("User " + result.getInt("user_id") + " exists!");
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            closeQuietly(preparedStatement);
            closeQuietly(result);
        }
        return false;
    }

    /** Checks the users table to see if user exists using the id.
     * @param userid
     * @return boolean
     */
    public boolean doesUserExist (int user_id) {
        String query = "SELECT * FROM users WHERE user_id=?";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            result = preparedStatement.executeQuery(query);
            if (result.next()) {
                System.out.println("User " + result.getInt("user_id") + " exists!");
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            closeQuietly(preparedStatement);
            closeQuietly(result);
        }
        return false;
    }

    /** Get a list of all enrolled students of a particular course.
     * @param course_id 
     * @return List<Student>
     */
    public List<Student> getAllEnrolledStudents (int course_id) {
        List<Student> enrolledStudents = new LinkedList<>();
        String query = "SELECT user_id FROM coursesusers WHERE course_id=?";

        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        Statement userStatement = null;
        ResultSet userResult = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, course_id);
            result = preparedStatement.executeQuery ();
            while (result.next()) {
                int user_id = result.getInt ("user_id");
                query = String.format ("SELECT user_id, first_name, last_name, username, email, usertype FROM users WHERE user_id=%d", user_id);
                userStatement = conn.createStatement();
                userResult = userStatement.executeQuery(query);
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
    
    /** Inserts a user into the database, checks if the user already exists first.
     * @param user
     * @param pass
     * @param salt
     */
    // public static void insertUser (Connection conn, User user, String pass, byte[] salt) {
    //     // Check if user is already in the database
    //     if (doesUserExist (conn, user.getId(), user.getUsername())) {
    //         System.out.println("User already exists!");
    //         return;
    //     }

    //     String insertSQLQuery = "INSERT INTO users" +
    //     "  (user_id, first_name, last_name, username, email, usertype, password, salt) VALUES " +
    //     " (?, ?, ?, ?, ?, ?, ?, ?);";
    //     PreparedStatement preparedStatement = null;
    //     try {
    //         // Prepare the statement for execution by filling user object fields
    //         preparedStatement = conn.prepareStatement(insertSQLQuery);
    //         preparedStatement.setInt(1, user.getId());
    //         preparedStatement.setString(2, user.getFirstName());
    //         preparedStatement.setString(3, user.getLastName());
    //         preparedStatement.setString(4, user.getUsername());
    //         preparedStatement.setString(5, user.getEmail());
    //         preparedStatement.setString(6, user.getUserType().toString());
    //         preparedStatement.setString(7, pass);
    //         preparedStatement.setBytes(8, salt);

    //         // Execute statement
    //         preparedStatement.executeUpdate();
    //         preparedStatement.close();
    //     }
    //     catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     finally {
    //         closeQuietly(preparedStatement);
    //     }
    // }
       
    /** Validates a password using a username and password. Password will be encrypted and salted to match the given password.
     * @param username
     * @param password
     * @return boolean
     */
    public boolean validatePassword (String username, String password) {
        String selectSQLQuery = String.format ("SELECT password, salt FROM users WHERE username='%s'", username);
        Statement statement = null;
        ResultSet result = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
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
}
