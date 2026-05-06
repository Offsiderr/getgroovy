package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;
import seng201.team67.services.TourService;

import java.io.IOException;
import java.util.List;

public class TourResultsController {


    private GameEnvironment gameEnvironment;
    private TourService tourService;

    private Boolean staminaLoss; //this is tour finished because of a lack of stamina?

    //FXML stuff
    @FXML
    private SplitPane artistPane;
    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private Label payText;
    @FXML private Label labelName;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    public TourResultsController(GameEnvironment gameEnvironment, TourService tourService, Boolean staminaLoss)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
        this.staminaLoss = staminaLoss;
    }

    @FXML private void initialize()
    {
        loadLineup();
        payText.setText(Double.toString(tourService.getCreditsEarned()));

        if (staminaLoss)
        {
            labelName.setText("Tour Finished - Your artist(s) ran out of stamina!");
        }
    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnvironment.getLabelService().getLineup();
        List<VBox> cards = List.of(artistCardOne, artistCardTwo, artistCardThree);
        configureArtistPane(cards, pool.size());

        for (int i = 0; i < cards.size(); i++) {
            VBox card = cards.get(i);
            if (i < pool.size()) {
                card.setDisable(false);
                ArtistDetailBoxFiller.populateArtistBox(card, pool.get(i));
            } else {
                clearArtistCard(card);
            }
        }
    }

    private void configureArtistPane(List<VBox> cards, int artistCount) {
        int visibleCount = Math.max(0, Math.min(artistCount, cards.size()));
        artistPane.getItems().setAll(cards.subList(0, visibleCount).toArray(Node[]::new));

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

    @FXML private void continueGame(ActionEvent event) throws IOException {
        gameEnvironment.increaseTours();
        screenNavigator.navigate(event, "/fxml/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
