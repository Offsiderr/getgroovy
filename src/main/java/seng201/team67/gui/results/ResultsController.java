package seng201.team67.gui.results;

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

public class ResultsController {

    private GameEnvironment gameEnvironment;
    private ConcertService concertService;

    //FXML stuff
    @FXML
    private SplitPane artistPane;
    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private Label ticketSales;
    @FXML private Label payText; //bonus money
    @FXML private Label crowdHype;
    @FXML private Label artistPay;
    @FXML private Label totalMoney;
    @FXML private Label difficultyType;
    @FXML private Label concertType;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final RandomEventService randomEventService = new RandomEventService();
    private static final DecimalFormat MULTIPLIER_FORMAT = new DecimalFormat("0.##");

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
        configureDifficultyMultiplier();
        configureTourMultiplier();

        double totalAmount = results.total;
        totalMoney.setText(String.format("$%.2f", 0.0));
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
            navigateToPostTourScreen(event, true);
            return;
        }

        screenNavigator.navigate(event, "/fxml/tour/MainGame.fxml",
                new MainGameController(gameEnvironment, concertService.getTourService()));
    }

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
                })
        );
        timeline.setCycleCount((int) (durationMs / 16) + 1);
        timeline.setOnFinished(e -> label.setText(String.format("$%.2f", target)));
        timeline.play();
    }
}
