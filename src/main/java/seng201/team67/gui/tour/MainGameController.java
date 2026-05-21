package seng201.team67.gui.tour;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.tourmaps.TourMapController;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.results.RandomEventResultController;
import seng201.team67.gui.results.TourResultsController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.TourType;
import seng201.team67.models.items.Item;
import seng201.team67.services.audio.SoundEffectsService;
import seng201.team67.services.gameplay.RandomEventService;
import seng201.team67.services.gameplay.TourService;

import java.io.IOException;
import java.util.List;

/**
 * Controls the main game view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MainGameController {

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;
    /** Service used to manage tour behaviour. */
    private TourService tourService;
    /** Service used to manage sound effects behaviour. */
    private SoundEffectsService sfx;

    /** Whether init stops. */
    private Boolean initStops = false;

    /** FXML reference for the label name control. */
    @FXML private Label labelName;
    /** FXML reference for the money text control. */
    @FXML private Label moneyText;
    /** FXML reference for the expedition count control. */
    @FXML private Label expeditionCount;
    /** FXML reference for the pay text control. */
    @FXML private Label payText;
    /** FXML reference for the cancel tour label control. */
    @FXML private Label cancelTourLabel;
    /** FXML reference for the stamina text control. */
    @FXML private Label staminaText;
    /** FXML reference for the effect text control. */
    @FXML private Label effectText;

    /** The tour progress bar. */
    @FXML private ProgressBar tourProgressBar;

    /** FXML reference for the artist card one control. */
    @FXML private VBox artistCardOne;
    /** FXML reference for the artist card two control. */
    @FXML private VBox artistCardTwo;
    /** FXML reference for the artist card three control. */
    @FXML private VBox artistCardThree;

    /** FXML reference for the start concert button control. */
    @FXML private Button startConcertButton;
    /** FXML reference for the end tour early button control. */
    @FXML private Button endTourEarlyButton;

    /** FXML reference for the map anchor pane control. */
    @FXML private AnchorPane mapAnchorPane;
    /** FXML reference for the cancel tour pane control. */
    @FXML private AnchorPane cancelTourPane;

    /** The bg1. */
    @FXML private javafx.scene.image.ImageView bg1;
    /** The bg2. */
    @FXML private javafx.scene.image.ImageView bg2;

    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** The view loader. */
    private final ViewLoader viewLoader = new ViewLoader();
    /** Service used to manage random event behaviour. */
    private final RandomEventService randomEventService = new RandomEventService();

    /**
     * Creates a new main game controller.
     * @param gameEnvironment the active game environment
     * @param tourService the tour service for the current run
     */
    public MainGameController(GameEnvironment gameEnvironment, TourService tourService)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     * @throws IOException if an input or output error occurs
     */
    @FXML public void initialize() throws IOException {
        cancelTourPane.setVisible(false);
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
        payText.setText(String.format("$%.2f", tourService.getCreditsEarned()));

        staminaText.setText(Integer.toString((int) Math.round(tourService.getTotalStamina())));
        effectText.setText(tourService.getConditionalEffectText());
        effectText.setVisible(!tourService.getConditionalEffectText().isBlank());

        loadLineup();

        if(tourService.getConcertStatus())
        {
            initStops = true;
            concertFinished();
        }

        loadMap(tourService.getTourType());

        refreshTourActionButtons();

        tourProgressBar.setProgress((double) tourService.getStopIndex() / tourService.getTourType().getStops());

        startBackgroundAnimation();

        if (tourService.getTourType() == TourType.LOCAL)
        {
            gameEnvironment.getMusicService().playLocalTourMusic();
        }
    }

    private void startBackgroundAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double width = bg1.getBoundsInParent().getWidth();

                if (!gameEnvironment.getConfig().movingBackgroundEnabled) {
                    bg1.setLayoutX(0);
                    bg2.setLayoutX(width);
                    return;
                }

                bg1.setLayoutX(bg1.getLayoutX() - 0.3);
                bg2.setLayoutX(bg2.getLayoutX() - 0.3);

                if (bg1.getLayoutX() + width <= 0) {
                    bg1.setLayoutX(bg1.getLayoutX() + width * 2);
                }

                if (bg2.getLayoutX() + width <= 0) {
                    bg2.setLayoutX(bg2.getLayoutX() + width * 2);
                }
            }
        };
        timer.start();
    }

    private void refreshTourActionButtons()
    {
        boolean isTourComplete = tourService.isTourComplete();
        startConcertButton.setText(isTourComplete ? "Finish Tour" : "Start Concert");
        endTourEarlyButton.setDisable(isTourComplete);
    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnvironment.getLabelService().getLineup();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            if (i < pool.size()) {
                cards[i].setDisable(false);
                Artist artist = pool.get(i);
                ArtistDetailBoxFiller.populateArtistBox(cards[i], artist, null);
            } else {
                clearArtistCard(cards[i]);
            }
        }
    }

    private void clearArtistCard(VBox card) {
        card.getChildren().clear();
        card.setDisable(true);
        ArtistDetailBoxFiller.applyBaseStyle(card);
    }

    private void loadMap(TourType type) throws IOException {
        String path = "";
        switch (type)
        {
            case LOCAL:
                path = "/fxml/maps/regionMap.fxml";
                break;
            case COUNTRY:
                path = "/fxml/maps/countryMap.fxml";
                break;
            case WORLD:
                path = "/fxml/maps/worldMap.fxml";
        }

        TourMapController worldMapController = new TourMapController();
        viewLoader.loadInto(mapAnchorPane, path, worldMapController);

        TourMapController mapController = worldMapController;

        if (tourService.hasStopOrder()) {
            mapController.applyStopOrder(tourService.getStopOrder());
        } else {
            mapController.applyRandomOrder();
            tourService.setStopOrder(mapController.getStopOrder());
        }

        mapController.initialiseStops(tourService.getTourType().getStops());

        for (int i = 0; i < tourService.getStopIndex(); i++) {
            mapController.markStopCompleted(i);
        }
    }

    /**
     * Starts the concert.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void startConcert(ActionEvent event) throws IOException
    {
        if(tourService.isTourComplete())
        {
            finishTour(event);
            return;
        }

        screenNavigator.navigate(event, "/fxml/tour/MainConcert.fxml", new MainConcertController(gameEnvironment, tourService));
    }

    private boolean concertFinished()
    {
        return true;
    }

    private void returnToMainMenu(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

    @FXML private void endTourEarly(ActionEvent event) throws IOException
    {
        if (tourService.isTourComplete()) {
            return;
        }

        cancelTourPane.setVisible(true);
        cancelTourLabel.setText("Are you sure you want to end the tour early? You will have to refund $"
                + String.format("%.2f", gameEnvironment.getConfig().cancelTourPenalty) + " of tickets");
    }

    @FXML private void finishTour(ActionEvent event) throws IOException {
        if (cancelTourPane.isVisible() && !tourService.isTourComplete()) {
            tourService.addCreditsEarned((double) -gameEnvironment.getConfig().cancelTourPenalty);
        }

        tourService.tourEnded();
        gameEnvironment.setArtistPoolGenerated(false);
        navigateAfterTourEnd(event, tourService.isEndedByExhaustion());
    }

    private void navigateAfterTourEnd(ActionEvent event, boolean staminaLoss) throws IOException {
        Artist retiredArtist = randomEventService.rollRetirementArtist(gameEnvironment);
        if (retiredArtist != null) {
            screenNavigator.navigate(event, "/fxml/results/EventResult.fxml",
                    new RandomEventResultController(
                            gameEnvironment,
                            tourService,
                            "Artist Retiring",
                            retiredArtist.getName() + " is retiring immediately after this tour.",
                            retiredArtist,
                            staminaLoss
                    ));
            return;
        }

        if (randomEventService.shouldTriggerRandomEvent(gameEnvironment)) {
            var randomEvent = randomEventService.getWeightedRandomEvent(gameEnvironment);
            var affectedArtist = randomEventService.getRandomAffectedArtist(gameEnvironment, randomEvent);

            if (randomEvent == null || affectedArtist == null) {
                screenNavigator.navigate(event, "/fxml/results/TourResults.fxml",
                        new TourResultsController(gameEnvironment, tourService, staminaLoss));
                return;
            }

            screenNavigator.navigate(event, "/fxml/results/EventResult.fxml",
                    new RandomEventResultController(
                            gameEnvironment,
                            tourService,
                            randomEvent,
                            affectedArtist,
                            staminaLoss
                    ));
            return;
        }

        screenNavigator.navigate(event, "/fxml/results/TourResults.fxml",
                new TourResultsController(gameEnvironment, tourService, staminaLoss));
    }

    @FXML private void closePane(ActionEvent event) throws IOException
    {
        cancelTourPane.setVisible(false);
    }
}