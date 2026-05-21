package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Artist;

import java.util.List;

/**
 * Provides game setup operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class GameSetupService {

    /**
     * Creates the label.
     * @param gameEnvironment the active game environment
     * @param selectedArtists the list of selected artists
     */
    public void createLabel(GameEnvironment gameEnvironment, List<Artist> selectedArtists)
    {
        gameEnvironment.setLabel(new Label(gameEnvironment.getTempName(), selectedArtists, gameEnvironment));
    }
}
