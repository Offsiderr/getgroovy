package seng201.team67.interfaces;
/**
 * Defines behaviour for objects that are usable.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public interface Usable {

    /**
     * Returns the uses. How many times this object CAN be used.
     * @return The uses.
     */
    public int getUses();
    /**
     * Returns the use amount. How much this object HAS been used
     * @return The use amount.
     */
    public int getUseAmount();
}

