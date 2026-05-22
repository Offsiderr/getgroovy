package seng201.team67.services.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.questions.PayoutType;
import seng201.team67.services.setup.DifficultyService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PayoutServiceTest {

    private GameEnvironment gameEnvironment;
    private PayoutService payoutService;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        payoutService = new PayoutService();
    }

    @Test
    @DisplayName("Payout service resolves the current difficulty payout tier")
    void resolvesConfiguredPayoutTier() {
        assertEquals(300, payoutService.getPayoutAmount(gameEnvironment, PayoutType.MAJOR_REWARD), 0.0001);
        assertEquals(-100, payoutService.getPayoutAmount(gameEnvironment, PayoutType.MAJOR_PENALTY), 0.0001);
    }
}
