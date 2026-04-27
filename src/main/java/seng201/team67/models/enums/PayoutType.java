package seng201.team67.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PayoutType {
    @JsonProperty("greatPayout")     GREAT_PAYOUT,
    @JsonProperty("okPayout")        OK_PAYOUT,
    @JsonProperty("badPenalty")      BAD_PENALTY,
    @JsonProperty("terriblePenalty") TERRIBLE_PENALTY,
    @JsonProperty("none")            NONE
}
