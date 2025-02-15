package databaseHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:h2:~/test"; 
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        initializeDatabase();
    }

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(255) UNIQUE NOT NULL, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "role VARCHAR(50) NOT NULL"
                    + ")";
            stmt.execute(createUsersTable);

            String createQuestionsTable = "CREATE TABLE IF NOT EXISTS questions ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "question_text TEXT NOT NULL, "
                    + "created_by VARCHAR(255) NOT NULL"
                    + ")";
            stmt.execute(createQuestionsTable);

            String createAnswersTable = "CREATE TABLE IF NOT EXISTS answers ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "question_id INT NOT NULL, "
                    + "answer_text TEXT NOT NULL, "
                    + "user_id INT NOT NULL, "
                    + "FOREIGN KEY (question_id) REFERENCES questions(id), "
                    + "FOREIGN KEY (user_id) REFERENCES users(id)"
                    + ")";
            stmt.execute(createAnswersTable);

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}