package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        WelcomePage.showWelcomePage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}