package seng201.team67.services.management;

import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.enums.ItemEffects;
import seng201.team67.models.items.CosumableItem;
import seng201.team67.models.items.Item;
import seng201.team67.models.enums.TourType;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides label operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class LabelService {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;

    /**
     * Creates a new label service.
     * @param gameEnvironment the active game environment
     */
    public LabelService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Hires an artist, with a cost override
     * @param artist the artist
     * @param cost the numeric value for the cost
     * @return True if hire artist, otherwise false.
     */
    public boolean hireArtist(Artist artist, int cost)
    {
        Label label = gameEnvironment.getLabel();
        //with a cost override. This is used in gatchas as they don't cost anything to choose an artist.
        if (cost > label.getMoney())
        {
            return false;
        }
        else if (label.getAllArtists().size() >= label.getArtistsLimit())
        {
            return false;
        }

        if (!label.addArtistToAll(artist))
        {
            return false;
        }

        spendMoney(cost);
        artist.owned = true;
        return true;
    }

    /**
     * Hires an artist, based on the cost of the artist
     * It updates related state as needed while performing the operation.
     * @param artist the artist
     * @return True if hire artist, otherwise false.
     */
    public boolean hireArtist(Artist artist)
    {
        Label label = gameEnvironment.getLabel();
        if (artist.getCost() > label.getMoney())
        {
            return false;
        }
        else if (label.getAllArtists().size() >= label.getArtistsLimit())
        {
            return false;
        }

        if (!label.addArtistToAll(artist))
        {
            return false;
        }

        spendMoney(artist.getCost());
        artist.owned = true;
        return true;
    }

    /**
     * Buys an item, based on the cost. Just takes money away and doesn't add anything to the artist or label.
     * @param cost the numeric value for the cost
     * @return True if buy item, otherwise false.
     */
    public boolean buyItem(int cost)
    {
        Label label = gameEnvironment.getLabel();
        if(label.getMoney() < cost)
        {
            return false;
        }
        else
        {
            spendMoney(cost);
            return true;
        }
    }


    /**
     * Buys an item, using the cost override
     * It updates related state as needed while performing the operation.
     * @param item the item involved in the operation
     * @param cost the numeric value for the cost
     * @return True if buy item, otherwise false.
     */
    public boolean buyItem(Item item, int cost)
    {
        Label label = gameEnvironment.getLabel();
        if(label.getMoney() < cost)
        {
            return false;
        }


        if (!label.addItemToAll(item))
        {
            return false;
        }

        spendMoney(cost);
        item.purchase();
        return true;
    }

    /**
     * Buys an item, based on the cost of the item
     * It updates related state as needed while performing the operation.
     * @param item the item involved in the operation
     * @return True if buy item, otherwise false.
     */
    public boolean buyItem(Item item)
    {
        Label label = gameEnvironment.getLabel();
        if(label.getMoney() < item.getCost())
        {
            return false;
        }

        if (!label.addItemToAll(item))
        {
            return false;
        }

        spendMoney(item.getCost());
        item.purchase();
        return true;
    }

    /**
     * Sets the label's line up.
     * @param artist_lineup the list of artist lineup
     */
    public void setLineUp(List<Artist> artist_lineup)
    {
        gameEnvironment.getLabel().setLineUp(artist_lineup);
    }

    /**
     * Returns the label name.
     * @return The label name.
     */
    public String getLabelName()
    {
        return gameEnvironment.getLabel().getName();
    }

    /**
     * Returns the lineup.
     * @return The lineup.
     */
    public List<Artist> getLineup()
    {
        return gameEnvironment.getLabel().getLineUp();
    }

    /**
     * Returns the lineup limit.
     * @return The lineup limit.
     */
    public int getLineupLimit()
    {
        return gameEnvironment.getLabel().getLineUpLimit();
    }

    /**
     * Returns the all artists.
     * @return The all artists.
     */
    public List<Artist> getAllArtists()
    {
        return gameEnvironment.getLabel().getAllArtists();
    }

    /**
     * Returns the all items.
     * @return The all items.
     */
    public List<Item> getAllItems()
    {
        return gameEnvironment.getLabel().getItems();
    }

    /**
     * Returns the money.
     * @return The money.
     */
    public Double getMoney()
    {
        return gameEnvironment.getLabel().getMoney();
    }

    /**
     * Returns the item sell price.
     * @param item the item involved in the operation
     * @return The item sell price.
     */
    public int getItemSellPrice(Item item)
    {
        double sellbackRate = gameEnvironment.getConfig().itemSellbackRate;
        double remainingUseFraction = 1.0;

        if (item instanceof CosumableItem consumable)
        {
            int initialUses = Math.max(1, consumable.getInitialUses());
            remainingUseFraction = Math.max(0.0, (double) consumable.getUses() / initialUses);
        }

        return Math.max(0, (int) Math.round(item.getCost() * sellbackRate * remainingUseFraction));
    }

    /**
     * Processes the sell item.
     * It updates related state as needed while performing the operation.
     * @param item the item involved in the operation
     * @return True if sell item, otherwise false.
     */
    public boolean sellItem(Item item)
    {
        Label label = gameEnvironment.getLabel();
        if (!label.getItems().contains(item))
        {
            return false;
        }

        earnMoney(getItemSellPrice(item));
        label.removeItem(item);
        item.dispose();
        return true;
    }

    /**
     * Takes the money.
     * @param money the numeric value for the money
     */
    public void takeMoney(double money)
    {
        spendMoney(money);
    }

    /**
     * Gives the money.
     * @param money the numeric value for the money
     */
    public void giveMoney(double money)
    {
        earnMoney(money);
    }

    private void spendMoney(double money)
    {
        if (money <= 0)
        {
            return;
        }

        Label label = gameEnvironment.getLabel();
        label.money = label.getMoney() - money;
        gameEnvironment.addTotalMoneySpent(money);
    }

    private void earnMoney(double money)
    {
        if (money <= 0)
        {
            return;
        }

        Label label = gameEnvironment.getLabel();
        label.money = label.getMoney() + money;
        gameEnvironment.addTotalMoneyEarnt(money);
    }

    /**
     * Returns the lineup total pay.
     * @return The lineup total pay.
     */
    public double getLineupTotalPay()
    {
        return getLineupTotalPay(TourType.LOCAL);
    }

    /**
     * Returns the lineup total pay.
     * It derives the value from the current state before returning it.
     * @param tourType the tour type
     * @return The lineup total pay.
     */
    public double getLineupTotalPay(TourType tourType)
    {
        List<Artist> artists = gameEnvironment.getLabel().getLineUp();

        double totalCost = 0;
        double difficultyMultiplier = gameEnvironment.getDifficulty().getPayMultiplier();
        double tourMultiplier = gameEnvironment.getConfig().getArtistPayMultiplier(tourType);

        for (Artist artist : artists)
        {
            totalCost += artist.getPay() * difficultyMultiplier * tourMultiplier;
        }
        return totalCost;
    }

    /**
     * Returns the average sp.
     * It derives the value from the current state before returning it.
     * @return The average sp.
     */
    public double getAverageSP()
    {
        List<Artist> artists = gameEnvironment.getLabel().getLineUp();
        if (artists.isEmpty())
        {
            return 0;
        }

        double sp = 0;
        for (Artist artist : artists)
        {
            sp += artist.getStarPower();
        }
        return sp / artists.size();
    }

    /**
     * Returns the max sp.
     * @return The max sp.
     */
    public double getMaxSP()
    {
        List<Artist> artists = gameEnvironment.getLabel().getLineUp();

        double sp = 0;
        for (Artist artist : artists)
        {
            if(sp < artist.getStarPower())
            {
                sp = artist.getStarPower();
            }
        }
        return sp;
    }

    /**
     * Returns the min sp.
     * It derives the value from the current state before returning it.
     * @return The min sp.
     */
    public double getMinSP()
    {
        List<Artist> artists = gameEnvironment.getLabel().getLineUp();
        if (artists.isEmpty())
        {
            return 0;
        }

        double sp = artists.getFirst().getStarPower();
        for (Artist artist : artists)
        {
            if(sp > artist.getStarPower())
            {
                sp = artist.getStarPower();
            }
        }
        return sp;
    }

    /**
     * Applies the stamina change.
     * @param staminaChange the numeric value for the stamina change
     */
    public void applyStaminaChange(double staminaChange)
    {
        gameEnvironment.getLabel().applyStaminaToLineup((int) Math.round(staminaChange));
    }

    /**
     * Applies the stamina change to lineup artist.
     * @param lineupIndex the numeric value for the lineup index
     * @param staminaChange the numeric value for the stamina change
     */
    public void applyStaminaChangeToLineupArtist(int lineupIndex, double staminaChange)
    {
        gameEnvironment.getLabel().applyStaminaToLineupArtist(lineupIndex, (int) Math.round(staminaChange));
    }

    /**
     * Resets the lineup stamina.
     */
    public void resetLineupStamina()
    {
        gameEnvironment.getLabel().resetLineupStamina();
    }

    /**
     * Processes the retire artist.
     * @param artist the artist
     * @return True if retire artist, otherwise false.
     */
    public boolean retireArtist(Artist artist)
    {
        return gameEnvironment.getLabel().removeArtist(artist);
    }

    private void addItemToArtist(Artist artist, Item item)
    {
        artist.addItem(item);
    }

    private void removeItemFromArtist(Artist artist, Item item)
    {
        artist.removeItem(item);
    }

    /**
     * Equips an item to an artist
     * @param artist the artist
     * @param item the item involved in the operation
     * @return True if equip item, otherwise false.
     */
    public Boolean equipItem(Artist artist, Item item)
    {
        return gameEnvironment.getLabel().equipItem(artist, item);
    }

    /**
     * Unequips an item from an artist
     * @param artist the artist
     * @param item the item involved in the operation
     * @return True if unequip item, otherwise false.
     */
    public Boolean unequipItem(Artist artist, Item item)
    {
        return gameEnvironment.getLabel().unequipItem(artist, item);
    }

    /**
     * Uses a consumable item
     * It updates related state as needed while performing the operation.
     * @param artist the artist
     * @param item the item involved in the operation
     * @return The use consumable.
     */
    public String useConsumable(Artist artist, Item item)
    {
        if (!(item instanceof CosumableItem consumable) || !artist.getItems().contains(item))
        {
            return "";
        }

        ArrayList<String> effectMessages = new ArrayList<>();
        for (ItemEffects itemEffects : item.getEffects())
        {
            int effectValue = artist.getEffectValue(item, itemEffects);
            if (!artist.calculateEffect(item, itemEffects))
            {
                continue;
            }

            effectMessages.add(itemEffects.getName() + " applied +" + effectValue + " "
                    + itemEffects.getTargetStat().toString().toLowerCase().replace('_', ' '));
        }

        consumable.consumeUse();
        if (consumable.getUses() <= 0)
        {
            artist.removeItem(item);
            item.dispose();
        }

        if (effectMessages.isEmpty())
        {
            return item.getName() + " was used but nothing happened.";
        }

        return item.getName() + ": " + String.join(", ", effectMessages)
                + " (" + consumable.getUses() + " use(s) left)";
    }

}
