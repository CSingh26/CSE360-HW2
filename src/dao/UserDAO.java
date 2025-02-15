package dao;

import databaseHelper.DatabaseHelper;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDAO {

    static {
        DatabaseHelper.initializeDatabase();
    }

    // Check if this is the first user (to assign admin role)
    public static boolean isFirstUser() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if a username already exists
    public static boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add a new user (first user is admin)
    public static boolean addUser(String username, String password, String role) {
        if (isUsernameTaken(username)) {
            System.out.println("Username already exists. Cannot add user.");
            return false;
        }

        if (isFirstUser()) {
            role = "admin"; // First user gets admin role
        }

        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); // Store hashed password
            stmt.setString(3, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Validate user login credentials
    public static String validateUser(String username, String password) {
        String sql = "SELECT role, password FROM users WHERE username = ?";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    if (storedHashedPassword.equals(hashPassword(password))) {
                        return rs.getString("role"); // Return user role
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Invalid login
    }

    // Delete user (only admins can do this)
    public static boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hash password using SHA-256
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Return plain text if hashing fails (not recommended)
        }
    }
}