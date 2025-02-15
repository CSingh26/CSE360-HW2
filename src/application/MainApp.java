package application;

import databaseHelper.DatabaseHelper;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseHelper.initializeDatabase();
            System.out.println("Database initialized successfully.");

            WelcomePage.showWelcomePage(primaryStage);
        } catch (Exception e) {
            System.err.println("Error initializing the application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}