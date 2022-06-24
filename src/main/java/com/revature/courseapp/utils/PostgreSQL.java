package com.revature.courseapp.utils;

// import software.aws.rds.jdbc.postgresql.Driver;
import java.sql.*;
import java.util.Properties;

import com.revature.courseapp.user.User;
import com.revature.courseapp.user.Student;
import com.revature.courseapp.user.FacultyMember;

public class PostgreSQL {
    public static String url;
    Connection conn;
    Properties props;

    // Constructor for local database
    public PostgreSQL () {
        this("jdbc:postgresql://127.0.0.1:5432/");
    }

    // Constructor for database with default credentials
    public PostgreSQL (String sqlUrl) {
        this(sqlUrl, "postgres", "", "false");
    }

    // Constructor with JDBC URL and credentials
    public PostgreSQL (String sqlUrl, String user, String password, String ssl) {
        setConnection(sqlUrl, user, password, ssl);
    }

    public Connection getConnection () {
        return conn;
    }

    public Connection setConnection (String sqlUrl, String user, String password, String ssl) {
        url = sqlUrl;
        props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("ssl", ssl);
        try {
            conn = DriverManager.getConnection(url, props);
            return conn;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieves the entire table of users
    public List<User> getAllUsers () {
        String sqlQuery = "SELECT * FROM users";
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            List<User> allUsers = new LinkedList<User>();
            while (result.next()) 
            {
                // Convert result into User object
                int id = result.getInt ("userid");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
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

    public boolean DoesUserExist (User user) {
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE userid=%d OR username='%s'", user.getId(), user.getUsername());

        // Make sure user doesn't already exist
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(selectSQLQuery);
            while (result.next()) {
                System.out.println(result.getInt("userid"));
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

    public boolean DoesUserExist (String username) {
        String selectSQLQuery = String.format ("SELECT * FROM users WHERE username='%s'", username);

        // Make sure user doesn't already exist
        try {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(selectSQLQuery);
            while (result.next()) {
                System.out.println(result.getInt("userid"));
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
    public void InsertUser (User user, String pass, byte[] salt) {
        // Check if user is already in the database
        if (DoesUserExist (user)) {
            System.out.println("User already exists!");
            return;
        }

        String insertSQLQuery = "INSERT INTO users" +
        "  (userid, firstName, lastName, username, email, usertype, password, salt) VALUES " +
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
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

    public void truncateTable (String tableName) {
        try {
            Statement statement = conn.createStatement();
            String truncateSQLQuery = String.format ("Truncate table %s", tableName);
            statement.execute(truncateSQLQuery);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println(String.format ("%s Table truncated.", tableName));
    }
}
