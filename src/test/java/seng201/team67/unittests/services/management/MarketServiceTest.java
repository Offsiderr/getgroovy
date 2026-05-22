package seng201.team67.unittests.services.management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.enums.ItemEffects;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;
import seng201.team67.services.management.MarketService;
import seng201.team67.services.setup.DifficultyService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarketServiceTest {

    private GameEnvironment gameEnvironment;
    private MarketService marketService;

    @BeforeEach
    void setUp() {
        gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        gameEnvironment.setAllItems(List.of(
                item("Common One", Rarity.COMMON),
                item("Common Two", Rarity.COMMON),
                item("Rare One", Rarity.RARE),
                item("Very Rare One", Rarity.VERY_RARE)
        ));
        marketService = new MarketService(gameEnvironment);
    }

    @Test
    @DisplayName("Market item pool is cached after the first generation")
    void itemPoolIsCachedAfterFirstGeneration() {
        ArrayList<Item> firstPool = marketService.getItemPurchasePool();
        ArrayList<Item> secondPool = marketService.getItemPurchasePool();

        assertSame(gameEnvironment.getItemPurchasePool(), firstPool);
        assertEquals(firstPool, secondPool);
        assertTrue(gameEnvironment.isItemPoolGenerated());
    }

    @Test
    @DisplayName("Market reroll excludes items that are already owned")
    void rerolledPoolExcludesOwnedItems() {
        Item ownedItem = gameEnvironment.getAllItems().getFirst();
        ownedItem.purchase();
        gameEnvironment.setItemPoolGenerated(false);

        ArrayList<Item> pool = marketService.getItemPurchasePool();

        assertFalse(pool.contains(ownedItem));
        assertTrue(pool.stream().noneMatch(Item::getOwned));
    }

    private Item item(String name, Rarity rarity) {
        return new EquippedItem(name, "Market stock", 40, rarity, List.of(ItemEffects.STAR_FUELLED));
    }
}
