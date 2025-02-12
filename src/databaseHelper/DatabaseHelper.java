package databaseHelper;

import java.sql.*;

public class DatabaseHelper {
	private static final String URL = "jdbc:h2:./database/hw2db";
	private static final String USER = "sa";
	private static final String PASSWORD = "";
	
	public static Connection getCon() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error connecting to the database");
		}
	}
	
	public static void dbSetup() {
		try (Connection conn = getCon();
			Statement st = conn.createStatement()) {
				
			//Creating user table
			String usrTable = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(100) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL, role VARCHAR(20) NOT NULL CHECK (role IN ('admin', 'user')))";
			
			//Creating questions table
			String quesTable = "CREATE TABLE IF NOT EXISTS questions (id INT AUTO_INCREMENT PRIMARY KEY, content VARCHAR(255) NOT NULL, author VARCHAR(100) NOT NULL, solved BOOLEAN DEFAULT FALSE)";
			
			//Creating Answer Table
			String answerTable = "CREATE TABLE IF NOT EXISTS ans (id INT AUTO_INCREMENT PRIMARY KEY, question_id INT NOT NULL, content VARCHAR(255) NOT NULL, author VARCHAR(255) NOT NULL, FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE)";
			
			st.execute(usrTable);
			st.execute(quesTable);
			st.execute(answerTable);
			
			System.out.println("Database is all set!!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

