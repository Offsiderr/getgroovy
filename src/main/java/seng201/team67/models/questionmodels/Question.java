package seng201.team67.models.questionmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the question used by the game. Questions contain answers (2-4) and answers contain outcomes
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class Question {

    /** Text value for the id. */
    private final String id;

    /** Text value for the prompt. */
    private final String prompt;

    /** Collection that stores the answers. */
    private final List<Answer> answers;

    /**
     * Creates a new question.
     * @param id the text value for the id
     * @param prompt the text value for the prompt
     * @param answers the list of answers
     */
    @JsonCreator
    public Question(
            @JsonProperty("id") String id,
            @JsonProperty("prompt") String prompt,
            @JsonProperty("answers") List<Answer> answers) {
        this.id = id;
        this.prompt = prompt;
        this.answers = answers;
    }


    /**
     * Returns the id.
     * @return The id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Returns the prompt.
     * @return The prompt.
     */
    public String getPrompt()
    {
        return prompt;
    }

    /**
     * Returns the answers.
     * @return The answers.
     */
    public List<Answer> getAnswers()
    {
        return answers;
    }
}
