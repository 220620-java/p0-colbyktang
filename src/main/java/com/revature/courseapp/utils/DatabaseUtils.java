package com.revature.courseapp.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseUtils {
    /** Close statement with exception handling.
     * @param statement
     */
    public static void closeQuietly (Statement statement) {
        try {
            if (statement != null) statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Close ResultSet with exception handling.
     * @param statement
     */
    public static void closeQuietly (ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Close PreparedStatement with exception handling.
     * @param statement
     */
    public static void closeQuietly (PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** 
     * @param tableName
     */
    // Clears a table
    public static void truncateTable (Connection conn, String tableName) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
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
        finally {
            closeQuietly(statement);
        }
        System.out.println(String.format ("%s Table truncated.", tableName));
    }
}
