package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.interfaces.Usable;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.items.Effect;

import java.util.List;

public class CosumableItem extends Item implements Usable {
    //Consumable items are not equiped, but used once as a temporary boost.

    private int concert_uses; //How many concerts this item will last for.

    public CosumableItem() {
        super();
    }

    @JsonCreator
    public CosumableItem(@JsonProperty("name") String name,
                         @JsonProperty("description") String description,
                         @JsonProperty("uses") int uses,
                         @JsonProperty("cost") int cost,
                         @JsonProperty("rarity") Rarity rarity,
                         @JsonProperty("effects") @JsonAlias("effect") List<Effect> effects) {
        super(name, description, cost, rarity, effects);
        this.concert_uses = uses;
    }

    @Override
    public int getUses() {
        return concert_uses;
    }

    @Override
    public int getUseAmount() {
        return 1;
    }

    public void consumeUse()
    {
        concert_uses = Math.max(0, concert_uses - getUseAmount());
    }

    public String getType()
    {
        return "Consumable";
    }
}
