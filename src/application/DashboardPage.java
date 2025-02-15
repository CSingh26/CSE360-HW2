package application;

import dao.UserDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardPage {

    public static void showDashboard(Stage primaryStage, String username, String role) {
        primaryStage.setTitle("Dashboard - " + username);

        TabPane tabPane = new TabPane();

        // My Questions Tab
        Tab questionsTab = new Tab("My Questions");
        questionsTab.setClosable(false);
        questionsTab.setContent(QuestionFormPage.getQuestionsView(primaryStage, username));

        // My Answers Tab
        Tab answersTab = new Tab("My Answers");
        answersTab.setClosable(false);
        answersTab.setContent(AnswerFormPage.getAnswersView(primaryStage, username));

        tabPane.getTabs().addAll(questionsTab, answersTab);

        // Admin Controls (Only for admin users)
        if ("admin".equals(role)) {
            Tab adminTab = new Tab("Admin Controls");
            adminTab.setClosable(false);
            adminTab.setContent(getAdminControls(primaryStage));
            tabPane.getTabs().add(adminTab);
        }

        // Logout Button
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> LoginPage.showLoginPage(primaryStage));

        VBox layout = new VBox(10, tabPane, logoutBtn);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 600, 500));
        primaryStage.show();
    }

    // Admin Controls Section
    private static VBox getAdminControls(Stage primaryStage) {
        Label adminLabel = new Label("Admin Controls:");
        adminLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Delete User Button (Only for Admin)
        Button deleteUserBtn = new Button("Delete User");

        deleteUserBtn.setOnAction(e -> openDeleteUserPage(primaryStage));

        return new VBox(15, adminLabel, deleteUserBtn);
    }

    // Admin - Delete User Functionality
    private static void openDeleteUserPage(Stage primaryStage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Enter username to delete:");
        dialog.setContentText("Username:");

        dialog.showAndWait().ifPresent(username -> {
            if (!username.isEmpty()) {
                boolean success = UserDAO.deleteUser(username);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User '" + username + "' deleted.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "User not found or cannot be deleted.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Username cannot be empty.");
            }
        });
    }

    // Show alert messages
    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}