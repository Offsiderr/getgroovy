package seng201.team67.models.questionmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the answer used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class Answer {

    /** Text value for the label. */
    private final String label;

    /** Collection that stores the outcomes. */
    private final List<Outcome> outcomes;


    /**
     * Creates a new answer.
     * @param label the text value for the label
     * @param outcomes the list of outcomes
     */
    @JsonCreator
    public Answer(
            @JsonProperty("label") String label,
            @JsonProperty("outcomes") List<Outcome> outcomes) {
        this.label = label;
        this.outcomes = outcomes;
    }

    /**
     * Returns the label.
     * @return The label.
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * Returns the outcomes.
     * @return The outcomes.
     */
    public List<Outcome> getOutcomes()
    {
        return outcomes;
    }
}
