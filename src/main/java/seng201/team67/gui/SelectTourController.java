package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.ArtistCardController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;

import javafx.event.ActionEvent;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.TourService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static seng201.team67.models.enums.TourType.COUNTRY;
import static seng201.team67.models.enums.TourType.WORLD;

public class SelectTourController {

    private GameEnvironment gameEnvironment;

    private List<Artist> lineup;

    //not needed currently, but here if needed in the future.
    private final List<ArtistCardController> artistCards = new ArrayList<>();


    //FXML stuff
    @FXML private SplitPane artistPane;
    @FXML private AnchorPane artistOne;
    @FXML private AnchorPane artistTwo;
    @FXML private AnchorPane artistThree;

    @FXML private Label countryTourText;
    @FXML private Label worldTourText;

    @FXML private Pane countryTourPane;
    @FXML private Pane worldTourPane;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();


    public SelectTourController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize() throws IOException
    {
        lineup = gameEnvironment.getLabelService().label.getLineUp();

        List<AnchorPane> slots = List.of(artistOne, artistTwo, artistThree);
        configureArtistPane(slots, lineup.size());

        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).getChildren().clear();
            if (i >= lineup.size()) {
                continue;
            }
            ArtistCardController cardController = new ArtistCardController(gameEnvironment, null);
            viewLoader.loadInto(slots.get(i), "/fxml/ArtistCard.fxml", cardController);
            cardController.setArtist(lineup.get(i));
            artistCards.add(cardController);
        }

        checkTours(countryTourText, countryTourPane, COUNTRY);
        checkTours(worldTourText, worldTourPane, WORLD);
    }

    private void configureArtistPane(List<AnchorPane> slots, int lineupSize) {
        List<AnchorPane> visibleSlots = new ArrayList<>();
        int visibleCount = Math.max(1, Math.min(lineupSize, slots.size()));

        for (int i = 0; i < slots.size(); i++) {
            AnchorPane slot = slots.get(i);
            if (i < visibleCount) {
                slot.setVisible(true);
                slot.setManaged(true);
                visibleSlots.add(slot);
            } else {
                slot.setVisible(false);
                slot.setManaged(false);
            }
        }

        artistPane.getItems().setAll(visibleSlots);
        artistPane.setPrefWidth(visibleCount == 1 ? 320 : visibleCount == 2 ? 650 : 977);
        artistPane.setLayoutX((1280 - artistPane.getPrefWidth()) / 2);
    }

    private void checkTours(Label label, Pane pane, TourType tour)
    {
        if(gameEnvironment.getTourCount() < tour.getExpeditionsUnlocked())
        {
            pane.setVisible(true);
            label.setText("Complete " + Integer.toString(tour.getExpeditionsUnlocked() - gameEnvironment.getTourCount()) +
            " more tours to unlock the " + tour + " tour.");
        }
        else
        {
            pane.setVisible(false);
        }
    }

    //All the buttons

    @FXML
    public void startLocalTour(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/MainGame.fxml",
                new MainGameController(gameEnvironment, new TourService(new Tour(TourType.LOCAL), gameEnvironment)));
    }

    @FXML
    public void startCountryTour(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/MainGame.fxml",
                new MainGameController(gameEnvironment, new TourService(new Tour(COUNTRY), gameEnvironment)));
    }

    @FXML
    public void startWorldTour(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/MainGame.fxml",
                new MainGameController(gameEnvironment, new TourService(new Tour(TourType.WORLD), gameEnvironment)));
    }

    @FXML public void cancelTour(ActionEvent event) throws IOException {
        screenNavigator.navigate(event, "/fxml/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

}

