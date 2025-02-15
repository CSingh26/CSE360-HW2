package application;

import dao.AnswerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerFormPage {

    public static VBox getAnswersView(Stage primaryStage, String username) {
        List<Map<String, String>> userAnswers = AnswerDAO.getUserAnswersWithQuestions(username);
        ObservableList<String> answerList = FXCollections.observableArrayList();

        // Store question-answer mapping for reference
        Map<String, Integer> answerToQuestionMap = new HashMap<>();

        for (Map<String, String> entry : userAnswers) {
            String questionText = entry.get("question");
            String answerText = entry.get("answer");
            String displayText = "Q: " + questionText + "\nA: " + answerText;

            answerList.add(displayText);
            answerToQuestionMap.put(displayText, Integer.parseInt(entry.get("question_id")));
        }

        ListView<String> answerView = new ListView<>(answerList);

        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        // Edit Answer
        editButton.setOnAction(e -> {
            String selectedAnswer = answerView.getSelectionModel().getSelectedItem();
            if (selectedAnswer != null) {
                String[] parts = selectedAnswer.split("\nA: ");
                if (parts.length < 2) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid answer format.");
                    return;
                }

                String oldAnswer = parts[1];
                TextInputDialog dialog = new TextInputDialog(oldAnswer);
                dialog.setTitle("Edit Answer");
                dialog.setHeaderText("Edit your answer:");
                dialog.setContentText("New answer:");

                dialog.showAndWait().ifPresent(newAnswer -> {
                    if (!newAnswer.isEmpty()) {
                        int questionId = answerToQuestionMap.get(selectedAnswer);
                        AnswerDAO.updateAnswer(oldAnswer, newAnswer, username);
                        answerList.set(answerList.indexOf(selectedAnswer), "Q: " + parts[0] + "\nA: " + newAnswer);
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
                String[] parts = selectedAnswer.split("\nA: ");
                if (parts.length < 2) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid answer format.");
                    return;
                }

                String answerText = parts[1];
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this answer?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        int questionId = answerToQuestionMap.get(selectedAnswer);
                        AnswerDAO.deleteAnswer(answerText, username);
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