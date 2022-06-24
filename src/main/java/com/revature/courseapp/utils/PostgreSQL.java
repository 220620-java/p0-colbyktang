package com.revature.courseapp.utils;

// import software.aws.rds.jdbc.postgresql.Driver;
import java.sql.*;
import java.util.Properties;

import com.revature.courseapp.user.User;

public class PostgreSQL {
    public static String url;

    public PostgreSQL () {
        this("jdbc:postgresql://127.0.0.1:5432/");
    }

    public PostgreSQL (String sqlUrl) {
        url = sqlUrl;
        Properties props = new Properties();
        props.setProperty("user","fred");
        props.setProperty("password","secret");
        props.setProperty("ssl","true");
        try {
            Connection conn = DriverManager.getConnection(url, props);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next())
            {
                System.out.print("Column 1 returned ");
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void GetAllUsers ()

    public void InsertUser (User user) {
        
    }
}
