package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnviroment;

import java.io.IOException;

public class ArtistSelectionController {

    public final GameEnviroment gameEnviroment;

    //Places to instantiate the artist cards.
    @FXML private HBox artistOne;
    @FXML private HBox artistTwo;
    @FXML private HBox artistThree;

    public ArtistSelectionController(GameEnviroment gameEnviroment) throws IOException {
        this.gameEnviroment = gameEnviroment;
    }


    @FXML
    public void initialize() throws IOException
    {
        //Load artist cards
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistCardd.fxml"));
        ArtistCardController controller = new ArtistCardController();
        loader.setController(controller);
        VBox card1 = loader.load();
        controller.setArtist(gameEnviroment.getArtistPool().getFirst());
        artistOne.getChildren().add(card1);
    }

}
