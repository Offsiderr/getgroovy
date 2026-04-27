package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.team67.GameEnviroment;
import seng201.team67.gui.controllers.instantiable.tourmaps.TourMapController;
import seng201.team67.models.Artist;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.SoundEffectsService;
import seng201.team67.services.TourService;

import java.io.IOException;
import java.util.List;

public class MainGameController {

    //TODO: implement different maps

    private GameEnviroment gameEnviroment;
    private TourService tourService;
    private SoundEffectsService sfx;

    private Boolean initStops = false;

    @FXML private Label labelName;
    @FXML private Label moneyText;
    @FXML private Label expeditionCount;
    @FXML private Label payText;
    @FXML private Label cancelTourLabel;
    @FXML private Label staminaText;

    @FXML private ProgressBar tourProgressBar;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private Button startConcertButton;

    @FXML private AnchorPane mapAnchorPane;
    @FXML private AnchorPane cancelTourPane;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public MainGameController(GameEnviroment gameEnviroment, TourService tourService)
    {
        this.gameEnviroment = gameEnviroment;
        this.tourService = tourService;
    }

    @FXML public void initialize() throws IOException {
        cancelTourPane.setVisible(false);
        labelName.setText(gameEnviroment.getLabelService().getLabelName());
        moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));
        payText.setText(Double.toString(tourService.getCreditsEarned()));

        staminaText.setText(Double.toString(tourService.getTotalStamina()));

        loadLineup();

        if(tourService.getConcertStatus())
        {
            initStops = true;
            concertFinished();
        }

        loadMap(tourService.getTourType());

        if(tourService.isTourComplete())
        {
            startConcertButton.setText("Finish Tour");
        }

        tourProgressBar.setProgress((double) tourService.getStopIndex() / tourService.getTourType().getStops());
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

    private void loadMap(TourType type) throws IOException {
        //TODO: make the world tour controller a class variable instead of local.
        String path = "";
        //TODO: something weird is up with this switch statement. investigate.
        switch (type)
        {
            case LOCAL:
                path = "/fxml/regionMap.fxml";
                break;
            case COUNTRY:
                path = "/fxml/countryMap.fxml";
                break;
            case WORLD:
                path = "/fxml/worldMap.fxml";
        }

        FXMLLoader mapLoader = new FXMLLoader(getClass().getResource(path));
        TourMapController worldMapController = new TourMapController();
        mapLoader.setController(worldMapController);
        Parent mapView = mapLoader.load();

        mapAnchorPane.getChildren().add(mapView);


        TourMapController mapController = mapLoader.getController();

        if (tourService.hasStopOrder()) {
            mapController.applyStopOrder(tourService.getStopOrder());
        } else {
            mapController.applyRandomOrder();
            tourService.setStopOrder(mapController.getStopOrder());
        }

        mapController.initialiseStops(tourService.getTourType().getStops());

        for (int i = 0; i < tourService.getStopIndex(); i++) {
            mapController.markStopCompleted(i);
        }

    }

    public void endTourEarly()
    {

    }

    @FXML public void startConcert(ActionEvent event) throws IOException
    {
        //sfx.playYes(); TODO: wire this up.

        if(tourService.isTourComplete())
        {
            finishTour(event);
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainConcert.fxml"));
        loader.setController(new MainConcertController(gameEnviroment, tourService));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private boolean concertFinished()
    {
        //Returns false if the tour is finished
        return true;
    }

    private void returnToMainMenu(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        loader.setController(new MainMenuController(gameEnviroment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void endTourEarly(ActionEvent event) throws IOException
    {
        cancelTourPane.setVisible(true);
        cancelTourLabel.setText("Are you sure you want to end the tour early? You will have to refund $" + gameEnviroment.getConfig().cancelTourPenalty + " of tickets");
        tourService.addCreditsEarned((double) -gameEnviroment.getConfig().cancelTourPenalty);
    }

    @FXML private void notCancellingTour(ActionEvent event) throws IOException
    {
        tourService.addCreditsEarned((double) gameEnviroment.getConfig().cancelTourPenalty);
    }


     @FXML private void finishTour(ActionEvent event) throws IOException {

         tourService.tourEnded();
         gameEnviroment.setPoolGenerated(false);
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TourResults.fxml"));
        loader.setController(new TourResultsController(gameEnviroment, tourService));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void closePane(ActionEvent event) throws IOException
    {
        cancelTourPane.setVisible(false);
    }
}
