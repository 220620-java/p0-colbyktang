package com.revature.courseapp.utils;

import java.sql.*;

import com.revature.courseapp.user.User;
import com.revature.courseapp.user.Student;
import com.revature.courseapp.user.FacultyMember;

public class DatabaseUsers extends PostgreSQL {

    // Retrieves the entire table of users
    public List<User> getAllUsers () {
        String sqlQuery = "SELECT * FROM users";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
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
            result.close();
            statement.close();
            return allUsers;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> getAllEnrolledStudents (int course_id) {
        List<Student> enrolledStudents = new LinkedList<>();
        String query = String.format ("SELECT user_id FROM coursesusers WHERE course_id=%d", course_id);
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery (query);
            while (result.next()) {
                int user_id = result.getInt ("user_id");
                query = String.format ("SELECT user_id, first_name, last_name, username, email, usertype FROM users WHERE user_id=%d", user_id);
                ResultSet userResult = statement.executeQuery(query);
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
            result.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return enrolledStudents;
    }

    public boolean doesUserExist (User user) {
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE user_id=%d OR username='%s'", user.getId(), user.getUsername());

        // Make sure user doesn't already exist
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(selectSQLQuery);
            while (result.next()) {
                System.out.println(result.getInt("user_id"));
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

    public boolean doesUserExist (String username) {
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE username='%s'", username);

        // Make sure user doesn't already exist
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(selectSQLQuery);

            // Found result, return true
            while (result.next()) {
                System.out.println(result.getInt("user_id") + " already exists!");
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

    // Inserts a user into the database
    public void insertUser (User user, String pass, byte[] salt) {
        // Check if user is already in the database
        if (doesUserExist (user)) {
            System.out.println("User already exists!");
            return;
        }

        String insertSQLQuery = "INSERT INTO users" +
        "  (user_id, first_name, last_name, username, email, usertype, password, salt) VALUES " +
        " (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            // Prepare the statement for execution by filling user object fields
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQLQuery);
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
    }

    public User getUserFromDB (String username) {
        User user = null;
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE username='%s'", username);
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(selectSQLQuery);
            while (result.next()) {
                int userid = result.getInt("userid");
                String firstname = result.getString("firstname");
                String lastname = result.getString("lastname");
                String email = result.getString("email");
                String usertype = result.getString("usertype");
                if (usertype.equals("STUDENT")) {
                    user = new Student (userid, firstname, lastname, username, email);
                }
                if (usertype.equals("FACULTY")) {
                    user = new FacultyMember (userid, firstname, lastname, username, email);
                }
                result.close();
                statement.close();
                System.out.println("RETURNING " + user);
                return user;
            }
            result.close();
            statement.close();
            return user;
        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean validatePassword (String username, String password) {
        String selectSQLQuery = String.format ("SELECT password, salt FROM users WHERE username='%s'", username);
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(selectSQLQuery);
            while (result.next()) {
                String dbPass = result.getString("password");
                byte[] salt = result.getBytes("salt");
                String ePass = Encryption.generateEncryptedPassword(password, salt);
                result.close();
                statement.close();
                if (!ePass.equals(dbPass)) {
                    System.out.println("Passwords do not match! ");
                    return false;
                }
                return ePass.equals(dbPass);
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

    public boolean removeUser (String username) {
        if (!doesUserExist(username)) {
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
