package seng201.team67.gui.util;

import seng201.team67.interfaces.Usable;
import seng201.team67.models.items.ConditionalItem;
import seng201.team67.models.items.Item;

public class ItemDisplayFormatter {

    private ItemDisplayFormatter() {
    }

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
