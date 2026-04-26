package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seng201.team67.GameEnviroment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.models.Artist;
import seng201.team67.services.SoundEffectsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMenuController {

    private GameEnviroment gameEnviroment;
    private SoundEffectsService soundEffectsService = new SoundEffectsService();

    private List<Artist> lineup;

    @FXML private Label labelName;
    @FXML private Label gameDifficulty;
    @FXML private Label moneyText;
    @FXML private Label gameTours;
    @FXML private SplitPane artistPane;
    @FXML private AnchorPane artistOne;
    @FXML private AnchorPane artistTwo;
    @FXML private AnchorPane artistThree;

    private Stage stage;
    private Scene scene;
    private Parent root;

    //not needed currently, but here if needed in the future.
    private final List<ArtistCardController> artistCards = new ArrayList<>();


    public MainMenuController(GameEnviroment gameEnviroment) {
        this.gameEnviroment = gameEnviroment;
    }

    @FXML
    public void initialize() throws IOException
    {
        labelName.setText(gameEnviroment.getLabelService().getLabelName());
        gameDifficulty.setText(gameEnviroment.getDifficulty().name());
        moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));
        gameTours.setText(gameEnviroment.getTourCount() + "/" + gameEnviroment.getSelectedNumTours() + " Tours");

        lineup = gameEnviroment.getLabelService().label.getLine_Up();

        List<AnchorPane> slots = List.of(artistOne, artistTwo, artistThree);

        for (int i = 0; i < slots.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistCard.fxml"));
                ArtistCardController cardController = new ArtistCardController();
                loader.setController(cardController);
                slots.get(i).getChildren().add(loader.load());
                cardController.setArtist(lineup.get(i));
                artistCards.add(cardController);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load ArtistCard.fxml", e);
            }
        }
    }

    //Mainmenu buttons

    @FXML public void startTour(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectTour.fxml"));
        loader.setController(new SelectTourController(gameEnviroment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void startRoster(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistRoster.fxml"));
        loader.setController(new RosterController(gameEnviroment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void startMarket()
    {
        soundEffectsService.playYes();

    }

    @FXML public void startStudio(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TheStudio.fxml"));
        loader.setController(new TheStudioController(gameEnviroment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
