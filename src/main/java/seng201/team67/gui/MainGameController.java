package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnviroment;
import seng201.team67.gui.controllers.instantiable.tourmaps.WorldTourController;
import seng201.team67.models.Artist;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.TourService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainGameController {

    //TODO: implement different maps

    private GameEnviroment gameEnviroment;
    private TourService tourService;

    @FXML private Label labelName;
    @FXML private Label moneyText;
    @FXML private Label expeditionCount;
    @FXML private Label payText;

    @FXML private ProgressBar tourProgressBar;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private AnchorPane mapAnchorPane;

    public MainGameController(GameEnviroment gameEnviroment, TourService tourService)
    {
        this.gameEnviroment = gameEnviroment;
        this.tourService = tourService;
    }

    @FXML public void initialize() throws IOException {
        labelName.setText(gameEnviroment.getLabelService().getLabelName());
        moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));

        loadLineup();
        loadMap(tourService.getTourType());
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
        FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("/fxml/worldMap.fxml"));
        WorldTourController worldMapController = new WorldTourController();
        mapLoader.setController(worldMapController);
        Parent mapView = mapLoader.load();

        mapAnchorPane.getChildren().add(mapView);
        worldMapController.initialiseStops(type.getStops());
    }

    public void endTourEarly()
    {

    }


}
