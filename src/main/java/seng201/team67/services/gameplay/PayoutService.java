package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.questions.PayoutType;

/**
 * Provides payout operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class PayoutService {

    /**
     * Returns the payout amount.
     * @param gameEnvironment the active game environment
     * @param payoutType the payout type
     * @return The payout amount.
     */
    public double getPayoutAmount(GameEnvironment gameEnvironment, PayoutType payoutType)
    {
        return gameEnvironment.getPayoutTier().resolve(payoutType);
    }
}
