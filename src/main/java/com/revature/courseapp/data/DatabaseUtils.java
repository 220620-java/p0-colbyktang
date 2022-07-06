package com.revature.courseapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/* Methods used by Postgres database implementations.
*  
* @author Colby Tang
* @version 1.0
*/
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
        Statement statement = null;
        ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
        try (Connection conn = connUtil.openConnection()) {
            String query = "truncate " + tableName + " restart identity CASCADE;";
            statement = conn.createStatement();
            statement.executeUpdate(query);
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
