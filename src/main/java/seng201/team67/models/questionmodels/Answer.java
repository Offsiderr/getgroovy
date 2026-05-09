package seng201.team67.models.questionmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Answer {

    private final String label;

    private final List<Outcome> outcomes;


    @JsonCreator
    public Answer(
            @JsonProperty("label") String label,
            @JsonProperty("outcomes") List<Outcome> outcomes) {
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
