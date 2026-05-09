package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.ArtistCardController;
import seng201.team67.gui.instantiable.GachaController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.Rarity;
import seng201.team67.services.setup.GachaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GachaSelectionController extends ArtistSelectionController {

    //This is quite similar to the starting artist selection. A decision was made not to make it modular due to the difficulty
    //in making the split panes adjustable.

    public final GameEnvironment gameEnvironment;
    private final GachaService gachaService;
    private boolean artists; //Yes if artists are being opened, otherwise it is items.

    @FXML private HBox itemOne;
    @FXML private HBox itemTwo;
    @FXML private HBox itemThree;
    @FXML private StackPane gachaContainer;
    @FXML private Button selectArtists;

    private int hboxSize;

    private Rarity rarity;

    //this needs to be implemented in the super class later... hard coded for now.
    private int selectionSize;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    private final List<ArtistCardController> artistCards = new ArrayList<>();

    public GachaSelectionController(GameEnvironment gameEnvironment, Boolean artists, int hboxSize, Rarity rarity) {
        super(gameEnvironment);
        this.gameEnvironment = gameEnvironment;
        this.artists = artists;
        this.hboxSize = hboxSize;
        this.rarity = rarity;
        gachaService = new GachaService(gameEnvironment);
    }

    @FXML
    public void initialize() throws IOException {
        selectArtists.setDisable(true);

        GachaController gachaController = new GachaController(gameEnvironment);
        gachaController.setOnGachaComplete(() -> {if(artists){showArtistCards();}else{showItemCards();}});
        viewLoader.loadInto(gachaContainer, "/fxml/Gatcha.fxml", gachaController);
    }

    private void showArtistCards() {
        gachaContainer.setVisible(false);

        List<HBox> slots = List.of(itemOne, itemTwo, itemThree);

        List<Artist> picked = gachaService.getPickedArtists(slots, rarity);


        for (int i = 0; i < slots.size(); i++) {
            ArtistCardController cardController = new ArtistCardController(gameEnvironment, null);
            viewLoader.loadInto(slots.get(i), "/fxml/ArtistCard.fxml", cardController);
            cardController.setArtist(picked.get(i));
            cardController.setSelectionController(this);
            artistCards.add(cardController);
        }
    }

    //Will move this later
    private void showItemCards() {
        gachaContainer.setVisible(false);

        List<Artist> pool = gameEnvironment.getArtistPool();
        Collections.shuffle(pool);

        List<HBox> slots = List.of(itemOne, itemTwo, itemThree);

        List<Artist> picked = new ArrayList<>();

        for (int i = 0; i < slots.size(); i++)
        {
            int z = i;
            while (pool.get(z).owned)
            {
                z += 1;
            }
            picked.add(pool.get(z));
        }


        for (int i = 0; i < slots.size(); i++) {
            ArtistCardController cardController = new ArtistCardController(gameEnvironment, null);
            viewLoader.loadInto(slots.get(i), "/fxml/ArtistCard.fxml", cardController);
            cardController.setArtist(picked.get(i));
            cardController.setSelectionController(this);
            artistCards.add(cardController);
        }
    }

    public void onSelectionChanged() {
        long selectedCount = gachaService.onSelectionChanged(artistCards);

        selectArtists.setDisable(selectedCount != 1);
    }

    public List<Artist> getSelectedArtists() {
        return artistCards.stream()
                .filter(ArtistCardController::isSelected)
                .map(c -> c.artist)
                .toList();
    }

    @FXML
    public void artistsSelected(ActionEvent event) throws IOException
    {
        gameEnvironment.getLabelService().hireArtist(getSelectedArtists().get(0), 0);
        moveScene(event);
    }

    @FXML
    private void moveScene(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/TheStudio.fxml", new TheStudioController(gameEnvironment));
    }
}
