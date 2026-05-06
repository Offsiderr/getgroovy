package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.items.Effect;

public class EquippedItem extends Item{
    //Equipped items are equiped to an artist

    public EquippedItem() {
        super();
    }

    @JsonCreator
    public EquippedItem(@JsonProperty("name") String name,
                        @JsonProperty("description") String description,
                        @JsonProperty("cost") int cost,
                        @JsonProperty("rarity") Rarity rarity,
                        @JsonProperty("effect") Effect effect) {
        super(name, description, cost, rarity, effect);
    }

}
