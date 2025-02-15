package dao;

import databaseHelper.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerDAO {

    // Fetch all answers submitted by the logged-in user (for "My Answers" tab)
    public static List<Map<String, String>> getUserAnswersWithQuestions(String username) {
        List<Map<String, String>> answers = new ArrayList<>();
        String sql = "SELECT a.answer_text, q.question_text, q.id AS question_id " +
                     "FROM answers a " +
                     "JOIN questions q ON a.question_id = q.id " +
                     "JOIN users u ON a.user_id = u.id " +
                     "WHERE u.username = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> answerData = new HashMap<>();
                answerData.put("answer", rs.getString("answer_text"));
                answerData.put("question", rs.getString("question_text"));
                answerData.put("question_id", String.valueOf(rs.getInt("question_id")));
                answers.add(answerData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    // Fetch all answers for a specific question
    public static List<String> getAnswersForQuestion(int questionId) {
        List<String> answers = new ArrayList<>();
        String sql = "SELECT answer_text FROM answers WHERE question_id = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                answers.add(rs.getString("answer_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    // Add an answer to a question
    public static boolean addAnswer(String questionText, String answer, String username) {
        String sql = "INSERT INTO answers (question_id, answer_text, user_id) VALUES " +
                     "((SELECT id FROM questions WHERE question_text = ?), ?, " +
                     "(SELECT id FROM users WHERE username = ?))";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, questionText);
            stmt.setString(2, answer);
            stmt.setString(3, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update an existing answer
    public static boolean updateAnswer(String oldAnswer, String newAnswer, String username) {
        String sql = "UPDATE answers " +
                     "SET answer_text = ? " +
                     "WHERE answer_text = ? " +
                     "AND user_id = (SELECT id FROM users WHERE username = ?)";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, newAnswer);
            stmt.setString(2, oldAnswer);
            stmt.setString(3, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete an answer
    public static boolean deleteAnswer(String answerText, String username) {
        String sql = "DELETE FROM answers " +
                     "WHERE answer_text = ? " +
                     "AND user_id = (SELECT id FROM users WHERE username = ?)";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, answerText);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<String> getUserAnswers(String username) {
        List<String> answers = new ArrayList<>();
        String sql = "SELECT answer_text FROM answers WHERE user_id = " +
                     "(SELECT id FROM users WHERE username = ?)";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
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