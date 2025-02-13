package dao;

import databaseHelper.DatabaseHelper;
import java.sql.*;

public class UserDAO {
    
    public static boolean isFirstUser() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection con = DatabaseHelper.getCon();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rslt = stmt.executeQuery()) {
            if (rslt.next()) {
                return rslt.getInt(1) == 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection con = DatabaseHelper.getCon();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rslt = stmt.executeQuery();
            if (rslt.next()) {
                return rslt.getInt(1) > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addUser(String username, String password, String role) {
        if (isUsernameTaken(username)) {
            System.out.println("Username already exists. Cannot add user.");
            return;
        }

        if (isFirstUser()) {
            role = "admin"; 
        }

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection con = DatabaseHelper.getCon();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static String validateUser(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection con = DatabaseHelper.getCon();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rslt = stmt.executeQuery();

            if (rslt.next()) {
                return rslt.getString("role"); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
}