package application;

import dao.QuestionDAO;
import dao.AnswerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class QuestionFormPage {

    public static VBox getQuestionsView(Stage primaryStage, String username) {
        TabPane tabPane = new TabPane();

        // All Questions Tab
        Tab allQuestionsTab = new Tab("All Questions");
        allQuestionsTab.setClosable(false);
        allQuestionsTab.setContent(getAllQuestionsView(username));

        // My Questions Tab
        Tab myQuestionsTab = new Tab("My Questions");
        myQuestionsTab.setClosable(false);
        myQuestionsTab.setContent(getMyQuestionsView(username));

        // My Answers Tab
        Tab myAnswersTab = new Tab("My Answers");
        myAnswersTab.setClosable(false);
        myAnswersTab.setContent(getMyAnswersView(username));

        tabPane.getTabs().addAll(allQuestionsTab, myQuestionsTab, myAnswersTab);
        return new VBox(10, tabPane);
    }

    // Display all questions with an option to answer
    private static VBox getAllQuestionsView(String username) {
        List<String> questions = QuestionDAO.getAllQuestions();
        ObservableList<String> questionList = FXCollections.observableArrayList(questions);
        ListView<String> questionView = new ListView<>(questionList);

        Button answerButton = new Button("Answer");

        answerButton.setOnAction(e -> {
            String selectedQuestion = questionView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Answer Question");
                dialog.setHeaderText("Enter your answer:");
                dialog.showAndWait().ifPresent(answer -> {
                    if (!answer.isEmpty()) {
                        AnswerDAO.addAnswer(selectedQuestion, answer, username);
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a question to answer.");
            }
        });

        return new VBox(10, questionView, answerButton);
    }

    // Display questions asked by the user and their answers
    private static VBox getMyQuestionsView(String username) {
        List<String> questions = QuestionDAO.getUserQuestions(username);
        ObservableList<String> questionList = FXCollections.observableArrayList(questions);
        ListView<String> questionView = new ListView<>(questionList);

        Button addQuestionButton = new Button("Add Question");
        Button editQuestionButton = new Button("Edit Question");  // New button
        Button viewAnswersButton = new Button("View Answers");

        // Add Question Button Functionality
        addQuestionButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Question");
            dialog.setHeaderText("Enter your question:");
            dialog.showAndWait().ifPresent(question -> {
                if (!question.isEmpty()) {
                    if (QuestionDAO.addQuestion(question, username)) {
                        questionList.add(question); // Update the UI dynamically
                    }
                }
            });
        });

        // Edit Question Button Functionality
        editQuestionButton.setOnAction(e -> {
            String selectedQuestion = questionView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                TextInputDialog dialog = new TextInputDialog(selectedQuestion);
                dialog.setTitle("Edit Question");
                dialog.setHeaderText("Modify your question:");
                dialog.setContentText("New question:");

                dialog.showAndWait().ifPresent(newQuestion -> {
                    if (!newQuestion.isEmpty()) {
                        if (QuestionDAO.updateQuestion(selectedQuestion, newQuestion, username)) {
                            questionList.set(questionList.indexOf(selectedQuestion), newQuestion);
                        }
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a question to edit.");
            }
        });

        // View Answers Button Functionality
        viewAnswersButton.setOnAction(e -> {
            String selectedQuestion = questionView.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                List<String> answers = QuestionDAO.getAnswersForQuestion(selectedQuestion);
                showAlert(Alert.AlertType.INFORMATION, "Answers", String.join("\n", answers));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a question to view answers.");
            }
        });

        return new VBox(10, questionView, addQuestionButton, editQuestionButton, viewAnswersButton);
    }

    // Display answers submitted by the user
    private static VBox getMyAnswersView(String username) {
        List<String> answers = AnswerDAO.getUserAnswers(username);
        ObservableList<String> answerList = FXCollections.observableArrayList(answers);
        ListView<String> answerView = new ListView<>(answerList);
        return new VBox(10, answerView);
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}