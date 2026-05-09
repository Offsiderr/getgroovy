package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.items.Effect;
import seng201.team67.models.items.CosumableItem;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;
import seng201.team67.services.management.LabelService;
import seng201.team67.services.setup.DifficultyService;

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
        Artist artistTwo = new Rapper("Keep Me", 2, "Rap");
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>(List.of(artist, artistTwo)));

        boolean retired = service.retireArtist(artist);

        assertTrue(retired);
        assertFalse(service.getAllArtists().contains(artist));
        assertFalse(service.getLineup().contains(artist));
        assertTrue(service.getAllArtists().contains(artistTwo));
    }

    @Test
    void retireArtistReturnsFalseWhenArtistIsLastRemainingRosterMember() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Artist artist = new Popstar("Last One", 1, "Pop");
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>(List.of(artist)));

        boolean retired = service.retireArtist(artist);

        assertFalse(retired);
        assertTrue(service.getAllArtists().contains(artist));
        assertTrue(service.getLineup().contains(artist));
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

    @Test
    void useConsumableAppliesEffectsConsumesUseAndRemovesSpentItem() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Artist artist = new Rockstar("Consumable Artist", 3, "Rock");
        artist.setStamina(20);
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>(List.of(artist)));
        CosumableItem item = new CosumableItem(
                "Energy Drink",
                "A quick backstage boost",
                1,
                100,
                Rarity.COMMON,
                List.of(Effect.SECOND_WIND)
        );
        service.buyItem(item, 0);
        service.equipItem(artist, item);

        String result = service.useConsumable(artist, item);

        assertFalse(result.isBlank());
        assertEquals(6, artist.getStarPower());
        assertEquals(0, item.getUses());
        assertFalse(artist.getItems().contains(item));
    }

    private GameEnvironment createConfiguredEnvironment() {
        return createConfiguredEnvironment(0);
    }

    private GameEnvironment createConfiguredEnvironment(int difficultyIndex) {
        GameEnvironment gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, difficultyIndex);
        return gameEnvironment;
    }

    private LabelService createLabelService(GameEnvironment gameEnvironment, List<Artist> artists) {
        LabelService service = new LabelService(gameEnvironment);
        gameEnvironment.setLabel(new Label("Test Label", artists, gameEnvironment));
        return service;
    }
}
