package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.models.enums.questions.PayoutTier;
import seng201.team67.models.enums.questions.PayoutType;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayoutTierTest {

    @Test
    void resolveReturnsConfiguredValuesForEachPayoutType() {
        assertAll(
                () -> assertEquals(300.0, PayoutTier.EASY.resolve(PayoutType.MAJOR_REWARD)),
                () -> assertEquals(50.0, PayoutTier.A_CHALLENGE.resolve(PayoutType.MINOR_REWARD)),
                () -> assertEquals(-100.0, PayoutTier.HEARTLESS.resolve(PayoutType.MINOR_PENALTY)),
                () -> assertEquals(-200.0, PayoutTier.A_CHALLENGE.resolve(PayoutType.MAJOR_PENALTY)),
                () -> assertEquals(0.0, PayoutTier.HEARTLESS.resolve(PayoutType.NONE))
        );
    }
}
