package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.questionmodels.Question;

import java.util.List;

public class QuestionService {

    public Question getQuestion(GameEnvironment gameEnvironment, String type)
    {
        return switch (type) {
            case "common" -> getRandomQuestion(gameEnvironment.getCommonQuestionPool());
            case "local" -> getRandomQuestion(gameEnvironment.getLocalQuestionPool());
            case "country" -> getRandomQuestion(gameEnvironment.getCountryQuestionPool());
            case "world" -> getRandomQuestion(gameEnvironment.getWorldQuestionPool());
            default -> throw new IllegalArgumentException("Unknown question type: " + type);
        };
    }

    private Question getRandomQuestion(List<Question> questions)
    {
        return questions.get((int) (Math.random() * questions.size()));
    }
}
