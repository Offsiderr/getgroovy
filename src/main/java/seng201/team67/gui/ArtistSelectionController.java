package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.gui.controllers.instantiable.GachaController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.Artist;
import seng201.team67.services.ArtistSelectionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArtistSelectionController {

    //This class stores the artist selection for the start of the game,
    //but will be split out into a basic selection interface super class
    //that other classes can inherit from in the future.

    public final GameEnvironment gameEnvironment;
    private final ArtistSelectionService artistSelectionService;

    @FXML private HBox artistOne;
    @FXML private HBox artistTwo;
    @FXML private HBox artistThree;
    @FXML private HBox artistFour;
    @FXML private HBox artistFive;
    @FXML private StackPane gachaContainer;
    @FXML private Button selectArtists;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    private final List<ArtistCardController> artistCards = new ArrayList<>();

    public ArtistSelectionController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
        this.artistSelectionService = new ArtistSelectionService(gameEnvironment);
    }

    @FXML
    public void initialize() throws IOException {
        selectArtists.setDisable(true);

        GachaController gachaController = new GachaController(gameEnvironment);
        gachaController.setOnGachaComplete(() -> showArtistCards());
        viewLoader.loadInto(gachaContainer, "/fxml/Gatcha.fxml", gachaController);
    }

    private void showArtistCards() {
        gachaContainer.setVisible(false);

        List<Artist> picked = artistSelectionService.pickArtists();


        List<HBox> slots = List.of(artistOne, artistTwo, artistThree, artistFour, artistFive);

        for (int i = 0; i < slots.size(); i++) {
            ArtistCardController cardController = new ArtistCardController(gameEnvironment, null);
            viewLoader.loadInto(slots.get(i), "/fxml/ArtistCard.fxml", cardController);
            cardController.setArtist(picked.get(i));
            cardController.setSelectionController(this);
            artistCards.add(cardController);
        }
    }

    public void onSelectionChanged() {

        long selectedCount = artistSelectionService.onSelectionChanged(artistCards);
        selectArtists.setDisable(selectedCount != gameEnvironment.getConfig().maxStartingArtists);
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
        gameEnvironment.createLabel(getSelectedArtists());
        gameEnvironment.getLabelService().getAllArtists().forEach(artist -> {
            artist.owned = true;
        });

        moveScene(event);
    }

    @FXML
    private void moveScene(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
