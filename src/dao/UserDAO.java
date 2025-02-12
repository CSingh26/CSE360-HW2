package dao;

import databaseHelper.DatabaseHelper;
import model.User;

import java.sql.*;

public class UserDAO {
	public static void addUser(String username, String password, String role) {
		String sqlPrompt = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
		
		try (Connection con = DatabaseHelper.getCon();
			 PreparedStatement stmt = con.prepareStatement(sqlPrompt)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, role);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean validateUser(String username, String password) {
	    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
	    try (Connection con = DatabaseHelper.getCon();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, username);
	        stmt.setString(2, password);

	        ResultSet rslt = stmt.executeQuery();
	        if (rslt.next()) {
	            return true;  // User exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;  
	}
	
}