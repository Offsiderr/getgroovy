package seng201.team67.unittests.models.questionmodels;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import seng201.team67.models.enums.PayoutType;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.models.questionmodels.Question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class QuestionModelsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void questionModelsDeserializeNestedQuestionJson() throws Exception {
        String json = """
                {
                  "id": "common-1",
                  "prompt": "How do you handle a late soundcheck?",
                  "answers": [
                    {
                      "label": "Delay the set",
                      "outcomes": [
                        {
                          "weight": 3,
                          "description": "Fans stay patient.",
                          "payout": "okPayout",
                          "staminaChange": -5.5,
                          "crowdEnergyChange": 10,
                          "concertEnds": false
                        }
                      ]
                    }
                  ]
                }
                """;

        Question question = objectMapper.readValue(json, Question.class);
        Answer answer = question.getAnswers().getFirst();
        Outcome outcome = answer.getOutcomes().getFirst();

        assertAll(
                () -> assertEquals("common-1", question.getId()),
                () -> assertEquals("How do you handle a late soundcheck?", question.getPrompt()),
                () -> assertEquals(1, question.getAnswers().size()),
                () -> assertEquals("Delay the set", answer.getLabel()),
                () -> assertEquals(1, answer.getOutcomes().size()),
                () -> assertEquals(3, outcome.getWeight()),
                () -> assertEquals("Fans stay patient.", outcome.getDescription()),
                () -> assertEquals(PayoutType.OK_PAYOUT, outcome.getPayoutType()),
                () -> assertEquals(-5.5, outcome.getStaminaChange()),
                () -> assertEquals(10, outcome.getCrowdEnergyChange()),
                () -> assertFalse(outcome.getConcertEnds())
        );
    }
}
