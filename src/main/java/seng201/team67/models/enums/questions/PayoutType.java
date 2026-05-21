package seng201.team67.models.enums.questions;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the available payout type values used by the game. Works with the PayoutTier to get the player a money
 * reward/punishment, and works with outcomes.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum PayoutType {
    /** The major reward. */
    @JsonProperty("majorReward")
    @JsonAlias("greatPayout")
    MAJOR_REWARD,

    /** The minor reward. */
    @JsonProperty("minorReward")
    @JsonAlias("okPayout")
    MINOR_REWARD,

    /** The minor penalty. */
    @JsonProperty("minorPenalty")
    @JsonAlias("badPenalty")
    MINOR_PENALTY,

    /** The major penalty. */
    @JsonProperty("majorPenalty")
    @JsonAlias("terriblePenalty")
    MAJOR_PENALTY,

    /** No payout */
    @JsonProperty("none")
    NONE
}
