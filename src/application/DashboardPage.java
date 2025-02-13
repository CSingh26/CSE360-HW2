package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardPage {

    public static void showDashboard(Stage primaryStage, String username, String role) {
        primaryStage.setTitle("Dashboard - " + username);

        TabPane tabPane = new TabPane();

        Tab questionsTab = new Tab("My Questions", QuestionFormPage.getQuestionsView(primaryStage, username));

        // Tab for User Answers
        Tab answersTab = new Tab("My Answers", AnswerFormPage.getAnswersView(primaryStage, username));

        tabPane.getTabs().addAll(questionsTab, answersTab);

        if ("admin".equals(role)) {
            Tab adminTab = new Tab("Admin Controls", getAdminControls(primaryStage));
            tabPane.getTabs().add(adminTab);
        }

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> LoginPage.showLoginPage(primaryStage));

        VBox layout = new VBox(10, tabPane, logoutBtn);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 500, 400));
        primaryStage.show();
    }

    private static VBox getAdminControls(Stage primaryStage) {
        Label adminLabel = new Label("Admin Controls:");
        Button deleteUserBtn = new Button("Delete User");
        Button deleteQuestionBtn = new Button("Delete Any Question");
        Button deleteAnswerBtn = new Button("Delete Any Answer");

        deleteUserBtn.setOnAction(e -> openDeleteUserPage(primaryStage));
        deleteQuestionBtn.setOnAction(e -> openDeleteQuestionPage(primaryStage));
        deleteAnswerBtn.setOnAction(e -> openDeleteAnswerPage(primaryStage));

        return new VBox(10, adminLabel, deleteUserBtn, deleteQuestionBtn, deleteAnswerBtn);
    }

    private static void openDeleteUserPage(Stage primaryStage) {
        System.out.println("Admin - Delete User Page");
    }

    private static void openDeleteQuestionPage(Stage primaryStage) {
        System.out.println("Admin - Delete Question Page");
    }

    private static void openDeleteAnswerPage(Stage primaryStage) {
        System.out.println("Admin - Delete Answer Page");
    }
}