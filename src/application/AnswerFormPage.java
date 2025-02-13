package application;

import dao.AnswerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AnswerFormPage {

    public static VBox getAnswersView(Stage primaryStage, String username) {
        List<String> answers = AnswerDAO.getUserAnswers(username);
        ObservableList<String> answerList = FXCollections.observableArrayList(answers);
        ListView<String> answerView = new ListView<>(answerList);

        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        // Edit Answer
        editButton.setOnAction(e -> {
            String selectedAnswer = answerView.getSelectionModel().getSelectedItem();
            if (selectedAnswer != null) {
                TextInputDialog dialog = new TextInputDialog(selectedAnswer);
                dialog.setTitle("Edit Answer");
                dialog.setHeaderText("Edit your answer:");
                dialog.setContentText("New answer:");

                dialog.showAndWait().ifPresent(newAnswer -> {
                    if (!newAnswer.isEmpty()) {
                        AnswerDAO.updateAnswer(selectedAnswer, newAnswer, username);
                        answerList.set(answerList.indexOf(selectedAnswer), newAnswer);
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select an answer to edit.");
            }
        });

        // Delete Answer
        deleteButton.setOnAction(e -> {
            String selectedAnswer = answerView.getSelectionModel().getSelectedItem();
            if (selectedAnswer != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this answer?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        AnswerDAO.deleteAnswer(selectedAnswer, username);
                        answerList.remove(selectedAnswer);
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select an answer to delete.");
            }
        });

        return new VBox(10, answerView, editButton, deleteButton);
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}