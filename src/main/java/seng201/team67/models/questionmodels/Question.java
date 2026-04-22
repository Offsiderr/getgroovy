package seng201.team67.models.questionmodels;

import java.util.List;

public class Question {

    private final String id;

    private final String prompt;

    private final List<Answer> answers;

    public Question(String id, String prompt, List<Answer> answers)
    {
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
