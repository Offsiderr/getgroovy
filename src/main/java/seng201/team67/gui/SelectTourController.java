package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.TourService;

import java.io.IOException;
import java.util.List;

import static seng201.team67.models.enums.TourType.COUNTRY;
import static seng201.team67.models.enums.TourType.WORLD;

public class SelectTourController {

    private GameEnvironment gameEnvironment;

    private List<Artist> lineup;


    //FXML stuff
    @FXML private SplitPane artistPane;
    @FXML private VBox artistOne;
    @FXML private VBox artistTwo;
    @FXML private VBox artistThree;

    @FXML private Label countryTourText;
    @FXML private Label worldTourText;

    @FXML private Pane countryTourPane;
    @FXML private Pane worldTourPane;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();


    public SelectTourController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize() throws IOException
    {
        lineup = gameEnvironment.getLabelService().label.getLineUp();

        List<VBox> slots = List.of(artistOne, artistTwo, artistThree);
        configureArtistPane(slots, lineup.size());

        for (int i = 0; i < slots.size(); i++) {
            VBox slot = slots.get(i);
            if (i < lineup.size()) {
                slot.setDisable(false);
                ArtistDetailBoxFiller.populateArtistBox(slot, lineup.get(i), null);
            } else {
                clearArtistCard(slot);
            }
        }

        checkTours(countryTourText, countryTourPane, COUNTRY);
        checkTours(worldTourText, worldTourPane, WORLD);
    }

    private void configureArtistPane(List<VBox> slots, int lineupSize) {
        int visibleCount = Math.max(0, Math.min(lineupSize, slots.size()));
        artistPane.getItems().setAll(slots.subList(0, visibleCount).toArray(Node[]::new));

        if (visibleCount == 2) {
            artistPane.setDividerPositions(0.5);
        } else if (visibleCount >= 3) {
            artistPane.setDividerPositions(1.0 / 3.0, 2.0 / 3.0);
        }
    }

    private void clearArtistCard(VBox card) {
        card.getChildren().clear();
        card.setDisable(true);
        ArtistDetailBoxFiller.applyBaseStyle(card);
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

