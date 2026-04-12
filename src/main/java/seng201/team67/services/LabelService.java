package seng201.team67.services;

import seng201.team67.models.Artist;
import seng201.team67.models.Label;

public class LabelService {

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
}
