package seng201.team67.services;

import seng201.team67.models.Artist;
import seng201.team67.models.Label;

import java.util.List;

public class LabelService {

    public Label label;

    public void setLabel(Label label){this.label = label;}

    public boolean hireArtist(Artist artist, int cost)
    {
        //with a cost override. This is used in gatchas as they don't cost anything to choose an artist.
        if (cost > label.getMoney())
        {
            return false;
        }
        else if (label.getAll_artists().size() >= label.getArtists_limit())
        {
            return false;
        }

        label.addArtistToAll(artist);
        return true;
    }

    public boolean hireArtist(Artist artist)
    {
        if (artist.getCost() > label.getMoney())
        {
            return false;
        }
        else if (label.getAll_artists().size() >= label.getArtists_limit())
        {
            return false;
        }

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
            return true;
        }
    }

    public void setLineUp(List<Artist> artist_lineup)
    {
        label.setLine_up(artist_lineup);
    }

    public String getLabelName()
    {
        return label.getName();
    }

    public List<Artist> getLineup()
    {
        return label.getLine_Up();
    }

    public List<Artist> getAllArtists()
    {
        return label.getAll_artists();
    }

    public Double getMoney()
    {
        return label.getMoney();
    }

}
