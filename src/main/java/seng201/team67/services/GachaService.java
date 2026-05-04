package seng201.team67.services;

import javafx.scene.layout.HBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.ArtistCardController;
import seng201.team67.models.Artist;
import seng201.team67.models.enums.Rarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GachaService {

    private GameEnvironment gameEnvironment;

    public GachaService(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    public List<Artist> getPickedArtists(List<HBox> slots, Rarity rarity)
    {
        List<Artist> pool = gameEnvironment.getArtistPool();
        Collections.shuffle(pool);


        List<Artist> picked = new ArrayList<>();
        for (int i = 0; i < slots.size(); i++)
        {
            int selectedStarpower = rarity.get_starpower();
            int z = i;
            while (pool.get(z).owned && pool.get(z).getStarPower() != selectedStarpower)
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
                c.setSelectable(selectedCount < 1);
            }
        });
        return selectedCount;
    }
}
