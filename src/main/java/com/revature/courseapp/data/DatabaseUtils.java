package com.revature.courseapp.data;

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

    /** Clears a table
     * @param tableName
     */
    public static void truncateTable (String tableName) {
        PreparedStatement preparedStatement = null;
        try (Connection conn = ConnectionUtil.getConnectionUtil().getCurrentConnection()) {
            String truncateSQLQuery = String.format ("Truncate table ? CASCADE");
            preparedStatement = conn.prepareStatement(truncateSQLQuery);
            preparedStatement.setString(1, tableName);
            preparedStatement.execute(truncateSQLQuery);
            preparedStatement.close();
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
            closeQuietly(preparedStatement);
        }
        System.out.println(String.format ("%s Table truncated.", tableName));
    }
}
