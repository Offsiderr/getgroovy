package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.items.Effect;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;
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
    void buyItemAddsPurchasedItemToRosterItems() {
        LabelService service = createLabelService(createConfiguredEnvironment(), new ArrayList<>());
        Item item = new EquippedItem(
                "Lucky Microphone",
                "Boosts confidence on stage",
                100,
                Rarity.RARE,
                List.of(Effect.STAR_FUELLED)
        );
        double startingMoney = service.getMoney();

        boolean result = service.buyItem(item);

        assertTrue(result);
        assertTrue(item.getOwned());
        assertTrue(service.getAllItems().contains(item));
        assertEquals(1, service.getAllItems().size());
        assertEquals(startingMoney - item.getCost(), service.getMoney(), 0.0001);
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
    void lineupTotalPayUsesDifficultyPayMultiplier() {
        List<Artist> artists = List.of(
                new Popstar("One", 1, "Pop"),
                new Rapper("Two", 3, "Rap")
        );
        LabelService easyService = createLabelService(createConfiguredEnvironment(0), artists);
        LabelService hardService = createLabelService(createConfiguredEnvironment(2), artists);

        assertEquals(20.0, easyService.getLineupTotalPay(), 0.0001);
        assertEquals(30.0, hardService.getLineupTotalPay(), 0.0001);
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

    @Test
    void equipItemMovesOwnedItemOntoArtistAndRemovesItFromInventory() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Artist artist = new Popstar("Equipped Artist", 1, "Pop");
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>(List.of(artist)));
        Item item = new EquippedItem(
                "Lucky Microphone",
                "Boosts confidence on stage",
                100,
                Rarity.RARE,
                List.of(Effect.STAR_FUELLED)
        );
        service.buyItem(item, 0);

        boolean equipped = service.equipItem(artist, item);

        assertTrue(equipped);
        assertTrue(artist.getItems().contains(item));
        assertFalse(service.getAllItems().contains(item));
    }

    private GameEnvironment createConfiguredEnvironment() {
        return createConfiguredEnvironment(0);
    }

    private GameEnvironment createConfiguredEnvironment(int difficultyIndex) {
        GameEnvironment gameEnvironment = new GameEnvironment();
        gameEnvironment.setDifficulty(difficultyIndex);
        return gameEnvironment;
    }

    private LabelService createLabelService(GameEnvironment gameEnvironment, List<Artist> artists) {
        LabelService service = new LabelService(gameEnvironment);
        service.setLabel(new Label("Test Label", artists, gameEnvironment));
        return service;
    }
}
