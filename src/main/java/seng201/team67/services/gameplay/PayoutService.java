package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.PayoutType;

public class PayoutService {

    public double getPayoutAmount(GameEnvironment gameEnvironment, PayoutType payoutType)
    {
        return gameEnvironment.getPayoutTier().resolve(payoutType);
    }
}
