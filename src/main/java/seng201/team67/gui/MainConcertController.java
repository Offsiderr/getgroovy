package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.questions.OutcomeController;
import seng201.team67.gui.instantiable.questions.QuestionController;
import seng201.team67.gui.instantiable.minigames.SoundEngineerStandoffController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.Artist;
import seng201.team67.models.MiniGameResult;
import seng201.team67.models.enums.Minigame;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.ConcertService;
import seng201.team67.services.MinigamesService;
import seng201.team67.services.TourService;

import java.io.IOException;
import java.util.List;

public class MainConcertController {

    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private ConcertService concertService;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    @FXML
    private Label labelName;
    @FXML private Label moneyText;
    @FXML private Label expeditionCount;
    @FXML private Label payText;
    @FXML private Label staminaText;

    @FXML private Slider crowdMeter;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private AnchorPane eventBox;//we load the questions in here


    public MainConcertController(GameEnvironment gameEnvironment, TourService tourService)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
    }

    @FXML public void initialize() throws IOException {


        loadLineup();
        concertService = new ConcertService(gameEnvironment, tourService);

        populateQuestion();
        refreshView();

    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnvironment.getLabelService().getLineup();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            if (i < pool.size()) {
                cards[i].setDisable(false);
                ArtistDetailBoxFiller.populateArtistBox(cards[i], pool.get(i));
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

        Minigame miniGame = tourService.rollMiniGameTrigger((int) crowdMeter.getValue());
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

        QuestionController questionController = new QuestionController(question, this::handleAnswer);
        viewLoader.loadInto(eventBox, "/fxml/QuestionEvent.fxml", questionController);
    }

    private void startMinigame(Minigame minigame) throws IOException {
        Object controller;

        switch (minigame)
        {
            case SOUNDENGINEER -> {
                controller = new SoundEngineerStandoffController(new MinigamesService(minigame), this::onMiniGameComplete, gameEnvironment);
            }
            default -> throw new IllegalStateException("Unexpected minigame: " + minigame);

        }
        viewLoader.loadInto(eventBox, minigame.path(), controller);
    }

    private void refreshView()
    {
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
        payText.setText(Double.toString(tourService.getCreditsEarned()));

        //refresh UI components
        //crowd meter. is there a more efficent way of doing this?
        crowdMeter.setBlockIncrement(concertService.getCrowdEnergyChange());
        crowdMeter.increment();

        payText.setText(Double.toString(concertService.getIncome()));
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
        viewLoader.loadInto(eventBox, "/fxml/OutcomeEvent.fxml", new OutcomeController(outcome, this::onOutcomeContinue));
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
        concertService.getTourService().setConcertFinished();
        concertService.getTourService().increaseStopIndex();
        if (!concertService.getTourService().isTourComplete()
                && concertService.getTourService().isLineupExhausted())
        {
            concertService.getTourService().endTourDueToExhaustion();
        }

        screenNavigator.navigate(eventBox, "/fxml/ConcertResults.fxml", new ResultsController(gameEnvironment, concertService));
    }

}
