package seng201.team67.models;

import seng201.team67.GameEnviroment;

import java.util.ArrayList;
import java.util.List;

public class Label {

    private GameEnviroment gameEnviroment;

    //(Guilds) Since our project is music themed, guilds are called music labels
    public String name;
    public int artists_limit;
    public int line_up_limit; //not in use currently
    public double money;


    private ArrayList<Item> items = new ArrayList<Item>();

    //This is a list of every artist that the player has in their label
    private ArrayList<Artist> all_artists = new ArrayList<Artist>();

    //This is a list of artists from the player's line up.
    private List<Artist> line_up = new ArrayList<Artist>();

    public Label(String name, List<Artist> selected_artists, GameEnviroment gameEnviroment)
    {
        this.gameEnviroment = gameEnviroment;
        all_artists.addAll(selected_artists);
        line_up.addAll(selected_artists);
        this.name = name;

        this.money = gameEnviroment.getConfig().startingCredits;
        this.artists_limit = gameEnviroment.getConfig().artistsRosterLimit;
        this.line_up_limit = gameEnviroment.getConfig().lineUpLimit;
    }

    //Getters
    public ArrayList<Artist> getAll_artists()
    {
        return all_artists;
    }

    public List<Artist> getLine_Up()
    {
        return line_up;
    }

    public int getArtists_limit()
    {
        return artists_limit;
    }

    public String getName()
    {
        return name;
    }

    public double getMoney(){return money;}

    //Setters
    public boolean addArtistToAll(Artist artist)
    {
        if(all_artists.size() < artists_limit)
        {
            all_artists.add(artist);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setLine_up(List<Artist> line_up)
    {
        this.line_up.clear();
        this.line_up.addAll(line_up);
    }

    public void applyStaminaToLineup(double stamina)
    {
        for(Artist artist : line_up)
        {
            artist.setStamina(artist.getStamina() - (int) stamina);
        }
    }
}
