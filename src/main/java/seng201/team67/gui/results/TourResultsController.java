package seng201.team67.gui.results;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.artists.Artist;
import seng201.team67.services.gameplay.ScoreService;
import seng201.team67.services.gameplay.TourService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Controls the tour results view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class TourResultsController {

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;
    /** Service used to manage tour behaviour. */
    private TourService tourService;
    /** Service used to manage score behaviour. */
    private final ScoreService scoreService = new ScoreService();
    /** Whether stamina loss. */
    private Boolean staminaLoss;

    /** FXML reference for the artist pane control. */
    @FXML private SplitPane artistPane;
    /** FXML reference for the artist card one control. */
    @FXML private VBox artistCardOne;
    /** FXML reference for the artist card two control. */
    @FXML private VBox artistCardTwo;
    /** FXML reference for the artist card three control. */
    @FXML private VBox artistCardThree;
    /** FXML reference for the artist card one1 control. */
    @FXML private VBox artistCardOne1;
    /** FXML reference for the artist card two1 control. */
    @FXML private VBox artistCardTwo1;
    /** FXML reference for the artist card three1 control. */
    @FXML private VBox artistCardThree1;

    /** FXML reference for the label tour name control. */
    @FXML private Label labelTourName;
    /** FXML reference for the label base revenue control. */
    @FXML private Label labelBaseRevenue;
    /** FXML reference for the label event payouts control. */
    @FXML private Label labelEventPayouts;
    /** FXML reference for the label event penalties control. */
    @FXML private Label labelEventPenalties;
    /** FXML reference for the label gross control. */
    @FXML private Label labelGross;
    /** FXML reference for the label pay description control. */
    @FXML private Label labelPayDescription;
    /** FXML reference for the label artist pay control. */
    @FXML private Label labelArtistPay;
    /** FXML reference for the label net earned control. */
    @FXML private Label labelNetEarned;
    /** FXML reference for the label credits before control. */
    @FXML private Label labelCreditsBefore;
    /** FXML reference for the label credits after control. */
    @FXML private Label labelCreditsAfter;
    /** FXML reference for the pay text control. */
    @FXML private Label payText;

    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** The multiplier format. */
    private static final DecimalFormat MULTIPLIER_FORMAT = new DecimalFormat("0.##");

    /**
     * Creates a new tour results controller.
     * @param gameEnvironment the active game environment
     * @param tourService the tour service for the current run
     * @param staminaLoss whether stamina loss
     */
    public TourResultsController(GameEnvironment gameEnvironment, TourService tourService, Boolean staminaLoss) {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
        this.staminaLoss = staminaLoss;
    }

    @FXML private void initialize() {
        populateTourResults();

        if (!tourService.isTourComplete() || staminaLoss) {
            labelTourName.setText(formatTourName() + " Tour - Ended Early");
        } else {
            labelTourName.setText(formatTourName() + " Tour - Finished");
        }
    }


    private void populateTourResults() {
        List<ConcertResults> concertResults = tourService.getConcertResults();
        double baseRevenue = concertResults.stream()
                .mapToDouble(result -> result.ticketSales)
                .sum();
        double eventPayouts = concertResults.stream()
                .mapToDouble(result -> Math.max(0, result.bonusMoney))
                .sum();
        double eventPenalties = concertResults.stream()
                .mapToDouble(result -> Math.max(0, -result.bonusMoney))
                .sum();

        if (tourService.getExhaustionRefund() > 0) {
            eventPenalties += tourService.getExhaustionRefund();
        }
        if (tourService.getCancellationRefund() > 0) {
            eventPenalties += tourService.getCancellationRefund();
        }

        double gross = baseRevenue + eventPayouts - eventPenalties;
        double artistPay = concertResults.stream()
                .mapToDouble(result -> result.artistsPay)
                .sum();
        double netEarned = tourService.getCreditsEarned();
        double creditsAfter = gameEnvironment.getLabelService().getMoney();
        double creditsBefore = creditsAfter - netEarned;

        labelBaseRevenue.setText("+ " + formatMoneyValue(baseRevenue));
        labelEventPayouts.setText("+ " + formatMoneyValue(eventPayouts));
        labelEventPenalties.setText("- " + formatMoneyValue(eventPenalties));
        labelGross.setText(formatMoney(gross));
        labelPayDescription.setText("Artist pay (" + gameEnvironment.getDifficulty().getDisplayName()
                + " Difficulty x" + MULTIPLIER_FORMAT.format(gameEnvironment.getDifficulty().getPayMultiplier())
                + ", " + formatTourName() + " Tour x" + MULTIPLIER_FORMAT.format(tourService.getTourArtistPayMultiplier()) + ")");
        labelArtistPay.setText("- " + formatMoneyValue(artistPay));
        labelNetEarned.setText(formatMoney(netEarned));
        labelCreditsBefore.setText(formatMoney(creditsBefore));
        labelCreditsAfter.setText(formatMoney(creditsAfter));

        double totalAmount = netEarned;
        payText.setText(String.format("$%.2f", 0.0));
        animateCount(payText, totalAmount, 1.0);
    }

    private String formatMoney(double amount) {
        return String.format("$%.2f", amount);
    }

    private String formatMoneyValue(double amount) {
        return String.format("%.2f", amount);
    }

    private String formatTourName() {
        String tourName = tourService.getTourType().toString();
        return Character.toUpperCase(tourName.charAt(0)) + tourName.substring(1);
    }

    private void animateCount(Label label, double target, double seconds) {
        long durationMs = (long) (seconds * 1000);
        long startTime = System.currentTimeMillis();

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(16), e -> {
                    long elapsed = System.currentTimeMillis() - startTime;
                    double progress = Math.min(1.0, (double) elapsed / durationMs);
                    double eased = 1 - Math.pow(1 - progress, 3);

                    double current = eased * target;
                    label.setText(String.format("$%.2f", current));
                })
        );
        timeline.setCycleCount((int) (durationMs / 16) + 1);
        timeline.setOnFinished(e -> label.setText(String.format("$%.2f", target)));
        timeline.play();
    }

    @FXML private void continueGame(ActionEvent event) throws IOException {
        gameEnvironment.increaseTours();
        scoreService.applyTourScore(gameEnvironment, tourService);

        boolean finishedSelectedExpeditions = gameEnvironment.getTourCount() >= gameEnvironment.getSelectedNumTours();
        boolean completedSuccessfully = tourService.isTourComplete() && !staminaLoss;

        if (finishedSelectedExpeditions && completedSuccessfully) {
            screenNavigator.navigate(event, "/fxml/results/WinScreen.fxml", new WinScreenController(gameEnvironment));
            return;
        }

        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
