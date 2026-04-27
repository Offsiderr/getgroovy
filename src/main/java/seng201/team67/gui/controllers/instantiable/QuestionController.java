package seng201.team67.gui.controllers.instantiable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Question;

import java.util.List;
import java.util.function.Consumer;

public class QuestionController {

    //TODO: impltement .setManaged to allow collapsing of layout space in the future.

    private final Question question;

    @FXML private Label questionTitle;

    @FXML private Button answerOne;
    @FXML private Button answerTwo;
    @FXML private Button answerThree;
    @FXML private Button answerFour;

    private final Consumer<Answer> onAnswer;


    public QuestionController(Question question, Consumer<Answer> onAnswer)
    {
        this.question = question;
        this.onAnswer = onAnswer;
    }

    @FXML private void initialize()
    {
        List<Button> answerButtons = List.of(answerOne, answerTwo, answerThree, answerFour);

        for(Button button : answerButtons)
        {
            button.setVisible(false);
        }
        questionTitle.setText(question.getPrompt());

        setAnswers(answerButtons);
    }

    @FXML private void setAnswers(List<Button> buttons)
    {
        List<Answer> answers = question.getAnswers();

        for (int i = 0; i < buttons.size(); i++) {
            Button btn = buttons.get(i);
            if (i < answers.size()) {
                Answer choice = answers.get(i);
                btn.setText(choice.getLabel());
                btn.setVisible(true);
                btn.setOnAction(e -> onAnswer.accept(choice));
            } else {
                btn.setVisible(false);
                btn.setManaged(false);  // collapses layout space too
            }
        }
    }
}

