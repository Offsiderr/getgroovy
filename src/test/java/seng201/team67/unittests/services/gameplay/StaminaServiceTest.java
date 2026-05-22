package seng201.team67.unittests.services.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.questions.OutcomeType;
import seng201.team67.services.gameplay.StaminaService;
import seng201.team67.services.setup.DifficultyService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StaminaServiceTest {

    private GameEnvironment gameEnvironment;
    private StaminaService staminaService;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 1);
        staminaService = new StaminaService();
    }

    @Test
    @DisplayName("Stamina service resolves the current difficulty stamina tier")
    void resolvesConfiguredStaminaTier() {
        assertEquals(-10, staminaService.getStaminaChangeAmount(gameEnvironment, OutcomeType.GOOD), 0.0001);
        assertEquals(-25, staminaService.getStaminaChangeAmount(gameEnvironment, OutcomeType.TERRIBLE), 0.0001);
    }
}
