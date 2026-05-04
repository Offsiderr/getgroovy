package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.QuestionLoaderService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionLoaderServiceTest {

    @Test
    void loadEventPoolLoadsQuestionsForKnownTypes() {
        QuestionLoaderService service = new QuestionLoaderService();

        assertLoadedQuestions(service.loadEventPool("common"));
        assertLoadedQuestions(service.loadEventPool("local"));
        assertLoadedQuestions(service.loadEventPool("country"));
        assertLoadedQuestions(service.loadEventPool("world"));
    }

    @Test
    void loadEventPoolReturnsEmptyListForUnknownType() {
        ArrayList<Question> questions = new QuestionLoaderService().loadEventPool("unknown");

        assertTrue(questions.isEmpty());
    }

    private void assertLoadedQuestions(ArrayList<Question> questions) {
        assertFalse(questions.isEmpty());
        Question firstQuestion = questions.getFirst();
        assertNotNull(firstQuestion.getId());
        assertNotNull(firstQuestion.getPrompt());
        assertFalse(firstQuestion.getAnswers().isEmpty());
    }
}
