package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.items.Effect;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;
import seng201.team67.services.management.MarketService;
import seng201.team67.services.setup.DifficultyService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MarketServiceTest {

    @Test
    void getItemPurchasePoolReturnsCachedPoolUntilRerolled() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        setMarketItems(gameEnvironment);
        MarketService marketService = new MarketService(gameEnvironment);

        ArrayList<Item> firstPool = marketService.getItemPurchasePool();
        ArrayList<Item> secondPool = marketService.getItemPurchasePool();

        assertEquals(firstPool, secondPool);
        assertTrue(gameEnvironment.isItemPoolGenerated());
    }

    @Test
    void rerollingAfterPurchaseBuildsANewPoolWithoutOwnedItem() {
        GameEnvironment gameEnvironment = createConfiguredEnvironment();
        Item commonOne = new EquippedItem("Common One", "Common item", 10, Rarity.COMMON, List.of(Effect.SECOND_WIND));
        Item commonTwo = new EquippedItem("Common Two", "Common item", 10, Rarity.COMMON, List.of(Effect.SECOND_WIND));
        Item rareItem = new EquippedItem("Rare One", "Rare item", 20, Rarity.RARE, List.of(Effect.STAR_FUELLED));
        Item veryRareItem = new EquippedItem("Very Rare One", "Very rare item", 30, Rarity.VERY_RARE, List.of(Effect.STAR_FUELLED));
        gameEnvironment.setAllItems(List.of(commonOne, commonTwo, rareItem, veryRareItem));
        MarketService marketService = new MarketService(gameEnvironment);

        ArrayList<Item> firstPool = marketService.getItemPurchasePool();
        Item purchasedItem = firstPool.stream()
                .filter(item -> item.getRarity() == Rarity.COMMON)
                .findFirst()
                .orElseThrow();
        Item remainingCommonItem = purchasedItem == commonOne ? commonTwo : commonOne;
        purchasedItem.purchase();
        gameEnvironment.removeItemFromPurchasePool(purchasedItem);

        assertFalse(gameEnvironment.getItemPurchasePool().contains(purchasedItem));

        gameEnvironment.setItemPoolGenerated(false);
        ArrayList<Item> rerolledPool = marketService.getItemPurchasePool();

        assertFalse(rerolledPool.contains(purchasedItem));
        assertTrue(rerolledPool.contains(remainingCommonItem));
        assertEquals(3, rerolledPool.size());
    }

    private GameEnvironment createConfiguredEnvironment() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        return gameEnvironment;
    }

    private void setMarketItems(GameEnvironment gameEnvironment) {
        Item commonItem = new EquippedItem("Common", "Common item", 10, Rarity.COMMON, List.of(Effect.SECOND_WIND));
        Item rareItem = new EquippedItem("Rare", "Rare item", 20, Rarity.RARE, List.of(Effect.STAR_FUELLED));
        Item veryRareItem = new EquippedItem("Very Rare", "Very rare item", 30, Rarity.VERY_RARE, List.of(Effect.STAR_FUELLED));
        gameEnvironment.setAllItems(new ArrayList<>(List.of(commonItem, rareItem, veryRareItem)));
    }
}
