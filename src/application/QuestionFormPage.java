package application;

import dao.QuestionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class QuestionFormPage {

    public static VBox getQuestionsView(Stage primaryStage, String username) {
        List<String> questions = QuestionDAO.getUserQuestions(username);
        ObservableList<String> questionList = FXCollections.observableArrayList(questions);
        ListView<String> questionView = new ListView<>(questionList);

        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        // Edit Question
        editButton.setOnAction(e -> {
            String selectedQuestion = questionView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                TextInputDialog dialog = new TextInputDialog(selectedQuestion);
                dialog.setTitle("Edit Question");
                dialog.setHeaderText("Edit your question:");
                dialog.setContentText("New question:");

                dialog.showAndWait().ifPresent(newQuestion -> {
                    if (!newQuestion.isEmpty()) {
                        QuestionDAO.updateQuestion(selectedQuestion, newQuestion, username);
                        questionList.set(questionList.indexOf(selectedQuestion), newQuestion);
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a question to edit.");
            }
        });

        // Delete Question
        deleteButton.setOnAction(e -> {
            String selectedQuestion = questionView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this question? All answers will be deleted too.", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        QuestionDAO.deleteQuestion(selectedQuestion, username);
                        questionList.remove(selectedQuestion);
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a question to delete.");
            }
        });

        return new VBox(10, questionView, editButton, deleteButton);
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}