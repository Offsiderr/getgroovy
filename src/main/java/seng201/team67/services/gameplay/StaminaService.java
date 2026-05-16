package seng201.team67.services.gameplay;

import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.questions.OutcomeType;

public class StaminaService {

    public double getStaminaChangeAmount(GameEnvironment gameEnvironment, OutcomeType outcomeType)
    {
        return gameEnvironment.getStaminaTier().resolve(outcomeType);
    }
}
