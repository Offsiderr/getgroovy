package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;
import seng201.team67.models.GameConfig;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.models.enums.PayoutTier;

public class DifficultyService {

    public void applyDifficulty(GameEnvironment gameEnvironment, int difficultyIndex)
    {
        switch (difficultyIndex)
        {
            case 0 -> {
                gameEnvironment.setDifficulty(Difficulty.EASY);
                gameEnvironment.setGameConfig(GameConfig.easy());
                gameEnvironment.setPayoutTier(PayoutTier.EASY);
            }
            case 1 -> {
                gameEnvironment.setDifficulty(Difficulty.ACHALLENGE);
                gameEnvironment.setGameConfig(GameConfig.aChallenge());
                gameEnvironment.setPayoutTier(PayoutTier.MEDIUM);
            }
            case 2 -> {
                gameEnvironment.setDifficulty(Difficulty.HEARTLESS);
                gameEnvironment.setGameConfig(GameConfig.hard());
                gameEnvironment.setPayoutTier(PayoutTier.HARD);
            }
            default -> throw new IllegalArgumentException("Unknown difficulty index: " + difficultyIndex);
        }
    }
}
