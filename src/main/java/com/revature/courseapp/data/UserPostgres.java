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
    private ConnectionUtil connUtil;

    public UserPostgres () {
        connUtil = ConnectionUtil.getConnectionUtil();
    }

    public UserPostgres (String jsonFilename) {
        connUtil = ConnectionUtil.getConnectionUtil(jsonFilename);
    }

    /** Inserts a user into the table users.
     *  @param user The object to be added to the data source
     *  @param pass
     *  @param byte[]
     *  @return The object that was added or null if the object was unable to be added
     */
    @Override
    public User create(User user) {
        // Check if user is already in the database
        if (doesUserExist(user.getUsername())) {
            System.out.println("User already exists!");
            return null;
        }

        // Insert the user into the database
        String query = "INSERT INTO users" +
        "  (user_id, first_name, last_name, username, email, usertype) VALUES " +
        " (?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try (Connection conn = connUtil.openConnection()) {
            
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
     *  @param salt
     *  @return The object that was added or null if the object was unable to be added
     */
    @Override
    public User create(User user, String pass) {
        // Check if user is already in the database
        if (doesUserExist(user.getUsername())) {
            System.out.println("User already exists!");
            return null;
        }

        byte[] salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword(pass, salt);

        // Inserts into the user table.
        String query = "INSERT INTO users" +
        "  (user_id, first_name, last_name, username, email, usertype, password, salt) VALUES " +
        " (?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = null;
        try (Connection conn = connUtil.openConnection()) {

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
            return user;
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

    /** Inserts a Student into the table users with password and salt and into the students table.
     *  @param student The object to be added to the data source
     *  @param pass
     *  @param salt
     *  @return The object that was added or null if the object was unable to be added
     */
    @Override
    public Student create (Student student, String pass) {
        // Check if user is already in the database
        if (doesUserExist(student.getUsername())) {
            System.out.println("User already exists!");
            return null;
        }

        byte[] salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword(pass, salt);

        // Inserts into the users table and then the students table.
        String query = "WITH insert_user AS ( " +
            "INSERT INTO users(first_name, last_name, username, email, usertype, password, salt) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?) " +
            "RETURNING user_id " +
            ")" +
            "INSERT INTO students (user_id, major, gpa) " +
            "SELECT user_id, ?, ? FROM insert_user " +
            "RETURNING user_id;";

        PreparedStatement preparedStatement = null;
        try (Connection conn = connUtil.openConnection()) {

            // Prepare the statement for execution by filling user object fields
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getUsername());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setString(5, student.getUserType().toString());
            preparedStatement.setString(6, pass);
            preparedStatement.setBytes(7, salt);
            preparedStatement.setString(8, student.getMajor());
            preparedStatement.setFloat(9, student.getGpa());

            // Execute statement
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                int id = result.getInt("user_id");
                return new Student(id, student.getFirstName(), student.getLastName(), student.getUsername(), student.getEmail(), student.getMajor(), student.getGpa());
            }
            return student;
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

    /** Inserts a Faculty Member into the table users with password and salt and the table faculty.
     *  @param facultyMember The object to be added to the data source
     *  @param pass
     *  @param salt
     *  @return The object that was added or null if the object was unable to be added
     */
    @Override
    public FacultyMember create (FacultyMember facultyMember, String pass) {
        // Check if user is already in the database
        if (doesUserExist(facultyMember.getUsername())) {
            System.out.println("User already exists!");
            return null;
        }

        byte[] salt = Encryption.generateSalt();
        pass = Encryption.generateEncryptedPassword(pass, salt);

        // Inserts into the users table and then the faculty table.
        String query = "WITH insert_user AS (" +
            "INSERT INTO users(first_name, last_name, username, email, usertype, password, salt) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?) " +
            "RETURNING user_id " +
            ") " +
            "INSERT INTO faculty (user_id, department) " +
            "SELECT user_id, ? FROM insert_user " +
            "RETURNING user_id;";

        PreparedStatement preparedStatement = null;
        try (Connection conn = connUtil.openConnection()) {

            // Prepare the statement for execution by filling user object fields
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, facultyMember.getFirstName());
            preparedStatement.setString(2, facultyMember.getLastName());
            preparedStatement.setString(3, facultyMember.getUsername());
            preparedStatement.setString(4, facultyMember.getEmail());
            preparedStatement.setString(5, facultyMember.getUserType().toString());
            preparedStatement.setString(6, pass);
            preparedStatement.setBytes(7, salt);
            preparedStatement.setString(8, facultyMember.getDepartment());

            // Execute statement
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                int id = result.getInt ("user_id");
                return new FacultyMember(id, facultyMember.getFirstName(), facultyMember.getLastName(), facultyMember.getUsername(), facultyMember.getEmail(), facultyMember.getDepartment());
            }
            return null;
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
        PreparedStatement preparedUserStatement = null;
        ResultSet userResult = null;
        try (Connection conn = connUtil.openConnection()) {
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
                    query = "SELECT * FROM students WHERE user_id=?";
                    preparedUserStatement = conn.prepareStatement(query);
                    preparedUserStatement.setInt(1, user_id);
                    userResult = preparedUserStatement.executeQuery();
                    if (userResult.next()) {
                        String major = userResult.getString("major");
                        Float gpa = userResult.getFloat("gpa");
                        user = new Student (user_id, firstname, lastname, username, email, major, gpa);
                    }
                    else {
                        System.out.println("Could not find student!");
                    }
                    
                }
                else if (usertype.equals("FACULTY")) {
                    query = "SELECT * FROM faculty WHERE user_id=?";
                    preparedUserStatement = conn.prepareStatement(query);
                    preparedUserStatement.setInt(1, user_id);
                    userResult = preparedUserStatement.executeQuery();
                    
                    if (userResult.next()) {
                        String department = userResult.getString("department");
                        user = new FacultyMember (user_id, firstname, lastname, username, email, department);
                    }
                    else {
                        System.out.println("Could not find faculty!");
                    }
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
            closeQuietly(preparedUserStatement);
            closeQuietly(result);
            closeQuietly(userResult);
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
        String query = "SELECT * FROM users WHERE username=?";
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        PreparedStatement preparedUserStatement = null;
        ResultSet userResult = null;
        try (Connection conn = connUtil.openConnection()) {
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
                    query = "SELECT * FROM students WHERE user_id=?";
                    preparedUserStatement = conn.prepareStatement(query);
                    preparedUserStatement.setInt(1, user_id);
                    userResult = preparedUserStatement.executeQuery();
                    if (userResult.next()) {
                        String major = userResult.getString("major");
                        Float gpa = userResult.getFloat("gpa");
                        user = new Student (user_id, firstname, lastname, username, email, major, gpa);
                    }
                    else {
                        System.out.println("Could not find student!");
                    }
                    
                }
                else if (usertype.equals("FACULTY")) {
                    query = "SELECT * FROM faculty WHERE user_id=?";
                    preparedUserStatement = conn.prepareStatement(query);
                    preparedUserStatement.setInt(1, user_id);
                    userResult = preparedUserStatement.executeQuery();
                    
                    if (userResult.next()) {
                        String department = userResult.getString("department");
                        user = new FacultyMember (user_id, firstname, lastname, username, email, department);
                    }
                    else {
                        System.out.println("Could not find faculty!");
                    }
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
            closeQuietly(preparedUserStatement);
            closeQuietly(result);
            closeQuietly(userResult);
        }
        return user;
    }

    /** Retrieves the entire table of users.
     * @return List<User>
     */
    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";
        Statement statement = null;
        ResultSet result = null;
        PreparedStatement preparedUserStatement = null;
        ResultSet userResult = null;
        try (Connection conn = connUtil.openConnection()) {
            statement = conn.createStatement();
            result = statement.executeQuery(query);
            List<User> allUsers = new LinkedList<User>();
            while (result.next()) {
                // Convert result into User object
                int user_id = result.getInt ("user_id");
                String firstname = result.getString("first_name");
                String lastname = result.getString("last_name");
                String username = result.getString("username");
                String email = result.getString("email");
                String usertype = result.getString ("usertype");

                if (usertype.equals("STUDENT")) {
                    query = "SELECT * FROM students WHERE user_id=?";
                    preparedUserStatement = conn.prepareStatement(query);
                    preparedUserStatement.setInt(1, user_id);
                    userResult = preparedUserStatement.executeQuery();

                    if (userResult.next()) {
                        String major = userResult.getString("major");
                        Float gpa = userResult.getFloat("gpa");
                        allUsers.add(new Student (user_id, firstname, lastname, username, email, major, gpa));
                    }
                    
                }
                else if (usertype.equals("FACULTY")) {
                    query = "SELECT * FROM faculty WHERE user_id=?";
                    preparedUserStatement = conn.prepareStatement(query);
                    preparedUserStatement.setInt(1, user_id);
                    userResult = preparedUserStatement.executeQuery();
                    
                    if (userResult.next()) {
                        String department = userResult.getString("department");
                        allUsers.add(new FacultyMember (user_id, firstname, lastname, username, email, department));
                    }
                    else {
                        System.out.println("Could not find faculty!");
                    }
                }
            }
            return allUsers;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeQuietly (statement);
            closeQuietly (preparedUserStatement);
            closeQuietly (result);
            closeQuietly (userResult);
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
        try (Connection conn = connUtil.openConnection()) {
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
        try (Connection conn = connUtil.openConnection()) {
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
        try (Connection conn = connUtil.openConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setString(2, username);
            result = preparedStatement.executeQuery();
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
        try (Connection conn = connUtil.openConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();
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
        try (Connection conn = connUtil.openConnection()) {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            result = preparedStatement.executeQuery();
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
       
    /** Validates a password using a username and password. Password will be encrypted and salted to match the given password.
     * @param username
     * @param password
     * @return boolean
     */
    public boolean validatePassword (String username, String password) {
        String selectSQLQuery = String.format ("SELECT password, salt FROM users WHERE username='%s'", username);
        Statement statement = null;
        ResultSet result = null;
        try (Connection conn = connUtil.openConnection()) {
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
