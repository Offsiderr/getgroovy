package seng201.team67.models;

import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.items.Item;

import java.util.ArrayList;
import java.util.List;

public class Label {

    private GameEnvironment gameEnvironment;

    //(Guilds) Since our project is music themed, guilds are called music labels
    public String name;
    public int artistsLimit;
    public int lineUpLimit; //not in use currently
    public double money;


    private ArrayList<Item> items = new ArrayList<Item>();

    //This is a list of every artist that the player has in their label
    private ArrayList<Artist> allArtists = new ArrayList<Artist>();

    //This is a list of artists from the player's line up.
    private List<Artist> lineUp = new ArrayList<Artist>();

    public Label(String name, List<Artist> selected_artists, GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        allArtists.addAll(selected_artists);
        lineUp.addAll(selected_artists);
        this.name = name;

        this.money = gameEnvironment.getConfig().startingCredits;
        this.artistsLimit = gameEnvironment.getConfig().artistsRosterLimit;
        this.lineUpLimit = gameEnvironment.getConfig().lineUpLimit;
    }

    //Getters
    public ArrayList<Artist> getAllArtists()
    {
        return allArtists;
    }

    public List<Artist> getLineUp()
    {
        return lineUp;
    }

    public int getLineUpLimit()
    {
        return lineUpLimit;
    }

    public int getArtistsLimit()
    {
        return artistsLimit;
    }

    public String getName()
    {
        return name;
    }

    public double getMoney(){return money;}

    public ArrayList<Item> getItems()
    {
        return new ArrayList<>(items);
    }

    //Setters
    public boolean addArtistToAll(Artist artist)
    {
        if(allArtists.size() < artistsLimit)
        {
            allArtists.add(artist);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void addItemToAll(Item item)
    {
        //add max items later
        items.add(item);
    }

    public void setLineUp(List<Artist> line_up)
    {
        if (line_up.size() > lineUpLimit)
        {
            throw new IllegalArgumentException("Lineup exceeds the configured limit of " + lineUpLimit + " artists.");
        }
        this.lineUp.clear();
        this.lineUp.addAll(line_up);
    }

    public void applyStaminaToLineup(int staminaChange)
    {
        for(Artist artist : lineUp)
        {
            artist.setStamina(artist.getStamina() + staminaChange);
        }
    }

    public void applyStaminaToLineupArtist(int lineupIndex, int staminaChange)
    {
        if (lineUp.isEmpty())
        {
            return;
        }

        Artist artist = lineUp.get(Math.floorMod(lineupIndex, lineUp.size()));
        artist.setStamina(artist.getStamina() + staminaChange);
    }

    public void resetLineupStamina()
    {
        for (Artist artist : lineUp)
        {
            artist.resetStamina();
        }
    }

    public void removeArtist(Artist artist)
    {
        allArtists.remove(artist);
        lineUp.remove(artist);
    }

    public void addItem(Item item)
    {
        items.add(item);
    }

    public void removeItem(Item item)
    {
        items.remove(item);
    }

    public boolean equipItem(Artist artist, Item item)
    {
        //TODO: make constant through game config
        if(artist.getItems().size() < 3 && items.contains(item))
        {
            artist.addItem(item);
            items.remove(item);
            return true;
        }
        return false;
    }
}
