package seng201.team67.services;

import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.items.Item;

import java.util.List;

public class LabelService {

    //This class needs a considerable amount of work along with the actual Label class to bring it inline with our
    //standards

    public Label label;
    private GameEnvironment gameEnvironment;

    public LabelService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    public void setLabel(Label label){this.label = label;}

    public boolean hireArtist(Artist artist, int cost)
    {
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
        label.setLineUp(artist_lineup);
    }

    public String getLabelName()
    {
        return label.getName();
    }

    public List<Artist> getLineup()
    {
        return label.getLineUp();
    }

    public int getLineupLimit()
    {
        return label.getLineUpLimit();
    }

    public List<Artist> getAllArtists()
    {
        return label.getAllArtists();
    }

    public List<Item> getAllItems()
    {
        return label.getItems();
    }

    public Double getMoney()
    {
        return label.getMoney();
    }

    public void takeMoney(double money)
    {
        label.money = label.getMoney() - money;
    }

    public void giveMoney(double money)
    {
        label.money = label.getMoney() + money;
    }

    public double getLineupTotalPay()
    {
        List<Artist> artists = label.getLineUp();

        double totalCost = 0;

        for (Artist artist : artists)
        {
            totalCost += artist.getPay() * gameEnvironment.getDifficulty().getPayMultiplier();
        }
        return totalCost;
    }

    public double getAverageSP()
    {
        List<Artist> artists = label.getLineUp();
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
        List<Artist> artists = label.getLineUp();

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
        List<Artist> artists = label.getLineUp();
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
        label.applyStaminaToLineup((int) Math.round(staminaChange));
    }

    public void applyStaminaChangeToLineupArtist(int lineupIndex, double staminaChange)
    {
        label.applyStaminaToLineupArtist(lineupIndex, (int) Math.round(staminaChange));
    }

    public void resetLineupStamina()
    {
        label.resetLineupStamina();
    }

    public void retireArtist(Artist artist)
    {
        label.removeArtist(artist);
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
        return label.equipItem(artist, item);
    }
}
