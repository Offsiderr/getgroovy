package seng201.team67.services.management;

import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.enums.items.Effect;
import seng201.team67.models.items.CosumableItem;
import seng201.team67.models.items.Item;

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
        label.money = label.money - cost;

        artist.owned = true;

        label.addArtistToAll(artist);
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
        label.money = label.money - artist.getCost();

        artist.owned = true;
        label.addArtistToAll(artist);
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


        label.money = label.money - cost;
        item.purchase();
        label.addItemToAll(item);
        return true;
    }

    public boolean buyItem(Item item)
    {
        Label label = gameEnvironment.getLabel();
        if(label.getMoney() < item.getCost())
        {
            return false;
        }

        label.money = label.money - item.getCost();

        item.purchase();
        label.addItemToAll(item);
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
        List<Artist> artists = gameEnvironment.getLabel().getLineUp();

        double totalCost = 0;

        for (Artist artist : artists)
        {
            totalCost += artist.getPay() * gameEnvironment.getDifficulty().getPayMultiplier();
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

    public String useConsumable(Artist artist, Item item)
    {
        if (!(item instanceof CosumableItem consumable) || !artist.getItems().contains(item))
        {
            return "";
        }

        ArrayList<String> effectMessages = new ArrayList<>();
        for (Effect effect : item.getEffects())
        {
            int effectValue = artist.getEffectValue(effect);
            if (!artist.calculateEffect(effect))
            {
                continue;
            }

            effectMessages.add(effect.getName() + " applied +" + effectValue + " "
                    + effect.getTargetStat().toString().toLowerCase().replace('_', ' '));
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
