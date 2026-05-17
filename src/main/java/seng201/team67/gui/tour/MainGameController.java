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

public class MainGameController {

    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private SoundEffectsService sfx;

    private Boolean initStops = false;

    @FXML private Label labelName;
    @FXML private Label moneyText;
    @FXML private Label expeditionCount;
    @FXML private Label payText;
    @FXML private Label cancelTourLabel;
    @FXML private Label staminaText;
    @FXML private Label effectText;

    @FXML private ProgressBar tourProgressBar;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private Button startConcertButton;
    @FXML private Button endTourEarlyButton;

    @FXML private AnchorPane mapAnchorPane;
    @FXML private AnchorPane cancelTourPane;

    @FXML private javafx.scene.image.ImageView bg1;
    @FXML private javafx.scene.image.ImageView bg2;

    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();
    private final RandomEventService randomEventService = new RandomEventService();

    private double scroll = 0;

    public MainGameController(GameEnvironment gameEnvironment, TourService tourService)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
    }

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
    }

    private void startBackgroundAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameEnvironment.getConfig().movingBackgroundEnabled) {
                    scroll = 0;
                    bg1.setTranslateX(0);
                    bg2.setTranslateX(bg1.getFitWidth());
                    return;
                }

                scroll += 0.3;
                bg1.setTranslateX(-scroll);
                bg2.setTranslateX(-scroll + bg1.getFitWidth());

                if (scroll >= bg1.getFitWidth()) {
                    scroll = 0;
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

    public void endTourEarly()
    {

    }

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

    @FXML private void notCancellingTour(ActionEvent event) throws IOException
    {
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
