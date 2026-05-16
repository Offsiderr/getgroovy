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

public class LabelService {

    //This class needs a considerable amount of work along with the actual Label class to bring it inline with our
    //standards

    private final GameEnvironment gameEnvironment;

    public LabelService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

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

        label.money = label.money - cost;
        artist.owned = true;
        return true;
    }

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

        label.money = label.money - artist.getCost();
        artist.owned = true;
        return true;
    }

    public boolean buyItem(int cost)
    {
        Label label = gameEnvironment.getLabel();
        if(label.getMoney() < cost)
        {
            return false;
        }
        else
        {
            label.money = label.money - cost;
            return true;
        }
    }


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

        label.money = label.money - cost;
        item.purchase();
        return true;
    }

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

        label.money = label.money - item.getCost();
        item.purchase();
        return true;
    }

    public void setLineUp(List<Artist> artist_lineup)
    {
        gameEnvironment.getLabel().setLineUp(artist_lineup);
    }

    public String getLabelName()
    {
        return gameEnvironment.getLabel().getName();
    }

    public List<Artist> getLineup()
    {
        return gameEnvironment.getLabel().getLineUp();
    }

    public int getLineupLimit()
    {
        return gameEnvironment.getLabel().getLineUpLimit();
    }

    public List<Artist> getAllArtists()
    {
        return gameEnvironment.getLabel().getAllArtists();
    }

    public List<Item> getAllItems()
    {
        return gameEnvironment.getLabel().getItems();
    }

    public Double getMoney()
    {
        return gameEnvironment.getLabel().getMoney();
    }

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

    public boolean sellItem(Item item)
    {
        Label label = gameEnvironment.getLabel();
        if (!label.getItems().contains(item))
        {
            return false;
        }

        label.money = label.money + getItemSellPrice(item);
        label.removeItem(item);
        item.dispose();
        return true;
    }

    public void takeMoney(double money)
    {
        Label label = gameEnvironment.getLabel();
        label.money = label.getMoney() - money;
    }

    public void giveMoney(double money)
    {
        Label label = gameEnvironment.getLabel();
        label.money = label.getMoney() + money;
    }

    public double getLineupTotalPay()
    {
        return getLineupTotalPay(TourType.LOCAL);
    }

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

    public void applyStaminaChange(double staminaChange)
    {
        gameEnvironment.getLabel().applyStaminaToLineup((int) Math.round(staminaChange));
    }

    public void applyStaminaChangeToLineupArtist(int lineupIndex, double staminaChange)
    {
        gameEnvironment.getLabel().applyStaminaToLineupArtist(lineupIndex, (int) Math.round(staminaChange));
    }

    public void resetLineupStamina()
    {
        gameEnvironment.getLabel().resetLineupStamina();
    }

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

    public Boolean equipItem(Artist artist, Item item)
    {
        return gameEnvironment.getLabel().equipItem(artist, item);
    }

    public Boolean unequipItem(Artist artist, Item item)
    {
        return gameEnvironment.getLabel().unequipItem(artist, item);
    }

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
