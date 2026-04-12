package seng201.team67.models;

import java.util.ArrayList;

public class Label {

    //(Guilds) Since our project is music themed, guilds are called music labels
    private String name;
    private int artists_limit = 12;
    private int line_up_limit = 5;

    private ArrayList<Item> items = new ArrayList<Item>();

    //This is a list of every artist that the player has in their label
    private ArrayList<Artist> all_artists = new ArrayList<Artist>();

    //This is a list of artists from the player's line up.
    private ArrayList<Artist> line_up = new ArrayList<Artist>();

    public Label(String name, ArrayList<Artist> selected_artists)
    {
        all_artists.addAll(selected_artists);
        this.name = name;
    }

    //Getters
    public ArrayList<Artist> getAll_artists()
    {
        return all_artists;
    }

    public ArrayList<Artist> getLine_Up()
    {
        return line_up;
    }

    public int getArtists_limit()
    {
        return artists_limit;
    }

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
}
