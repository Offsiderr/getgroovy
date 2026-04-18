package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seng201.team67.GameEnviroment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.models.Artist;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectTourController {

    private GameEnviroment gameEnviroment;

    private List<Artist> lineup;

    //not needed currently, but here if needed in the future.
    private final List<ArtistCardController> artistCards = new ArrayList<>();


    //FXML stuff
    @FXML private SplitPane artistPane;
    @FXML private AnchorPane artistOne;
    @FXML private AnchorPane artistTwo;
    @FXML private AnchorPane artistThree;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public SelectTourController(GameEnviroment gameEnviroment) {
        this.gameEnviroment = gameEnviroment;
    }

    @FXML
    public void initialize() throws IOException
    {
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

    //All the buttons

    @FXML
    public void startLocalTour()
    {

    }

    @FXML
    public void startCountryTour()
    {

    }

    @FXML
    public void startWorldTour()
    {

    }

    @FXML public void cancelTour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        loader.setController(new MainMenuController(gameEnviroment));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

