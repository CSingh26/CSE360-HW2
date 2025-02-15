package dao;

import databaseHelper.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    static {
        DatabaseHelper.initializeDatabase();
    }

    // Fetch all questions from all users (for "All Questions" tab)
    public static List<String> getAllQuestions() {
        List<String> questions = new ArrayList<>();
        String sql = "SELECT question_text FROM questions";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                questions.add(rs.getString("question_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Fetch all questions asked by a specific user (for "My Questions" tab)
    public static List<String> getUserQuestions(String username) {
        List<String> questions = new ArrayList<>();
        String sql = "SELECT question_text FROM questions WHERE created_by = ?";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(rs.getString("question_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Fetch question ID based on the question text
    public static int getQuestionId(String questionText) {
        String sql = "SELECT id FROM questions WHERE question_text = ?";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, questionText);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if the question is not found
    }

    // Add a new question to the database
    public static boolean addQuestion(String question, String username) {
        String sql = "INSERT INTO questions (question_text, created_by) VALUES (?, ?)";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, question);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;  
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update an existing question
    public static boolean updateQuestion(String oldContent, String newContent, String username) {
        String sql = "UPDATE questions SET question_text = ? WHERE question_text = ? AND created_by = ?";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, newContent);
            stmt.setString(2, oldContent);
            stmt.setString(3, username);
            return stmt.executeUpdate() > 0;  
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a question
    public static boolean deleteQuestion(String content, String username) {
        String sql = "DELETE FROM questions WHERE question_text = ? AND created_by = ?";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, content);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;  
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetch all answers for a given question (using question_id)
    public static List<String> getAnswersForQuestion(String questionText) {
        List<String> answers = new ArrayList<>();
        String sql = "SELECT a.answer_text FROM answers a " +
                     "JOIN questions q ON a.question_id = q.id " +
                     "WHERE q.question_text = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, questionText);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                answers.add(rs.getString("answer_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }
}