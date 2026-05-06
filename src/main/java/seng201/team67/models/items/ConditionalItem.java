package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.items.Effect;

public class ConditionalItem extends EquippedItem {

    //Conditional items are context sensitive. eg. you get a bonus if you end a concert with 2 artist's combined stamina over 150

    public ConditionalItem() {
        super();
    }

    @JsonCreator
    public ConditionalItem(@JsonProperty("name") String name,
                           @JsonProperty("description") String description,
                           @JsonProperty("cost") int cost,
                           @JsonProperty("rarity") Rarity rarity,
                           @JsonProperty("effect") Effect effect) {
        super(name, description, cost, rarity, effect);
    }



}
