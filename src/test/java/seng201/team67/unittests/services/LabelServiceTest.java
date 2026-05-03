package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.Popstar;
import seng201.team67.models.Rapper;
import seng201.team67.services.LabelService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LabelServiceTest {

    @Test
    void hireArtistDeductsMoneyMarksArtistOwnedAndAddsToRoster() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>());
        Artist artist = new Popstar("Taylor Swift", 2, "Pop");
        double startingMoney = service.getMoney();

        boolean hired = service.hireArtist(artist);

        assertTrue(hired);
        assertTrue(artist.owned);
        assertEquals(startingMoney - artist.getCost(), service.getMoney(), 0.0001);
        assertTrue(service.getAllArtists().contains(artist));
    }

    @Test
    void hireArtistReturnsFalseWhenThereIsNotEnoughMoney() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>());
        Artist expensiveArtist = new Popstar("Expensive", 100, "Pop");

        boolean hired = service.hireArtist(expensiveArtist);

        assertFalse(hired);
        assertFalse(expensiveArtist.owned);
        assertFalse(service.getAllArtists().contains(expensiveArtist));
    }

    @Test
    void buyItemDeductsCreditsWhenAffordable() {
        LabelService service = createLabelService(createConfiguredEnvironment(), new ArrayList<>());
        double startingMoney = service.getMoney();

        boolean result = service.buyItem(50);

        assertTrue(result);
        assertEquals(startingMoney - 50, service.getMoney(), 0.0001);
    }

    @Test
    void lineupStarPowerHelpersReturnExpectedValues() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        LabelService service = createLabelService(gameEnvironment, List.of(
                new Popstar("One", 1, "Pop"),
                new Rapper("Two", 3, "Rap")
        ));

        assertEquals(2.0, service.getAverageSP(), 0.0001);
        assertEquals(3.0, service.getMaxSP(), 0.0001);
        assertEquals(1.0, service.getMinSP(), 0.0001);
    }

    @Test
    void applyStaminaChangeToLineupArtistRoundsAndUpdatesTargetArtist() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Artist artist = new Rapper("Target", 2, "Rap");
        LabelService service = createLabelService(gameEnvironment, List.of(artist));

        service.applyStaminaChangeToLineupArtist(0, -4.6);

        assertEquals(115, artist.getStamina());
    }

    @Test
    void retireArtistRemovesArtistFromLabel() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Artist artist = new Popstar("Retire Me", 1, "Pop");
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>(List.of(artist)));

        service.retireArtist(artist);

        assertFalse(service.getAllArtists().contains(artist));
        assertFalse(service.getLineup().contains(artist));
    }

    private GameEnvironment createConfiguredEnvironment() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        gameEnvironment.setDifficulty(0);
        return gameEnvironment;
    }

    private LabelService createLabelService(GameEnvironment gameEnvironment, List<Artist> artists) {
        LabelService service = new LabelService(gameEnvironment);
        service.setLabel(new Label("Test Label", artists, gameEnvironment));
        return service;
    }
}
