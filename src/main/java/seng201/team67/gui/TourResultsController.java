package seng201.team67.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.artists.Artist;
import seng201.team67.services.gameplay.TourService;

import java.io.IOException;
import java.text.DecimalFormat;
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

    public TourResultsController(GameEnvironment gameEnvironment, TourService tourService, Boolean staminaLoss)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
        this.staminaLoss = staminaLoss;
    }

    @FXML private void initialize()
    {
        loadLineup();
        populateTourResults();

        if (staminaLoss)
        {
            labelTourName.setText(formatTourName() + " Tour - Ended Early");
        }
        else
        {
            labelTourName.setText(formatTourName() + " Tour - Finished");
        }
    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnvironment.getLabelService().getLineup();
        List<VBox> cards = List.of(artistCardOne, artistCardTwo, artistCardThree);
        configureArtistPane(cards, pool.size());
        artistPane.setVisible(!pool.isEmpty());
        artistPane.setManaged(!pool.isEmpty());

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

    private void populateTourResults()
    {
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

        if (tourService.getExhaustionRefund() > 0)
        {
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
                + " Difficulty x" + MONEY_FORMAT.format(gameEnvironment.getDifficulty().getPayMultiplier()) + ")");
        labelArtistPay.setText("- " + formatMoneyValue(artistPay));
        labelNetEarned.setText(formatMoney(netEarned));
        labelCreditsBefore.setText(formatMoney(creditsBefore));
        labelCreditsAfter.setText(formatMoney(creditsAfter));

        int totalAmount = (int) Math.round(netEarned);
        payText.setText("$0");
        animateCount(payText, totalAmount, 1.0);
    }

    private String formatMoney(double amount)
    {
        return "$" + MONEY_FORMAT.format(amount);
    }

    private String formatMoneyValue(double amount)
    {
        return MONEY_FORMAT.format(amount);
    }

    private String formatTourName()
    {
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

        boolean finishedSelectedExpeditions = gameEnvironment.getTourCount() >= gameEnvironment.getSelectedNumTours();
        boolean completedSuccessfully = tourService.isTourComplete() && !staminaLoss;

        if (finishedSelectedExpeditions && completedSuccessfully)
        {
            screenNavigator.navigate(event, "/fxml/WinScreen.fxml", new WinScreenController(gameEnvironment));
            return;
        }

        screenNavigator.navigate(event, "/fxml/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
