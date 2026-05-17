package seng201.team67.gui.tour;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.dev.DevFunctionsInGameController;
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
import seng201.team67.models.items.CosumableItem;
import seng201.team67.models.items.Item;
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
    @FXML private Label effectText;

    @FXML private Slider crowdMeter;
    @FXML private ImageView micThumb;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private AnchorPane eventBox;

    @FXML private ImageView bg1;
    @FXML private ImageView bg2;

    private double bgSpeed = 0.5;
    private Parent devOverlay;
    private boolean devOverlayVisible = false;

    public MainConcertController(GameEnvironment gameEnvironment, TourService tourService)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
    }

    @FXML public void initialize() throws IOException {

        setTourBackground();
        effectText.setVisible(false);
        effectText.setText("");

        startBackgroundAnimation();

        loadLineup();
        concertService = new ConcertService(gameEnvironment, tourService);

        crowdMeter.valueProperty().addListener((obs, oldVal, newVal) -> updateMicPosition());

        populateQuestion();
        refreshView();

        Platform.runLater(() -> {
            updateMicPosition();
            attachDevToggleShortcut();
        });
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
                if (!gameEnvironment.getConfig().movingBackgroundEnabled) {
                    bg1.setLayoutX(0);
                    bg2.setLayoutX(bg1.getFitWidth());
                    return;
                }

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
        int activeArtistIndex = getCurrentAnsweringArtistIndex(pool);

        for (int i = 0; i < cards.length; i++) {
            if (i < pool.size()) {
                cards[i].setDisable(false);
                Artist artist = pool.get(i);
                ArtistDetailBoxFiller.populateArtistBox(cards[i], artist, null, item -> handleItemUse(artist, item));
                if (i == activeArtistIndex) {
                    highlightArtistName(cards[i]);
                }
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

    private void handleItemUse(Artist artist, Item item)
    {
        if (!(item instanceof CosumableItem))
        {
            return;
        }

        String result = gameEnvironment.getLabelService().useConsumable(artist, item);
        if (result.isBlank())
        {
            return;
        }

        effectText.setText(result);
        effectText.setVisible(true);
        refreshView();
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

        showDevOverlayIfVisible();
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
        showDevOverlayIfVisible();
    }

    private void refreshView()
    {
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
        payText.setText(String.format("$%.2f", tourService.getCreditsEarned()));
        loadLineup();

        crowdMeter.setValue(concertService.getCrowdEnergy());

        payText.setText(String.format("$%.2f", concertService.getIncome()));

        updateMicPosition();
    }

    private int getCurrentAnsweringArtistIndex(List<Artist> lineup)
    {
        if (lineup.isEmpty()) {
            return -1;
        }
        return Math.floorMod(tourService.getCurrentLineupStaminaIndex(), lineup.size());
    }

    private void highlightArtistName(VBox card)
    {
        if (card.getChildren().isEmpty()) {
            return;
        }

        Node container = card.getChildren().getFirst();
        if (!(container instanceof HBox outerRow) || outerRow.getChildren().size() < 2) {
            return;
        }

        Node detailsRowNode = outerRow.getChildren().get(1);
        if (!(detailsRowNode instanceof HBox detailsRow) || detailsRow.getChildren().isEmpty()) {
            return;
        }

        Node detailsBoxNode = detailsRow.getChildren().getFirst();
        if (!(detailsBoxNode instanceof VBox detailsBox) || detailsBox.getChildren().isEmpty()) {
            return;
        }

        Node nameNode = detailsBox.getChildren().getFirst();
        if (nameNode instanceof Label nameLabel) {
            nameLabel.setTextFill(Color.web("#32CD32"));
        }
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
        viewLoader.loadInto(eventBox, "/fxml/events/OutcomeEvent.fxml",
                new OutcomeController(gameEnvironment, tourService, outcome, this::onOutcomeContinue));
        showDevOverlayIfVisible();
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

    private void attachDevToggleShortcut()
    {
        if (labelName.getScene() == null)
        {
            return;
        }

        labelName.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.D)
            {
                toggleDevOverlay();
                event.consume();
            }
        });
    }

    private void toggleDevOverlay()
    {
        devOverlayVisible = !devOverlayVisible;
        if (devOverlayVisible)
        {
            showDevOverlayIfVisible();
        }
        else if (devOverlay != null)
        {
            eventBox.getChildren().remove(devOverlay);
        }
    }

    private void showDevOverlayIfVisible()
    {
        if (!devOverlayVisible)
        {
            return;
        }

        if (devOverlay == null)
        {
            devOverlay = viewLoader.load("/fxml/devui/DevFunctionsInGame.fxml",
                    new DevFunctionsInGameController(this));
        }

        if (!eventBox.getChildren().contains(devOverlay))
        {
            eventBox.getChildren().add(devOverlay);
        }

        AnchorPane.setTopAnchor(devOverlay, 0.0);
        AnchorPane.setRightAnchor(devOverlay, 0.0);
        AnchorPane.setBottomAnchor(devOverlay, 0.0);
        AnchorPane.setLeftAnchor(devOverlay, 0.0);
    }

    public void debugSetCrowd(int crowd)
    {
        concertService.setCrowdEnergyForDebug(crowd);
        refreshView();
    }

    public void debugSetAnsweredQuestions(int answeredQuestions)
    {
        concertService.setAnsweredQuestionCountForDebug(answeredQuestions);
        refreshView();
    }

    public void debugForceGoodOutcome()
    {
        concertService.setLastEventWonForDebug(true);
        concertService.setWinStreakForDebug(1);
        refreshView();
    }

    public void debugSetWinStreak(int winStreak)
    {
        concertService.setWinStreakForDebug(winStreak);
        concertService.setLastEventWonForDebug(winStreak > 0);
        refreshView();
    }

    public void debugApplyItemConcertModifiers()
    {
        concertService.applyItemConcertModifiersForDebug();
        refreshView();
    }

    public void debugSetRetirementRiskForAll(int retirementRisk)
    {
        for (Artist artist : gameEnvironment.getLabelService().getAllArtists())
        {
            artist.increaseRetirementChance(retirementRisk - artist.getRetirementChance());
        }
        refreshView();
    }
}
