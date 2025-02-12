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

        //Login Button Action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (UserDAO.validateUser(username, password)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login Successful!", ButtonType.OK);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Credentials!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        //Back Button Action
        backButton.setOnAction(e -> WelcomePage.showWelcomePage(primaryStage));

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, backButton);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 300, 250));
        primaryStage.show();
    }
}