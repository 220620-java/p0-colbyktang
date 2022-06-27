package com.revature.courseapp.utils;

// import software.aws.rds.jdbc.postgresql.Driver;
import java.sql.*;
import java.util.Properties;

public class PostgreSQL {
    public static String url;
    protected Connection conn;
    protected Properties props;

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

    // Get the connection that's opened
    public Connection getConnection () {
        return conn;
    }

    // Open a connection with the username and password
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

    public void closeConnection () {
        try {
            if (conn != null)
                conn.close();
        }
        catch (SQLException e) {
            e.getStackTrace();
        }
    }

    // Clears a table
    public void truncateTable (String tableName) {
        try {
            Statement statement = conn.createStatement();
            String truncateSQLQuery = String.format ("Truncate table %s CASCADE", tableName);
            statement.execute(truncateSQLQuery);
            statement.close();
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
