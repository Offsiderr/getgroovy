package seng201.team67.gui.results;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.tour.MainGameController;
import seng201.team67.gui.results.TourResultsController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.EventType;
import seng201.team67.models.enums.RandomEvent;
import seng201.team67.services.gameplay.RandomEventService;
import seng201.team67.services.gameplay.TourService;

import java.io.IOException;

public class RandomEventResultController {

    private final GameEnvironment gameEnvironment;
    private final TourService tourService;
    private final Artist affectedArtist;
    private final Boolean staminaLoss;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final RandomEventService randomEventService = new RandomEventService();
    private final RandomEvent randomEvent;
    private final String customTitle;
    private final String customDescription;

    @FXML private Label labelName;
    @FXML private Label labelName1;
    @FXML private VBox artistCardTwo;

    public RandomEventResultController(GameEnvironment gameEnvironment, TourService tourService, RandomEvent randomEvent) {
        this(gameEnvironment, tourService, randomEvent, null, null, null, null);
    }

    public RandomEventResultController(GameEnvironment gameEnvironment,
                                       TourService tourService,
                                       RandomEvent randomEvent,
                                       Artist affectedArtist) {
        this(gameEnvironment, tourService, randomEvent, affectedArtist, null, null, null);
    }

    public RandomEventResultController(GameEnvironment gameEnvironment,
                                       TourService tourService,
                                       RandomEvent randomEvent,
                                       Artist affectedArtist,
                                       Boolean staminaLoss) {
        this(gameEnvironment, tourService, randomEvent, affectedArtist, staminaLoss, null, null);
    }

    public RandomEventResultController(GameEnvironment gameEnvironment,
                                       TourService tourService,
                                       String customTitle,
                                       String customDescription,
                                       Artist affectedArtist,
                                       Boolean staminaLoss) {
        this(gameEnvironment, tourService, null, affectedArtist, staminaLoss, customTitle, customDescription);
    }

    private RandomEventResultController(GameEnvironment gameEnvironment,
                                        TourService tourService,
                                        RandomEvent randomEvent,
                                        Artist affectedArtist,
                                        Boolean staminaLoss,
                                        String customTitle,
                                        String customDescription) {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
        this.randomEvent = randomEvent;
        this.affectedArtist = affectedArtist;
        this.staminaLoss = staminaLoss;
        this.customTitle = customTitle;
        this.customDescription = customDescription;
    }

    @FXML
    private void initialize() {
        if (randomEvent != null) {
            randomEventService.applyRandomEvent(gameEnvironment, randomEvent, affectedArtist);
            labelName.setText(randomEvent.getName());
        } else {
            labelName.setText(customTitle);
        }

        labelName1.setText(buildEventDescription());
        loadArtistCard();
    }

    private String buildEventDescription() {
        if (randomEvent == null) {
            return customDescription == null ? "" : customDescription;
        }

        return randomEvent.getDescription() + "\n\n" + buildEffectText();
    }

    private String buildEffectText() {
        if (randomEvent.getType() == EventType.RETIREMENT) {
            return "Payout: $" + randomEvent.getValue();
        }

        String valuePrefix = randomEvent.getValue() > 0 ? "+" : "";

        if (randomEvent.getType() == EventType.SKILL) {
            return "Skill Change: " + valuePrefix + randomEvent.getValue();
        }

        return "Stat Change: " + valuePrefix + randomEvent.getValue();
    }

    private void loadArtistCard() {
        if (affectedArtist == null) {
            artistCardTwo.getChildren().clear();
            ArtistDetailBoxFiller.applyBaseStyle(artistCardTwo);
            return;
        }

        artistCardTwo.setDisable(false);
        ArtistDetailBoxFiller.populateArtistBox(artistCardTwo, affectedArtist, null);
    }

    @FXML
    private void continueGame(ActionEvent event) throws IOException {
        if (staminaLoss != null) {
            screenNavigator.navigate(event, "/fxml/results/TourResults.fxml",
                    new TourResultsController(gameEnvironment, tourService, staminaLoss));
            return;
        }

        screenNavigator.navigate(event, "/fxml/tour/MainGame.fxml",
                new MainGameController(gameEnvironment, tourService));
    }
}
