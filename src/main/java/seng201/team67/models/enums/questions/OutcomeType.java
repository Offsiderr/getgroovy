package seng201.team67.models.enums.questions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the available outcome type values used by the game. All outcomes belong to one of these types
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum OutcomeType {
    /** The great. */
    @JsonProperty("great") GREAT("Great"),
    /** The good. */
    @JsonProperty("good") GOOD("Good"),
    /** The ok. */
    @JsonProperty("ok") OK("Ok"),
    /** The none. */
    @JsonProperty("none") NONE("None"),
    /** The bad. */
    @JsonProperty("bad") BAD("Bad"),
    /** The terrible. */
    @JsonProperty("terrible") TERRIBLE("Terrible");

    /** Text value for the display name. */
    private final String displayName;

    /**
     * Creates a new outcome type.
     * @param displayName the text value for the display name
     */
    OutcomeType(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Returns the display name.
     * @return The display name.
     */
    public String getDisplayName()
    {
        return displayName;
    }
}
