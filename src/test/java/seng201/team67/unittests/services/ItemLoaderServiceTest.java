package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.models.items.ConditionalItem;
import seng201.team67.models.items.CosumableItem;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;
import seng201.team67.services.ItemLoaderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemLoaderServiceTest {

    @Test
    void loadAllReturnsItemsFromJson() {
        List<Item> items = new ItemLoaderService().loadAll();

        assertFalse(items.isEmpty());
        assertEquals(3, items.size());
        assertTrue(items.stream().anyMatch(CosumableItem.class::isInstance));
        assertTrue(items.stream().anyMatch(EquippedItem.class::isInstance));
        assertTrue(items.stream().anyMatch(ConditionalItem.class::isInstance));
        assertTrue(items.stream().allMatch(item -> item.getName() != null && !item.getName().isBlank()));
        assertTrue(items.stream().allMatch(item -> item.getDescription() != null && !item.getDescription().isBlank()));
        assertTrue(items.stream().allMatch(item -> item.getCost() > 0));
        assertTrue(items.stream().allMatch(item -> item.getRarity() != null));
        assertTrue(items.stream().allMatch(item -> item.getEffects() != null && !item.getEffects().isEmpty()));
        assertTrue(items.stream().allMatch(item -> item.getImagePath() != null && !item.getImagePath().isBlank()));
        assertTrue(items.stream().allMatch(item -> item.getMultiplier() != null));

        CosumableItem consumableItem = (CosumableItem) items.stream()
                .filter(CosumableItem.class::isInstance)
                .findFirst()
                .orElseThrow();

        assertEquals(2, consumableItem.getUses());
        assertEquals(1, consumableItem.getUseAmount());
        assertEquals("Energy Drink", consumableItem.getName());
        assertTrue(consumableItem.getDescription().contains("backstage boost"));
        assertEquals(2, consumableItem.getEffects().size());
        assertNotNull(consumableItem);
    }
}
