package seng201.team67.gui.tour;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.questions.OutcomeController;
import seng201.team67.gui.instantiable.questions.QuestionController;
import seng201.team67.gui.instantiable.minigames.SoundEngineerStandoffController;
import seng201.team67.gui.instantiable.minigames.CrowdWaveController;
import seng201.team67.gui.instantiable.minigames.CrowdHypeController;
import seng201.team67.gui.instantiable.minigames.MicTimingController;
import seng201.team67.gui.results.ResultsController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.minigames.MiniGameResult;
import seng201.team67.models.enums.Minigame;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.gameplay.ConcertService;
import seng201.team67.services.gameplay.MinigamesService;
import seng201.team67.services.gameplay.ScoreService;
import seng201.team67.services.gameplay.TourService;

import java.io.IOException;
import java.util.List;

public class MainConcertController {

    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private ConcertService concertService;
    private final ScoreService scoreService = new ScoreService();
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    @FXML private Label labelName;
    @FXML private Label moneyText;
    @FXML private Label expeditionCount;
    @FXML private Label payText;
    @FXML private Label staminaText;

    @FXML private Slider crowdMeter;
    @FXML private ImageView micThumb;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private AnchorPane eventBox;

    @FXML private ImageView bg1;
    @FXML private ImageView bg2;

    private double bgSpeed = 0.5;

    public MainConcertController(GameEnvironment gameEnvironment, TourService tourService)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
    }

    @FXML public void initialize() throws IOException {

        setTourBackground();

        startBackgroundAnimation();

        loadLineup();
        concertService = new ConcertService(gameEnvironment, tourService);

        crowdMeter.valueProperty().addListener((obs, oldVal, newVal) -> updateMicPosition());

        populateQuestion();
        refreshView();

        Platform.runLater(() -> updateMicPosition());
    }

    private void updateMicPosition() {
        double min = crowdMeter.getMin();
        double max = crowdMeter.getMax();
        double value = crowdMeter.getValue();

        double percent = (value - min) / (max - min);

        double sliderHeight = crowdMeter.getHeight();

        double y = (1 - percent) * sliderHeight;

        micThumb.setLayoutY(crowdMeter.getLayoutY() + y - micThumb.getFitHeight() / 2);
        micThumb.setLayoutX(1158 + 16 - (micThumb.getFitWidth() / 2));
    }

    private void setTourBackground() {

        String imagePath = "/images/gameBackground.png";

        bgSpeed = 0.5;

        bg1.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        bg2.setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }

    private void startBackgroundAnimation() {
        AnimationTimer bgTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                bg1.setLayoutX(bg1.getLayoutX() - bgSpeed);
                bg2.setLayoutX(bg2.getLayoutX() - bgSpeed);

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

    private void makeSilhouette(ImageView imageView) {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setBrightness(-1.0);
        adjust.setContrast(0.0);
        adjust.setSaturation(-1.0);

        DropShadow glow = new DropShadow();
        glow.setColor(javafx.scene.paint.Color.web("#ffffff"));
        glow.setRadius(15);
        glow.setSpread(0.3);

        glow.setInput(adjust);

        imageView.setEffect(glow);
    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnvironment.getLabelService().getLineup();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            if (i < pool.size()) {
                cards[i].setDisable(false);
                ArtistDetailBoxFiller.populateArtistBox(cards[i], pool.get(i), null);
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

    private void populateQuestion() throws IOException {
        eventBox.getChildren().clear();

        if (concertService.isEnded())
        {
            handleConcertEnd();
            return;
        }

        Minigame miniGame = concertService.getConcertMinigame();
        if(miniGame != null)
        {
            startMinigame(miniGame);
            return;
        }

        Question question = concertService.getNextQuestion();
        if(question == null)
        {
            handleConcertEnd();
            return;
        }

        QuestionController questionController = new QuestionController(question, this::handleAnswer, tourService);
        viewLoader.loadInto(eventBox, "/fxml/events/QuestionEvent.fxml", questionController);

        AnchorPane questionPane = (AnchorPane) eventBox.getChildren().get(0);

        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        for (int i = 0; i < lineup.size(); i++) {
            ImageView artistView = new ImageView(new Image(getClass().getResourceAsStream(lineup.get(i).getImagePath())));
            artistView.setFitHeight(150);
            artistView.setPreserveRatio(true);
            makeSilhouette(artistView);
            artistView.setLayoutX(170 + i * 150);

            double yOffset = 180;
            if (tourService.getTourType() == seng201.team67.models.enums.TourType.COUNTRY) {
                yOffset = 230;
            }
            if (tourService.getTourType() == seng201.team67.models.enums.TourType.WORLD) {
                yOffset = 260;
            }

            artistView.setLayoutY(yOffset);

            questionPane.getChildren().add(1, artistView);
        }
    }

    private void startMinigame(Minigame minigame) throws IOException {
        Object controller;

        switch (minigame)
        {
            case SOUNDENGINEER -> {
                controller = new SoundEngineerStandoffController(new MinigamesService(minigame), this::onMiniGameComplete, gameEnvironment);
            }
            case CROWDWAVE -> {
                controller = new CrowdWaveController(this::onMiniGameComplete);
            }
            case CROWDHYPE -> {
                controller = new CrowdHypeController(this::onMiniGameComplete);
            }
            case MICTIMING -> {
                controller = new MicTimingController(this::onMiniGameComplete);
            }
            default -> {
                return;
            }
        }
        viewLoader.loadInto(eventBox, minigame.path(), controller);
    }

    private void refreshView()
    {
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
        payText.setText(Double.toString(tourService.getCreditsEarned()));

        crowdMeter.setValue(concertService.getCrowdEnergyChange());

        payText.setText(Double.toString(concertService.getIncome()));

        updateMicPosition();
    }

    private void handleAnswer(Answer answer) {
        try {
            Outcome outcome = concertService.handleAnswer(answer);
            refreshView();
            loadOutcome(outcome);
        } catch (IOException e) {
            throw new RuntimeException("Outcome view couldn't load", e);
        }
    }

    private void loadOutcome(Outcome outcome) throws IOException {
        eventBox.getChildren().clear();
        viewLoader.loadInto(eventBox, "/fxml/events/OutcomeEvent.fxml", new OutcomeController(outcome, this::onOutcomeContinue));
    }

    private void onOutcomeContinue() {
        try {
            populateQuestion();
        } catch (IOException e) {
            throw new RuntimeException("Next question couldn't load", e);
        }
    }

    private void onMiniGameComplete(MiniGameResult result)
    {
        concertService.applyMiniGameResult(result);
        refreshView();
        try {
            populateQuestion();
        } catch (IOException e)
        {
            throw  new RuntimeException("Next question couldn't load", e);
        }
    }

    private void handleConcertEnd() throws IOException
    {
        gameEnvironment.increaseConcertCount();
        var results = concertService.createConcertResults();
        scoreService.applyConcertScore(gameEnvironment, concertService, results);
        concertService.getTourService().addConcertResult(results);
        concertService.getTourService().setConcertFinished();
        concertService.getTourService().increaseStopIndex();
        if (!concertService.getTourService().isTourComplete()
                && concertService.getTourService().isLineupExhausted())
        {
            concertService.getTourService().endTourDueToExhaustion();
        }

        screenNavigator.navigate(eventBox, "/fxml/results/ConcertResults.fxml", new ResultsController(gameEnvironment, concertService));
    }
}