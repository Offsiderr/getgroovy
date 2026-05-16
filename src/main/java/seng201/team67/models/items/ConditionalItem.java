package seng201.team67.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.enums.ItemEffects;

import java.util.List;

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
                           @JsonProperty("effects") @JsonAlias("effect") List<ItemEffects> itemEffects) {
        super(name, description, cost, rarity, itemEffects);
    }

    public String getType()
    {
        return "Conditional";
    }

}
