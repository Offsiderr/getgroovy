package seng201.team67.models.questionmodels;

import java.util.List;

public class Answer {

    private final String label;

    private final List<Outcome> outcomes;

    public Answer(String label, List<Outcome> outcomes)
    {
        this.label = label;
        this.outcomes = outcomes;
    }

    public String getLabel()
    {
        return label;
    }

    public List<Outcome> getOutcomes()
    {
        return outcomes;
    }
}
