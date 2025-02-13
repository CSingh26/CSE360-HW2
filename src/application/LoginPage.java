package application;

import dao.UserDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage {

    public static void showLoginPage(Stage primaryStage) {
        primaryStage.setTitle("Login");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button backButton = new Button("Back");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            String role = UserDAO.validateUser(username, password);  
            if (role != null) {
                DashboardPage.showDashboard(primaryStage, username, role); 
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password!");
            }
        });

        backButton.setOnAction(e -> WelcomePage.showWelcomePage(primaryStage));

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, backButton);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 350, 250));
        primaryStage.show();
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}