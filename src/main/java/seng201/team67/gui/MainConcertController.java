package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.team67.GameEnviroment;
import seng201.team67.gui.controllers.instantiable.QuestionController;
import seng201.team67.gui.controllers.instantiable.tourmaps.WorldTourController;
import seng201.team67.models.Artist;
import seng201.team67.models.Concert;
import seng201.team67.models.questionmodels.Answer;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.ConcertService;
import seng201.team67.services.TourService;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class MainConcertController {

    private GameEnviroment gameEnviroment;
    private Concert concert;
    private TourService tourService;
    private ConcertService concertService;

    @FXML
    private Label labelName;
    @FXML private Label moneyText;
    @FXML private Label expeditionCount;
    @FXML private Label payText;

    @FXML private Slider crowdMeter;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private AnchorPane eventBox;//we load the questions in here

    private Stage stage;
    private Scene scene;
    private Parent root;


    public MainConcertController(GameEnviroment gameEnviroment, TourService tourService)
    {
        this.gameEnviroment = gameEnviroment;
        this.tourService = tourService;
    }

    @FXML public void initialize() throws IOException {
        labelName.setText(gameEnviroment.getLabelService().getLabelName());
        moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));

        loadLineup();
        concertService = new ConcertService(gameEnviroment, tourService);

        populateQuestion();
    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnviroment.getLabelService().getLineup();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            cards[i].getChildren().clear();
            cards[i].setStyle("-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;");
            if (i < pool.size()) {
                Artist artist = pool.get(i);
                populateCard(cards[i], artist);
            }
        }
    }

    private void populateCard(VBox card, Artist artist)
    {
        Label nameLabel = new Label(artist.getName());
        Label typeLabel = new Label(artist.getType());
        Label starPowerLabel = new Label("Star Power: " + artist.getStar_power());
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

    private void refreshView()
    {
        //refresh UI components
        //crowd meter. is there a more efficent way of doing this?
        crowdMeter.setBlockIncrement(concertService.getCrowdEnergyChange());
        crowdMeter.increment();

        payText.setText(Double.toString(concertService.getIncome()));
    }

    private void handleAnswer(Answer answer) {
        try {
            concertService.handleAnswer(answer);
            refreshView();
            populateQuestion();
        } catch (IOException e)
        {
            throw new RuntimeException("Next question couldn't load", e);
        }
    }


    private void handleConcertEnd() throws IOException
    {
        concertService.getTourService().setConcertFinished();
        concertService.getTourService().increaseStopIndex();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConcertResults.fxml"));
        loader.setController(new ResultsController(gameEnviroment, concertService));

        Parent root = loader.load();
        stage = (Stage)  eventBox.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
