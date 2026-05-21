package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;
import seng201.team67.models.GameConfig;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.models.enums.questions.PayoutTier;
import seng201.team67.models.enums.questions.StaminaTier;

/**
 * Provides difficulty operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class DifficultyService {

    /**
     * Applies the difficulty for all the enums that rely on difficulty to provide values.
     * @param gameEnvironment the active game environment
     * @param difficultyIndex the numeric value for the difficulty index
     */
    public void applyDifficulty(GameEnvironment gameEnvironment, int difficultyIndex)
    {
        switch (difficultyIndex)
        {
            case 0 -> {
                gameEnvironment.setDifficulty(Difficulty.EASY);
                gameEnvironment.setGameConfig(GameConfig.easy());
                gameEnvironment.setPayoutTier(PayoutTier.EASY);
                gameEnvironment.setStaminaTier(StaminaTier.EASY);
            }
            case 1 -> {
                gameEnvironment.setDifficulty(Difficulty.A_CHALLENGE);
                gameEnvironment.setGameConfig(GameConfig.aChallenge());
                gameEnvironment.setPayoutTier(PayoutTier.A_CHALLENGE);
                gameEnvironment.setStaminaTier(StaminaTier.A_CHALLENGE);
            }
            case 2 -> {
                gameEnvironment.setDifficulty(Difficulty.HEARTLESS);
                gameEnvironment.setGameConfig(GameConfig.hard());
                gameEnvironment.setPayoutTier(PayoutTier.HEARTLESS);
                gameEnvironment.setStaminaTier(StaminaTier.HEARTLESS);
            }
            default -> throw new IllegalArgumentException("Unknown difficulty index: " + difficultyIndex);
        }
    }
}
