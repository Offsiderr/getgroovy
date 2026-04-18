package seng201.team67.services;

import seng201.team67.models.Artist;
import seng201.team67.models.Label;

import java.util.List;

public class LabelService {

    public Label label;

    public void setLabel(Label label){this.label = label;}

    public boolean hireArtist(Label label, Artist artist, int gold)
    {
        if (gold < artist.getCost())
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

}
