package seng201.team67.models.enums.items;

/**
 * Represents the available item type values used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public enum ItemType {
    /** The consumable item type. This means it can be used a limited amount of times by the artist but whenever they'd like during a concert */
    CONSUMABLE,
    /** The equipped item type. This means that it can be attached to a artist and can be used an unlimited amount of times */
    EQUIPPED,
    /** The conditional. This means that it is equipped to an artist and is triggered whenever the item's conditions are met */
    CONDITIONAL
}
