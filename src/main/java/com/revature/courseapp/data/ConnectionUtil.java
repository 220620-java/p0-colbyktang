package com.revature.courseapp.data;

import java.sql.*;
import java.util.Properties;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * A class that establishes a connection with either a default local database or a remote database.
 * When accessing a remote database, a JSON object in the src/resources is read for the endpoint,
 * username, and password to log into the database.
 * @author Colby Tang
 * @version 1.0
 */
public class ConnectionUtil {
    protected String url;
    protected Connection conn;
    protected Properties props;

    // Singleton design pattern
    private static ConnectionUtil connUtil;

    private ConnectionUtil () {
        this ("remote_db.json");
    }

    private ConnectionUtil (String jsonFilename) {
        props = new Properties();
        try {
            String[] credentials = readDatabaseCredentials(jsonFilename);
            if (credentials == null) { System.out.println("Could not read " + jsonFilename);}
            String endpoint = credentials[0];
            String user = credentials[1];
            String password = credentials[2];
            String ssl = credentials[3];

            props.setProperty("user", user);
            props.setProperty("password", password);
            props.setProperty("ssl", ssl);

            String url = String.format ("jdbc:postgresql://%s:5432/", endpoint);
            conn = DriverManager.getConnection(url, props);
            System.out.println("Using remote database!");
        }
        catch (SQLException e) {
            try {
                System.out.println("Could not connect to remote database, using local database!");
                String[] credentials = readDatabaseCredentials("local_db.json");
                String endpoint = credentials[0];
                String user = credentials[1];
                String password = credentials[2];
                String ssl = credentials[3];
    
                props.setProperty("user", user);
                props.setProperty("password", password);
                props.setProperty("ssl", ssl);

                String url = String.format ("jdbc:postgresql://%s:5432/", endpoint);
                conn = DriverManager.getConnection(url, props);
                System.out.println("Using local database!");
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Returns the object reference for singleton pattern.
     * @return ConnectionUtil
     */
    public static synchronized ConnectionUtil getConnectionUtil () {
        if (connUtil == null) {
            connUtil = new ConnectionUtil();
        }
        return connUtil;
    }

    /**
     * Returns the object reference for singleton pattern.
     * @param String
     * @return ConnectionUtil
     */
    public static synchronized ConnectionUtil getConnectionUtil (String jsonFilename) {
        if (connUtil == null) {
            connUtil = new ConnectionUtil(jsonFilename);
        }
        return connUtil;
    }

    /** 
     * @return Connection
     */
    // Get the connection that's opened
    public Connection getCurrentConnection () {
        return conn;
    }

    /**
     * Close the connection if it's open.
     * @param conn
     */
    public static void closeConnection (Connection conn) {
        try {
            if (conn != null)
                conn.close();
        }
        catch (SQLException e) {
            e.getStackTrace();
        }
    }

    /**
     * Close the connection if it's open using the default method.
     * 
     */
    public void closeConnection () {
        closeConnection (conn);
    }

    /**
     * Retrieves the jdbc url used to connect to the database.
     * @return String - the url
     */
    public String getUrl () {
        return url;
    }

    /** Reads a json file in /src/main/resources for the database credentials.
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
            credentials = new String[4];
            String endpoint = (String) json.get("endpoint");
            String username = (String) json.get("username");
            String password = (String) json.get("password");
            String ssl = (String) json.get("ssl");
            credentials[0] = endpoint;
            credentials[1] = username;
            credentials[2] = password;
            credentials[3] = ssl;
            return credentials;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return credentials;
    }
}


