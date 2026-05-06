package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.models.minigames.MiniGameResult;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniGameResultTest {

    @Test
    void constructorStoresCrowdAndCreditResults() {
        MiniGameResult result = new MiniGameResult(12, -30);

        assertAll(
                () -> assertEquals(12, result.getCrowdMeterResult()),
                () -> assertEquals(-30, result.getCreditResult())
        );
    }
}
