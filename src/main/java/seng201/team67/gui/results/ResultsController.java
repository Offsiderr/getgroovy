package seng201.team67.gui.results;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.tour.MainGameController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.artists.Artist;
import seng201.team67.services.gameplay.ConcertService;
import seng201.team67.services.gameplay.RandomEventService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Controls the results view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ResultsController {
    /** The positive amount color. */
    private static final Paint POSITIVE_AMOUNT_COLOR = Paint.valueOf("#00ff4d");
    /** The negative amount color. */
    private static final Paint NEGATIVE_AMOUNT_COLOR = Paint.valueOf("#ff4d4d");

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;
    /** Service used to manage concert behaviour. */
    private ConcertService concertService;

    //FXML stuff
    /** FXML reference for the artist pane control. */
    @FXML
    private SplitPane artistPane;
    /** FXML reference for the artist card one control. */
    @FXML private VBox artistCardOne;
    /** FXML reference for the artist card two control. */
    @FXML private VBox artistCardTwo;
    /** FXML reference for the artist card three control. */
    @FXML private VBox artistCardThree;

    /** FXML reference for the ticket sales control. */
    @FXML private Label ticketSales;
    /** FXML reference for the pay text control. */
    @FXML private Label payText; //bonus money
    /** FXML reference for the crowd hype control. */
    @FXML private Label crowdHype;
    /** FXML reference for the artist pay control. */
    @FXML private Label artistPay;
    /** FXML reference for the total money control. */
    @FXML private Label totalMoney;
    /** FXML reference for the difficulty type control. */
    @FXML private Label difficultyType;
    /** FXML reference for the concert type control. */
    @FXML private Label concertType;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** Service used to manage random event behaviour. */
    private final RandomEventService randomEventService = new RandomEventService();
    /** The multiplier format. */
    private static final DecimalFormat MULTIPLIER_FORMAT = new DecimalFormat("0.##");

    /**
     * Creates a new results controller.
     * @param gameEnvironment the active game environment
     * @param concertService the concert service to use
     */
    public ResultsController(GameEnvironment gameEnvironment, ConcertService concertService)
    {
        this.gameEnvironment = gameEnvironment;
        this.concertService = concertService;
    }

    @FXML private void initialize()
    {
        loadLineup();
        ConcertResults results = concertService.createConcertResults();

        payText.setText(formatMoney(results.bonusMoney));
        ticketSales.setText(formatMoney(results.ticketSales));
        crowdHype.setText(Integer.toString(results.crowdHype));
        artistPay.setText("-" + formatMoney(results.artistsPay));
        applyAmountColor(ticketSales, results.ticketSales);
        applyAmountColor(payText, results.bonusMoney);
        applyAmountColor(artistPay, -results.artistsPay);
        configureDifficultyMultiplier();
        configureTourMultiplier();

        double totalAmount = results.total;
        totalMoney.setText(String.format("$%.2f", 0.0));
        applyAmountColor(totalMoney, totalAmount);
        animateCount(totalMoney, totalAmount, 1.0);
    }

    private void configureDifficultyMultiplier()
    {
        double payMultiplier = gameEnvironment.getDifficulty().getPayMultiplier();
        boolean showMultiplier = payMultiplier != 1.0;
        difficultyType.setVisible(showMultiplier);
        difficultyType.setManaged(showMultiplier);

        if (showMultiplier)
        {
            difficultyType.setText("x" + MULTIPLIER_FORMAT.format(payMultiplier)
                    + " - " + gameEnvironment.getDifficulty().getDisplayName() + " Difficulty");
        }
    }

    private void configureTourMultiplier()
    {
        double payMultiplier = concertService.getTourService().getTourPayMultiplier();
        boolean showMultiplier = payMultiplier != 1.0;
        concertType.setVisible(showMultiplier);
        concertType.setManaged(showMultiplier);

        if (showMultiplier)
        {
            String tourName = concertService.getTourService().getTourType().toString();
            concertType.setText("x" + MULTIPLIER_FORMAT.format(payMultiplier)
                    + " - " + Character.toUpperCase(tourName.charAt(0)) + tourName.substring(1) + " Tour");
        }
    }

    private String formatMoney(double amount)
    {
        return String.format("$%.2f", amount);
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
                ArtistDetailBoxFiller.populateArtistBox(card, pool.get(i), null);
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
        if (concertService.getTourService().isEndedByExhaustion()) {
            concertService.getTourService().tourEnded();
            gameEnvironment.setArtistPoolGenerated(false);
            gameEnvironment.setItemPoolGenerated(false);
            navigateToPostTourScreen(event, true);
            return;
        }

        screenNavigator.navigate(event, "/fxml/tour/MainGame.fxml",
                new MainGameController(gameEnvironment, concertService.getTourService()));
    }

    /**
     * At the end of a tour, we have to roll to see if an artist retires based on their tolerance
     * and then roll for a random event
     * and if either of these pass, you get sent to them, then to the tour results screen.
     * @param event
     * @param staminaLoss
     * @throws IOException
     */
    private void navigateToPostTourScreen(ActionEvent event, boolean staminaLoss) throws IOException {
        Artist retiredArtist = randomEventService.rollRetirementArtist(gameEnvironment);
        if (retiredArtist != null) {
            screenNavigator.navigate(event, "/fxml/results/EventResult.fxml",
                    new RandomEventResultController(
                            gameEnvironment,
                            concertService.getTourService(),
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
                        new TourResultsController(gameEnvironment, concertService.getTourService(), staminaLoss));
                return;
            }

            screenNavigator.navigate(event, "/fxml/results/EventResult.fxml",
                    new RandomEventResultController(
                            gameEnvironment,
                            concertService.getTourService(),
                            randomEvent,
                            affectedArtist,
                            staminaLoss
                    ));
            return;
        }

        screenNavigator.navigate(event, "/fxml/results/TourResults.fxml",
                new TourResultsController(gameEnvironment, concertService.getTourService(), staminaLoss));
    }

    private void animateCount(Label label, double target, double seconds) {
        long durationMs = (long) (seconds * 1000);
        long startTime = System.currentTimeMillis();

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(16), e -> {  // ~60fps
                    long elapsed = System.currentTimeMillis() - startTime;
                    double progress = Math.min(1.0, (double) elapsed / durationMs);

                    // ease-out curve: feels snappier at start, settles at end
                    double eased = 1 - Math.pow(1 - progress, 3);

                    double current = eased * target;
                    label.setText(String.format("$%.2f", current));
                    applyAmountColor(label, current);
                })
        );
        timeline.setCycleCount((int) (durationMs / 16) + 1);
        timeline.setOnFinished(e -> {
            label.setText(String.format("$%.2f", target));
            applyAmountColor(label, target);
        });
        timeline.play();
    }

    private void applyAmountColor(Label label, double amount)
    {
        label.setTextFill(amount < 0 ? NEGATIVE_AMOUNT_COLOR : POSITIVE_AMOUNT_COLOR);
    }
}
