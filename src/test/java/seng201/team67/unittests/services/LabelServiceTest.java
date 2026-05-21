package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.models.enums.ItemEffects;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.items.CosumableItem;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.management.LabelService;
import seng201.team67.services.setup.DifficultyService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LabelServiceTest {

    private static final int ALL_ARTISTS_LIMIT = 5;

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
        assertEquals(artist.getCost(), gameEnvironment.getTotalMoneySpent(), 0.0001);
        assertTrue(service.getAllArtists().contains(artist));
    }

    @Test
    void hireArtistReturnsFalseWhenThereIsNotEnoughMoney() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>());
        Artist expensiveArtist = new Popstar("Expensive", 5, "Pop");
        service.takeMoney(service.getMoney());

        boolean hired = service.hireArtist(expensiveArtist);

        assertFalse(hired);
        assertFalse(expensiveArtist.owned);
        assertFalse(service.getAllArtists().contains(expensiveArtist));
    }

    @Test
    void hireArtistReturnsFalseWhenAllArtistsPoolAlreadyHasFiveArtists() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        List<Artist> roster = new ArrayList<>();
        for (int i = 0; i < ALL_ARTISTS_LIMIT; i++) {
            roster.add(new Popstar("Artist " + i, 1, "Pop"));
        }
        LabelService service = createLabelService(gameEnvironment, roster);
        Artist extraArtist = new Rapper("Overflow", 2, "Rap");

        boolean hired = service.hireArtist(extraArtist);

        assertFalse(hired);
        assertFalse(extraArtist.owned);
        assertEquals(ALL_ARTISTS_LIMIT, service.getAllArtists().size());
    }

    @Test
    void buyItemDeductsCreditsWhenAffordable() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>());
        double startingMoney = service.getMoney();

        boolean result = service.buyItem(50);

        assertTrue(result);
        assertEquals(startingMoney - 50, service.getMoney(), 0.0001);
        assertEquals(50.0, gameEnvironment.getTotalMoneySpent(), 0.0001);
    }

    @Test
    void buyItemAddsPurchasedItemToRosterItems() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>());
        Item item = new EquippedItem(
                "Lucky Microphone",
                "Boosts confidence on stage",
                100,
                Rarity.RARE,
                List.of(ItemEffects.STAR_FUELLED)
        );
        double startingMoney = service.getMoney();

        boolean result = service.buyItem(item);

        assertTrue(result);
        assertTrue(item.getOwned());
        assertTrue(service.getAllItems().contains(item));
        assertEquals(1, service.getAllItems().size());
        assertEquals(startingMoney - item.getCost(), service.getMoney(), 0.0001);
        assertEquals(item.getCost(), gameEnvironment.getTotalMoneySpent(), 0.0001);
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

        assertEquals(80.0, easyService.getLineupTotalPay(), 0.0001);
        assertEquals(120.0, hardService.getLineupTotalPay(), 0.0001);
    }

    @Test
    void lineupTotalPayUsesTourArtistPayMultiplier() {
        List<Artist> artists = List.of(
                new Popstar("One", 1, "Pop"),
                new Rapper("Two", 3, "Rap")
        );
        LabelService service = createLabelService(createConfiguredEnvironment(0), artists);

        assertEquals(80.0, service.getLineupTotalPay(TourType.LOCAL), 0.0001);
        assertEquals(120.0, service.getLineupTotalPay(TourType.COUNTRY), 0.0001);
        assertEquals(160.0, service.getLineupTotalPay(TourType.WORLD), 0.0001);
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
                List.of(ItemEffects.STAR_FUELLED)
        );
        service.buyItem(item, 0);

        boolean equipped = service.equipItem(artist, item);

        assertTrue(equipped);
        assertTrue(artist.getItems().contains(item));
        assertFalse(service.getAllItems().contains(item));
    }

    @Test
    void unequipItemMovesEquippedItemBackToInventory() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Artist artist = new Popstar("Equipped Artist", 1, "Pop");
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>(List.of(artist)));
        Item item = new EquippedItem(
                "Lucky Microphone",
                "Boosts confidence on stage",
                100,
                Rarity.RARE,
                List.of(ItemEffects.STAR_FUELLED)
        );
        service.buyItem(item, 0);
        service.equipItem(artist, item);

        boolean unequipped = service.unequipItem(artist, item);

        assertTrue(unequipped);
        assertFalse(artist.getItems().contains(item));
        assertTrue(service.getAllItems().contains(item));
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
                List.of(ItemEffects.SECOND_WIND)
        );
        service.buyItem(item, 0);
        service.equipItem(artist, item);

        String result = service.useConsumable(artist, item);

        assertFalse(result.isBlank());
        assertEquals(5, artist.getStarPower());
        assertEquals(0, item.getUses());
        assertFalse(artist.getItems().contains(item));
    }

    @Test
    void useConsumableUsesItemMultiplierWhenApplyingEffect() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Artist artist = new Rockstar("Boosted Artist", 3, "Rock");
        artist.setStamina(50);
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>(List.of(artist)));
        CosumableItem item = new CosumableItem(
                "Energy Drink",
                "A quick backstage boost",
                1,
                100,
                Rarity.COMMON,
                List.of(ItemEffects.STAMINA_BOOST)
        );
        item.setMultiplier(15.0);
        service.buyItem(item, 0);
        service.equipItem(artist, item);

        service.useConsumable(artist, item);

        assertEquals(65, artist.getStamina());
    }

    @Test
    void useConsumableAppliesStarPowerBoostEffect() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Artist artist = new Popstar("Confident Artist", 2, "Pop");
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>(List.of(artist)));
        CosumableItem item = new CosumableItem(
                "Pep Talk",
                "A quick confidence boost",
                1,
                100,
                Rarity.RARE,
                List.of(ItemEffects.STAR_POWER_BOOST)
        );
        item.setMultiplier(2.0);
        service.buyItem(item, 0);
        service.equipItem(artist, item);

        service.useConsumable(artist, item);

        assertEquals(4, artist.getStarPower());
    }

    @Test
    void getItemSellPriceReturnsConfiguredRateForUnusedItem() {
        LabelService service = createLabelService(createConfiguredEnvironment(), new ArrayList<>());
        Item item = new EquippedItem(
                "Lucky Microphone",
                "Boosts confidence on stage",
                100,
                Rarity.RARE,
                List.of(ItemEffects.STAR_FUELLED)
        );

        assertEquals(70, service.getItemSellPrice(item));
    }

    @Test
    void getItemSellPriceScalesWithRemainingConsumableUses() {
        LabelService service = createLabelService(createConfiguredEnvironment(), new ArrayList<>());
        CosumableItem item = new CosumableItem(
                "Energy Drink",
                "A quick backstage boost",
                3,
                300,
                Rarity.COMMON,
                List.of(ItemEffects.SECOND_WIND)
        );

        item.consumeUse();
        assertEquals(140, service.getItemSellPrice(item));

        item.consumeUse();
        assertEquals(70, service.getItemSellPrice(item));
    }

    @Test
    void sellItemAddsCreditsAndRemovesItemFromInventory() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>());
        Item item = new EquippedItem(
                "Lucky Microphone",
                "Boosts confidence on stage",
                100,
                Rarity.RARE,
                List.of(ItemEffects.STAR_FUELLED)
        );
        service.buyItem(item, 0);
        double startingMoney = service.getMoney();

        boolean sold = service.sellItem(item);

        assertTrue(sold);
        assertEquals(startingMoney + 70, service.getMoney(), 0.0001);
        assertEquals(70.0, gameEnvironment.getTotalMoneyEarnt(), 0.0001);
        assertFalse(service.getAllItems().contains(item));
        assertFalse(item.getOwned());
    }

    @Test
    void directMoneyAdjustmentsUpdateTrackedTotals() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        LabelService service = createLabelService(gameEnvironment, new ArrayList<>());

        service.takeMoney(40);
        service.giveMoney(90);

        assertEquals(40.0, gameEnvironment.getTotalMoneySpent(), 0.0001);
        assertEquals(90.0, gameEnvironment.getTotalMoneyEarnt(), 0.0001);
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
