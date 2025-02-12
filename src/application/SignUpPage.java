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

        Label roleLabel = new Label("Role:");
        ChoiceBox<String> roleChoice = new ChoiceBox<>();
        roleChoice.getItems().addAll("admin", "user");
        roleChoice.setValue("user");

        Button signupButton = new Button("Sign Up");
        Button backButton = new Button("Back");

        //Signup Button Action
        signupButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleChoice.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields!");
                return;
            }

            if (!isValidPassword(password)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Password", 
                          "Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character.");
                return;
            }

            UserDAO.addUser(username, password, role);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Signup Successful!");
            WelcomePage.showWelcomePage(primaryStage);
        });

        //Back Button Action
        backButton.setOnAction(e -> WelcomePage.showWelcomePage(primaryStage));

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, roleLabel, roleChoice, signupButton, backButton);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 350, 300));
        primaryStage.show();
    }

    //Password Validation Method
    private static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    //Show Alerts for Validation Messages
    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}