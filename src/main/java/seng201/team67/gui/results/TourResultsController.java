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

public class TourResultsController {

    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private final ScoreService scoreService = new ScoreService();
    private Boolean staminaLoss;

    @FXML private SplitPane artistPane;
    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;
    @FXML private VBox artistCardOne1;
    @FXML private VBox artistCardTwo1;
    @FXML private VBox artistCardThree1;

    @FXML private Label labelTourName;
    @FXML private Label labelBaseRevenue;
    @FXML private Label labelEventPayouts;
    @FXML private Label labelEventPenalties;
    @FXML private Label labelGross;
    @FXML private Label labelPayDescription;
    @FXML private Label labelArtistPay;
    @FXML private Label labelNetEarned;
    @FXML private Label labelCreditsBefore;
    @FXML private Label labelCreditsAfter;
    @FXML private Label payText;

    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.##");

    public TourResultsController(GameEnvironment gameEnvironment, TourService tourService, Boolean staminaLoss) {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
        this.staminaLoss = staminaLoss;
    }

    @FXML private void initialize() {
        populateTourResults();

        if (staminaLoss) {
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
                + " Difficulty x" + MONEY_FORMAT.format(gameEnvironment.getDifficulty().getPayMultiplier())
                + ", " + formatTourName() + " Tour x" + MONEY_FORMAT.format(tourService.getTourArtistPayMultiplier()) + ")");
        labelArtistPay.setText("- " + formatMoneyValue(artistPay));
        labelNetEarned.setText(formatMoney(netEarned));
        labelCreditsBefore.setText(formatMoney(creditsBefore));
        labelCreditsAfter.setText(formatMoney(creditsAfter));

        int totalAmount = (int) Math.round(netEarned);
        payText.setText("$0");
        animateCount(payText, totalAmount, 1.0);
    }

    private String formatMoney(double amount) {
        return "$" + MONEY_FORMAT.format(amount);
    }

    private String formatMoneyValue(double amount) {
        return MONEY_FORMAT.format(amount);
    }

    private String formatTourName() {
        String tourName = tourService.getTourType().toString();
        return Character.toUpperCase(tourName.charAt(0)) + tourName.substring(1);
    }

    private void animateCount(Label label, int target, double seconds) {
        long durationMs = (long) (seconds * 1000);
        long startTime = System.currentTimeMillis();

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(16), e -> {
                    long elapsed = System.currentTimeMillis() - startTime;
                    double progress = Math.min(1.0, (double) elapsed / durationMs);
                    double eased = 1 - Math.pow(1 - progress, 3);

                    int current = (int) Math.round(eased * target);
                    label.setText("$" + current);
                })
        );
        timeline.setCycleCount((int) (durationMs / 16) + 1);
        timeline.setOnFinished(e -> label.setText("$" + target));
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
