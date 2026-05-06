package seng201.team67.models.items;

import seng201.team67.interfaces.Purchasable;
import seng201.team67.interfaces.Usable;

public class CosumableItem extends Item implements Usable {
    //Consumable items are not equiped, but used once as a temporary boost.

    private int concert_uses; //How many concerts this item will last for.

    public CosumableItem( int uses, int cost) {
        super(cost);
        this.concert_uses = uses;
    }

    @Override
    public int getUses() {
        return 0;
    }

    @Override
    public int getUseAmount() {
        return 0;
    }
}
