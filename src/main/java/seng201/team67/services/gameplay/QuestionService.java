package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.questionmodels.Question;

import java.util.List;

/**
 * Provides question operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class QuestionService {

    /**
     * Returns a random question.
     * @param gameEnvironment the active game environment
     * @param type the text value for the type
     * @return The question.
     */
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
