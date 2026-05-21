package seng201.team67.gui.util;

import seng201.team67.interfaces.Usable;
import seng201.team67.models.items.ConditionalItem;
import seng201.team67.models.items.Item;

/**
 * Represents the item display formatter used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ItemDisplayFormatter {

    private ItemDisplayFormatter() {
    }

    /**
     * Returns the remaining uses text.
     * @param item the item involved in the operation
     * @return The remaining uses text.
     */
    public static String getRemainingUsesText(Item item) {
        if (item instanceof Usable usable) {
            return "Remaining uses: " + usable.getUses();
        }

        if (item instanceof ConditionalItem) {
            return "Remaining uses: Unlimited";
        }

        return "";
    }
}
