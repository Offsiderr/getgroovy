package seng201.team67.models.questionmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Question {

    //The structure is Question has answers (2-4) and Answers have outcomes.


    private final String id;

    private final String prompt;

    private final List<Answer> answers;

    @JsonCreator
    public Question(
            @JsonProperty("id") String id,
            @JsonProperty("prompt") String prompt,
            @JsonProperty("answers") List<Answer> answers) {
        this.id = id;
        this.prompt = prompt;
        this.answers = answers;
    }


    public String getId()
    {
        return id;
    }

    public String getPrompt()
    {
        return prompt;
    }

    public List<Answer> getAnswers()
    {
        return answers;
    }
}
