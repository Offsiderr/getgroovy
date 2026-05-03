package seng201.team67.services;

import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.models.Artist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtistSelectionService {

    private GameEnvironment gameEnvironment;

    public ArtistSelectionService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

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
