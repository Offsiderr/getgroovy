package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.questions.OutcomeType;

/**
 * Provides stamina operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class StaminaService {

    /**
     * Returns the stamina change amount.
     * @param gameEnvironment the active game environment
     * @param outcomeType the outcome type
     * @return The stamina change amount.
     */
    public double getStaminaChangeAmount(GameEnvironment gameEnvironment, OutcomeType outcomeType)
    {
        return gameEnvironment.getStaminaTier().resolve(outcomeType);
    }
}
