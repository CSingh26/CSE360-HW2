package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomePage extends Application {

    @Override
    public void start(Stage primaryStage) {
        showWelcomePage(primaryStage);
    }

    public static void showWelcomePage(Stage primaryStage) {
        primaryStage.setTitle("Welcome to HW2 App of Chaitanya");

        Button signupBtn = new Button("Signup");
        Button loginBtn = new Button("Login");

        signupBtn.setOnAction(e -> openSignUpPage(primaryStage));
        loginBtn.setOnAction(e -> openLoginPage(primaryStage));

        VBox layout = new VBox(20);
        layout.getChildren().addAll(signupBtn, loginBtn);
        layout.setPadding(new Insets(40));
        layout.setSpacing(16);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void openSignUpPage(Stage primaryStage) {
        SignUpPage.showSignupPage(primaryStage); 
    }

    private static void openLoginPage(Stage primaryStage) {
        LoginPage.showLoginPage(primaryStage);  
    }

    public static void main(String[] args) {
        launch(args);
    }
}