package seng201.team67.models;

import java.util.ArrayList;
import java.util.List;

public class Label {

    //(Guilds) Since our project is music themed, guilds are called music labels
    public String name;
    public int artists_limit = 12;
    public int line_up_limit = 5;
    public double money = 1000;


    private ArrayList<Item> items = new ArrayList<Item>();

    //This is a list of every artist that the player has in their label
    private ArrayList<Artist> all_artists = new ArrayList<Artist>();

    //This is a list of artists from the player's line up.
    private List<Artist> line_up = new ArrayList<Artist>();

    public Label(String name, List<Artist> selected_artists)
    {
        all_artists.addAll(selected_artists);
        line_up.addAll(selected_artists);
        this.name = name;
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


}
