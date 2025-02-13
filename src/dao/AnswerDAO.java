package dao;

import databaseHelper.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO {

	public static List<String> getUserAnswers(String username) {
	    List<String> answers = new ArrayList<>();
	    String sql = "SELECT content FROM answers WHERE author = ?";
	    try (Connection con = DatabaseHelper.getCon();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setString(1, username);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            answers.add(rs.getString("content"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return answers;
	}

    public static void updateAnswer(String oldContent, String newContent, String username) {
        String sql = "UPDATE answers SET content = ? WHERE content = ? AND author = ?";
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

    public static void deleteAnswer(String content, String username) {
        String sql = "DELETE FROM answers WHERE content = ? AND author = ?";
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