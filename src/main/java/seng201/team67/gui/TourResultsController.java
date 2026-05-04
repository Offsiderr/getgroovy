package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.Artist;
import seng201.team67.services.TourService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TourResultsController {


    private GameEnvironment gameEnvironment;
    private TourService tourService;

    private List<Artist> lineup;

    private Boolean staminaLoss; //this is tour finished because of a lack of stamina?

    //not needed currently, but here if needed in the future.
    private final List<ArtistCardController> artistCards = new ArrayList<>();

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
            card.getChildren().clear();
            if (i < pool.size()) {
                Artist artist = pool.get(i);
                card.setStyle("-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;");
                populateCard(card, artist);
            }
        }
    }

    private void configureArtistPane(List<VBox> cards, int lineupSize) {
        List<VBox> visibleCards = new ArrayList<>();
        int visibleCount = Math.max(1, Math.min(lineupSize, cards.size()));

        for (int i = 0; i < cards.size(); i++) {
            VBox card = cards.get(i);
            if (i < visibleCount) {
                card.setVisible(true);
                card.setManaged(true);
                visibleCards.add(card);
            } else {
                card.setVisible(false);
                card.setManaged(false);
            }
        }

        artistPane.getItems().setAll(visibleCards);
        artistPane.setPrefWidth(visibleCount == 1 ? 320 : visibleCount == 2 ? 650 : 977);
        artistPane.setLayoutX((1280 - artistPane.getPrefWidth()) / 2);
    }

    private void populateCard(VBox card, Artist artist)
    {
        Label nameLabel = new Label(artist.getName());
        Label typeLabel = new Label(artist.getType());
        Label starPowerLabel = new Label("Star Power: " + artist.getStarPower());
        Label staminaLabel = new Label("Stamina: " + artist.getStamina());
        Label healthLabel = new Label("Health: " + artist.getHealth());
        Label costLabel = new Label("Hire: $" + (int) artist.getCost());

        card.getChildren().addAll(nameLabel, typeLabel, starPowerLabel, staminaLabel, healthLabel, costLabel);
        card.setPadding(new Insets(8));
        card.setAlignment(Pos.CENTER);
    }

    @FXML private void continueGame(ActionEvent event) throws IOException {
        gameEnvironment.increaseTours();
        screenNavigator.navigate(event, "/fxml/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
