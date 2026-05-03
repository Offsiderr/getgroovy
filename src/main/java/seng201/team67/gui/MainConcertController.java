package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.OutcomeController;
import seng201.team67.gui.controllers.instantiable.QuestionController;
import seng201.team67.gui.controllers.instantiable.SoundEngineerStandoffController;
import seng201.team67.models.Artist;
import seng201.team67.models.Concert;
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
    private Concert concert;
    private TourService tourService;
    private ConcertService concertService;
    private MinigamesService minigamesService;

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

    private Stage stage;
    private Scene scene;
    private Parent root;


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
            cards[i].getChildren().clear();
            if (i < pool.size()) {
                cards[i].setManaged(true);
                cards[i].setVisible(true);
                cards[i].setStyle("-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;");
                Artist artist = pool.get(i);
                populateCard(cards[i], artist);
            } else {
                cards[i].setManaged(false);
                cards[i].setVisible(false);
            }
        }
    }

    private void populateCard(VBox card, Artist artist)
    {
        Label nameLabel = new Label(artist.getName());
        Label typeLabel = new Label(artist.getType());
        Label starPowerLabel = new Label("Star Power: " + artist.getStarPower());
        Label staminaLabel = new Label("Stamina: " + artist.getStamina());
        Label healthLabel = new Label("Health: " + artist.getHealth());
        Label costLabel = new Label("Hire: $" + (int) artist.getCost());

        card.getChildren().addAll(nameLabel, typeLabel, starPowerLabel, staminaLabel, healthLabel, costLabel);
        card.setPadding(new Insets(8));
        card.setAlignment(Pos.CENTER);

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

        FXMLLoader questionLoader = new FXMLLoader(getClass().getResource("/fxml/QuestionEvent.fxml"));

        Question question = concertService.getNextQuestion();
        if(question == null)
        {
            handleConcertEnd();
            return;
        }

        QuestionController questionController = new QuestionController(question, this::handleAnswer);
        questionLoader.setController(questionController);
        Parent mapView = questionLoader.load();

        eventBox.getChildren().add(mapView);
    }

    private void startMinigame(Minigame minigame) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(minigame.path()));

        switch (minigame)
        {
            case SOUNDENGINEER -> {
                SoundEngineerStandoffController miniGameController = new SoundEngineerStandoffController(new MinigamesService(minigame), this::onMiniGameComplete, gameEnvironment);
                loader.setController(miniGameController);
            }

        }
        Parent view = loader.load();

        eventBox.getChildren().add(view);
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OutcomeEvent.fxml"));
        loader.setController(new OutcomeController(outcome, this::onOutcomeContinue));
        eventBox.getChildren().add(loader.load());
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConcertResults.fxml"));
        loader.setController(new ResultsController(gameEnvironment, concertService));

        Parent root = loader.load();
        stage = (Stage)  eventBox.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
