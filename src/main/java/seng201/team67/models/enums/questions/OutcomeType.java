package seng201.team67.models.enums.questions;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OutcomeType {
    @JsonProperty("great") GREAT("Great"),
    @JsonProperty("good") GOOD("Good"),
    @JsonProperty("ok") OK("Ok"),
    @JsonProperty("none") NONE("None"),
    @JsonProperty("bad") BAD("Bad"),
    @JsonProperty("terrible") TERRIBLE("Terrible");

    private final String displayName;

    OutcomeType(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
