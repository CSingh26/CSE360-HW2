package dao;

import databaseHelper.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

	public static List<String> getUserQuestions(String username) {
	    List<String> questions = new ArrayList<>();
	    String sql = "SELECT content FROM questions WHERE author = ?";
	    try (Connection con = DatabaseHelper.getCon();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, username);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            questions.add(rs.getString("content"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return questions;
	}

    public static void updateQuestion(String oldContent, String newContent, String username) {
        String sql = "UPDATE questions SET content = ? WHERE content = ? AND author = ?";
        try (Connection con = DatabaseHelper.getCon();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, newContent);
            stmt.setString(2, oldContent);
            stmt.setString(3, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteQuestion(String content, String username) {
        String sql = "DELETE FROM questions WHERE content = ? AND author = ?";
        try (Connection con = DatabaseHelper.getCon();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, content);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}