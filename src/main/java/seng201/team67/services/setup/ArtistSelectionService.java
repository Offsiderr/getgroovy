package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.ArtistCardController;
import seng201.team67.models.artists.Artist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides artist selection operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ArtistSelectionService {

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;

    /**
     * Creates a new artist selection service.
     * @param gameEnvironment the active game environment
     */
    public ArtistSelectionService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Processes the artists that the player has picked and sets them as their label's lineup
     * @return A list containing the pick artists.
     */
    public List<Artist> pickArtists()
    {
        List<Artist> pool = gameEnvironment.getArtistPool();
        Collections.shuffle(pool);

        List<Artist> picked = new ArrayList<>();
        //pick artists
        for(int i = 0; i < gameEnvironment.getConfig().startingArtistPoolSize; i++)
        {
            int z = i;
            while (pool.get(z).getStarPower() > gameEnvironment.getConfig().maxSPInStartingSelection || picked.contains(pool.get(z)))
            {
                z += 1;
            }
            picked.add(pool.get(z));
        }
        return picked;
    }

    public long onSelectionChanged(List<ArtistCardController> artistCards)
    {
        long selectedCount = artistCards.stream()
                .filter(ArtistCardController::isSelected)
                .count();



        artistCards.forEach(c -> {
            if (!c.isSelected()) {
                c.setSelectable(selectedCount < gameEnvironment.getConfig().maxStartingArtists);
            }
        });

        return selectedCount;
    }
}
