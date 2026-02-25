/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author USER15
 */
public class config {
    //Connection Method to SQLITE
public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:tableDB.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }
public void addRecord(String sql, Object... values) {
    try (Connection conn = this.connectDB(); // Use the connectDB method
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}
public boolean authenticate(String sql, Object... values) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return true;
            }
        }
    } catch (SQLException e) {
        System.out.println("Login Error: " + e.getMessage());
    }
    return false;
}
public String getAccountRole(String sql, Object... values) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // This retrieves the 'account_type' column value from your database
                return rs.getString("account_type");
            }
        }
    } catch (SQLException e) {
        System.out.println("Role Retrieval Error: " + e.getMessage());
    }
    return "failed";
}
public String getAccountType(String sql, Object... values) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("type"); // Ensure your DB column is named "type"
            }
        }
    } catch (SQLException e) {
        System.out.println("Error fetching type: " + e.getMessage());
    }
    return null;
}
public ResultSet getData(String sql, Object... values) {
    try {
        Connection conn = connectDB();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }
        return pstmt.executeQuery();
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
        return null;
    }
}
public void deleteData(String sql) {
    try {
        Connection conn = connectDB(); // Use your existing connection method
        PreparedStatement pst = conn.prepareStatement(sql);
        int rowsDeleted = pst.executeUpdate();
        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(null, "User Deleted Successfully!");
        }
        pst.close();
        conn.close();
    } catch (SQLException ex) {
        System.out.println("Delete Error: " + ex.getMessage());
    }
}
public void insertData(String sql){
    try{
        // 1. Establish the connection link
        Connection conn = connectDB(); 
        
        // 2. Use the active 'conn' for the statement
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.executeUpdate();
        
        System.out.println("Inserted Successfully");
        pst.close();
        conn.close(); // 3. Always close to save changes to SQLite
    } catch(SQLException ex){
        System.out.println("Connection Error: " + ex);
    }
}
public void updateData(String sql){
    try {
        // 1. Establish the connection link
        Connection conn = connectDB(); 
        
        // 2. Use the active 'conn' to prepare the statement
        PreparedStatement pst = conn.prepareStatement(sql);
        int rowsUpdated = pst.executeUpdate();
        
        if(rowsUpdated > 0){
            System.out.println("Update Successful");
        }
        
        pst.close();
        conn.close(); // 3. Close the connection to save changes
    } catch(SQLException ex){
        System.out.println("Update Error: " + ex.getMessage());
    }
}
}
