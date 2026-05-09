package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Artist;

import java.util.List;

public class GameSetupService {

    public void createLabel(GameEnvironment gameEnvironment, List<Artist> selectedArtists)
    {
        gameEnvironment.setLabel(new Label(gameEnvironment.getTempName(), selectedArtists, gameEnvironment));
    }
}
