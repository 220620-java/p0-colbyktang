package com.revature.courseapp.utils;

// import software.aws.rds.jdbc.postgresql.Driver;
import java.sql.*;
import java.util.Properties;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
// import org.postgresql.*;

public class PostgreSQL {
    public static String url;
    protected Connection conn;
    protected Properties props;

    // Constructor for local database
    public PostgreSQL () {
        this("jdbc:postgresql://127.0.0.1:5432/", "postgres", "", "false");
        System.out.println("Using local database!");
    }

    // Constructor for database with credentials from a file
    public PostgreSQL (String jsonFilename) {
        // Find and read json file for database credentials
        // Make sure the filename ends with .json
        System.out.println("Create");
        String[] credentials = readDatabaseCredentials(jsonFilename);
        
        // If credentials did not work then 
        if (credentials != null) {
            setConnection("jdbc:postgresql://" + credentials[0] + ":5432/", credentials[1], credentials[2], "false");
            System.out.println("Using remote database!");
        }
    }

    // Constructor with JDBC URL and credentials
    public PostgreSQL (String sqlUrl, String user, String password, String ssl) {
        setConnection(sqlUrl, user, password, ssl);
    }

    
    /** 
     * @return Connection
     */
    // Get the connection that's opened
    public Connection getConnection () {
        return conn;
    }

    
    /** 
     * @param sqlUrl
     * @param user
     * @param password
     * @param ssl
     * @return Connection
     */
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

    
    /** 
     * @param tableName
     */
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

    
    /** 
     * @param jsonFilename
     * @return String[]
     * @throws JSONException
     */
    public String[] readDatabaseCredentials (String jsonFilename) throws JSONException {
        // if .json extension does not exist then add it
        if (!jsonFilename.endsWith(".json")) {
            jsonFilename += ".json";
        }

        InputStream in = this.getClass().getClassLoader()
            .getResourceAsStream(jsonFilename);

        // if file does not exist then exit
        if (in == null) {
            System.out.println("Json file does not exist! Exiting...");
            return null;
        }

        // Create a string array to hold endpoint, username, and pass
        // Keep credentials null in case it returns during an exception
        String[] credentials = null;
        try {
            JSONTokener tokener = new JSONTokener(in);
            JSONObject json = new JSONObject(tokener);

            // Extract the credentials
            credentials = new String[3];
            String endpoint = (String) json.get("endpoint");
            String username = (String) json.get("username");
            String password = (String) json.get("password");
            credentials[0] = endpoint;
            credentials[1] = username;
            credentials[2] = password;
            return credentials;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return credentials;
    }

}


