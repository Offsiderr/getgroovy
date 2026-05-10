package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.services.setup.SetupService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetupServiceTest {

    @Test
    void formIsValidWhenNameMatchesRulesAndDifficultyIsSelected()
    {
        SetupService setupService = new SetupService(new GameEnvironment());

        assertTrue(setupService.isFormValid("Studio 67", true));
    }

    @Test
    void formIsInvalidWhenNameBreaksRulesOrDifficultyIsMissing()
    {
        SetupService setupService = new SetupService(new GameEnvironment());

        assertFalse(setupService.isFormValid("AB", true));
        assertFalse(setupService.isFormValid("This Name Is Far Too Long", true));
        assertFalse(setupService.isFormValid("Bad@Name", true));
        assertFalse(setupService.isFormValid("Studio 67", false));
    }

    @Test
    void requireValidLabelNameTrimsWhitespaceBeforeStoring()
    {
        SetupService setupService = new SetupService(new GameEnvironment());

        assertEquals("Studio 67", setupService.requireValidLabelName("  Studio 67  "));
    }

    @Test
    void gameEnvironmentRejectsInvalidLabelNames()
    {
        GameEnvironment gameEnvironment = new GameEnvironment();

        assertThrows(IllegalArgumentException.class, () -> gameEnvironment.setLabelName("!!"));

        gameEnvironment.setLabelName("  Valid Name  ");
        assertEquals("Valid Name", gameEnvironment.getTempName());
    }
}
