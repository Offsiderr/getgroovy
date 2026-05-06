package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.models.enums.PayoutTier;
import seng201.team67.models.enums.PayoutType;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayoutTierTest {

    @Test
    void resolveReturnsConfiguredValuesForEachPayoutType() {
        assertAll(
                () -> assertEquals(300.0, PayoutTier.EASY.resolve(PayoutType.GREAT_PAYOUT)),
                () -> assertEquals(50.0, PayoutTier.MEDIUM.resolve(PayoutType.OK_PAYOUT)),
                () -> assertEquals(-100.0, PayoutTier.HARD.resolve(PayoutType.BAD_PENALTY)),
                () -> assertEquals(-200.0, PayoutTier.MEDIUM.resolve(PayoutType.TERRIBLE_PENALTY)),
                () -> assertEquals(0.0, PayoutTier.HARD.resolve(PayoutType.NONE))
        );
    }
}
