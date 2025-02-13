package application;

import dao.UserDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignUpPage {

    public static void showSignupPage(Stage primaryStage) {
        primaryStage.setTitle("Sign Up");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button signupButton = new Button("Sign Up");
        Button backButton = new Button("Back");

        signupButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username cannot be empty!");
                return;
            }

            if (UserDAO.isUsernameTaken(username)) {
                showAlert(Alert.AlertType.ERROR, "Username Taken", 
                          "This username is already taken. Please choose another one.");
                return;
            }

            String passwordError = getPasswordError(password);
            if (!passwordError.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Password", passwordError);
                return;
            }

            String role = UserDAO.isFirstUser() ? "admin" : "user";
            UserDAO.addUser(username, password, role);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Signup Successful! Please log in.");
            LoginPage.showLoginPage(primaryStage);
        });

        backButton.setOnAction(e -> WelcomePage.showWelcomePage(primaryStage));

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, signupButton, backButton);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 350, 300));
        primaryStage.show();
    }

    private static String getPasswordError(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one number.";
        }
        if (!password.matches(".*[@$!%*?&].*")) {
            return "Password must contain at least one special character (@$!%*?&).";
        }
        return ""; 
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}