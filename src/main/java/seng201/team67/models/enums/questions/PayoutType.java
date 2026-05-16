package seng201.team67.models.enums.questions;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum PayoutType {
    @JsonProperty("majorReward")
    @JsonAlias("greatPayout")
    MAJOR_REWARD,

    @JsonProperty("minorReward")
    @JsonAlias("okPayout")
    MINOR_REWARD,

    @JsonProperty("minorPenalty")
    @JsonAlias("badPenalty")
    MINOR_PENALTY,

    @JsonProperty("majorPenalty")
    @JsonAlias("terriblePenalty")
    MAJOR_PENALTY,

    @JsonProperty("none")
    NONE
}
