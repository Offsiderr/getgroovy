package seng201.team67.gui.results;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

/**
 * Controls the random event result view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class RandomEventResultController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** Service used to manage tour behaviour. */
    private final TourService tourService;
    /** The affected artist. */
    private final Artist affectedArtist;
    /** Whether stamina loss. */
    private final Boolean staminaLoss;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** Service used to manage random event behaviour. */
    private final RandomEventService randomEventService = new RandomEventService();
    /** The random event. */
    private final RandomEvent randomEvent;
    /** Text value for the custom title. */
    private final String customTitle;
    /** Text value for the custom description. */
    private final String customDescription;

    /** FXML reference for the label name control. */
    @FXML private Label labelName;
    /** FXML reference for the label name1 control. */
    @FXML private Label labelName1;
    /** FXML reference for the artist card two control. */
    @FXML private VBox artistCardTwo;
    /** FXML reference for the bg1 control. */
    @FXML private ImageView bg1;
    /** FXML reference for the bg2 control. */
    @FXML private ImageView bg2;

    /**
     * Creates a new random event result controller.
     * @param gameEnvironment the active game environment
     * @param tourService the tour service for the current run
     * @param randomEvent the random event
     */
    public RandomEventResultController(GameEnvironment gameEnvironment, TourService tourService, RandomEvent randomEvent) {
        this(gameEnvironment, tourService, randomEvent, null, null, null, null);
    }

    /**
     * Creates a new random event result controller.
     * @param gameEnvironment the active game environment
     * @param tourService the tour service for the current run
     * @param randomEvent the random event
     * @param affectedArtist the affected artist
     */
    public RandomEventResultController(GameEnvironment gameEnvironment,
                                       TourService tourService,
                                       RandomEvent randomEvent,
                                       Artist affectedArtist) {
        this(gameEnvironment, tourService, randomEvent, affectedArtist, null, null, null);
    }

    /**
     * Creates a new random event result controller.
     * @param gameEnvironment the active game environment
     * @param tourService the tour service for the current run
     * @param randomEvent the random event
     * @param affectedArtist the affected artist
     * @param staminaLoss whether stamina loss
     */
    public RandomEventResultController(GameEnvironment gameEnvironment,
                                       TourService tourService,
                                       RandomEvent randomEvent,
                                       Artist affectedArtist,
                                       Boolean staminaLoss) {
        this(gameEnvironment, tourService, randomEvent, affectedArtist, staminaLoss, null, null);
    }

    /**
     * Creates a new random event result controller.
     * @param gameEnvironment the active game environment
     * @param tourService the tour service for the current run
     * @param customTitle the text value for the custom title
     * @param customDescription the text value for the custom description
     * @param affectedArtist the affected artist
     * @param staminaLoss whether stamina loss
     */
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
        startBackgroundAnimation();

        if (randomEvent != null) {
            randomEventService.applyRandomEvent(gameEnvironment, randomEvent, affectedArtist);
            labelName.setText(randomEvent.getName());
        } else {
            labelName.setText(customTitle);
        }

        labelName1.setText(buildEventDescription());
        loadArtistCard();
    }

    private void startBackgroundAnimation() {
        bg1.setImage(new Image(getClass().getResourceAsStream("/images/gameBackground.png")));
        bg2.setImage(new Image(getClass().getResourceAsStream("/images/gameBackground.png")));

        AnimationTimer bgTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                bg1.setLayoutX(bg1.getLayoutX() - 0.5);
                bg2.setLayoutX(bg2.getLayoutX() - 0.5);

                if (bg1.getLayoutX() <= -bg1.getFitWidth()) {
                    bg1.setLayoutX(bg2.getLayoutX() + bg2.getFitWidth());
                }
                if (bg2.getLayoutX() <= -bg2.getFitWidth()) {
                    bg2.setLayoutX(bg1.getLayoutX() + bg1.getFitWidth());
                }
            }
        };
        bgTimer.start();
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

        if (randomEvent.getType() == EventType.SKILL) {
            if (randomEvent == RandomEvent.BREAKTHROUGH_SESSION && affectedArtist != null && affectedArtist.getSkill() != null) {
                return "New Skill: " + affectedArtist.getSkill().getName();
            }

            String valuePrefix = randomEvent.getValue() > 0 ? "+" : "";
            return "Skill Change: " + valuePrefix + randomEvent.getValue();
        }

        String valuePrefix = randomEvent.getValue() > 0 ? "+" : "";
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
        if (shouldNavigateToTourResults()) {
            screenNavigator.navigate(event, "/fxml/results/TourResults.fxml",
                    new TourResultsController(gameEnvironment, tourService, staminaLoss));
            return;
        }

        screenNavigator.navigate(event, "/fxml/tour/MainGame.fxml",
                new MainGameController(gameEnvironment, tourService));
    }

    private boolean shouldNavigateToTourResults() {
        return staminaLoss != null || tourService.isTourComplete() || tourService.isEndedByExhaustion();
    }
}
