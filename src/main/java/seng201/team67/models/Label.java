package seng201.team67.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the label used by the game. Since our game is music themed, Guilds are called labels.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Label {

    /** Hard cap for the label's all-artists roster. */
    private static final int ALL_ARTISTS_LIMIT = 5;

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;

    /** Text value for the name. */
    public String name;
    /** Numeric value for the artists limit. */
    public int artistsLimit;
    /** Numeric value for the line up limit. */
    public int lineUpLimit; //not in use currently
    /** Numeric value for the money. */
    public double money;


    /** Collection that stores the items. */
    private ArrayList<Item> items = new ArrayList<Item>();

    /** Collection that stores the all artists. */
    private ArrayList<Artist> allArtists = new ArrayList<Artist>();

    /** Collection that stores the line up. */
    private List<Artist> lineUp = new ArrayList<Artist>();

    /**
     * Creates a new label.
     */
    public Label()
    {
    }

    /**
     * Creates a new label.
     * It initializes the state needed for the surrounding game flow.
     * @param name the name value to use
     * @param selected_artists the list of selected artists
     * @param gameEnvironment the active game environment
     */
    public Label(String name, List<Artist> selected_artists, GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        allArtists.addAll(selected_artists);
        lineUp.addAll(selected_artists);
        this.name = name;

        this.money = gameEnvironment.getConfig().startingCredits;
        this.artistsLimit = ALL_ARTISTS_LIMIT;
        this.lineUpLimit = gameEnvironment.getConfig().lineUpLimit;
    }

    //Getters
    /**
     * Returns the all artists.
     * @return The all artists.
     */
    public ArrayList<Artist> getAllArtists()
    {
        return allArtists;
    }

    /**
     * Returns the line up.
     * @return The line up.
     */
    public List<Artist> getLineUp()
    {
        return lineUp;
    }

    /**
     * Returns the line up limit.
     * @return The line up limit.
     */
    public int getLineUpLimit()
    {
        return lineUpLimit;
    }

    /**
     * Returns the artists limit.
     * @return The artists limit.
     */
    public int getArtistsLimit()
    {
        return ALL_ARTISTS_LIMIT;
    }

    /**
     * Returns the name.
     * @return The name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the money.
     * @return The money.
     */
    public double getMoney(){return money;}

    /**
     * Returns the items.
     * @return The items.
     */
    public ArrayList<Item> getItems()
    {
        return new ArrayList<>(items);
    }

    //Setters
    /**
     * Adds the artist to all.
     * It updates related state as needed while performing the operation.
     * @param artist the artist
     * @return True if artist to all, otherwise false.
     */
    public boolean addArtistToAll(Artist artist)
    {
        if (allArtists.contains(artist))
        {
            return false;
        }

        if(allArtists.size() < getArtistsLimit())
        {
            allArtists.add(artist);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Adds the item to all.
     * @param item the item involved in the operation
     * @return True if item to all, otherwise false.
     */
    public boolean addItemToAll(Item item)
    {
        if (items.contains(item))
        {
            return false;
        }

        //add max items later
        items.add(item);
        return true;
    }

    /**
     * Sets the line up.
     * @param line_up the list of line up
     */
    public void setLineUp(List<Artist> line_up)
    {
        if (line_up.size() > lineUpLimit)
        {
            throw new IllegalArgumentException("Lineup exceeds the configured limit of " + lineUpLimit + " artists.");
        }
        this.lineUp.clear();
        this.lineUp.addAll(line_up);
    }

    /**
     * Applies the stamina to lineup.
     * @param staminaChange the numeric value for the stamina change
     */
    public void applyStaminaToLineup(int staminaChange)
    {
        for(Artist artist : lineUp)
        {
            artist.setStamina(artist.getStamina() + staminaChange);
        }
    }

    /**
     * Applies the stamina to lineup artist.
     * @param lineupIndex the numeric value for the lineup index
     * @param staminaChange the numeric value for the stamina change
     */
    public void applyStaminaToLineupArtist(int lineupIndex, int staminaChange)
    {
        if (lineUp.isEmpty())
        {
            return;
        }

        Artist artist = lineUp.get(Math.floorMod(lineupIndex, lineUp.size()));
        artist.setStamina(artist.getStamina() + staminaChange);
    }

    /**
     * Resets the lineup stamina.
     */
    public void resetLineupStamina()
    {
        for (Artist artist : lineUp)
        {
            artist.resetStamina();
        }
    }

    /**
     * Removes the artist.
     * @param artist the artist
     * @return True if artist, otherwise false.
     */
    public boolean removeArtist(Artist artist)
    {
        if (allArtists.size() <= 1 || !allArtists.contains(artist))
        {
            return false;
        }

        allArtists.remove(artist);
        lineUp.remove(artist);
        return true;
    }

    /**
     * Adds the item.
     * @param item the item involved in the operation
     */
    public void addItem(Item item)
    {
        items.add(item);
    }

    /**
     * Removes the item.
     * @param item the item involved in the operation
     */
    public void removeItem(Item item)
    {
        items.remove(item);
    }

    /**
     * Processes the equip item.
     * @param artist the artist
     * @param item the item involved in the operation
     * @return True if equip item, otherwise false.
     */
    public boolean equipItem(Artist artist, Item item)
    {
        if(artist.getItems().size() < 3 && items.contains(item))
        {
            artist.addItem(item);
            items.remove(item);
            return true;
        }
        return false;
    }

    /**
     * Processes the unequip item.
     * @param artist the artist
     * @param item the item involved in the operation
     * @return True if unequip item, otherwise false.
     */
    public boolean unequipItem(Artist artist, Item item)
    {
        if (!artist.getItems().contains(item))
        {
            return false;
        }

        artist.removeItem(item);
        items.add(item);
        return true;
    }
}
