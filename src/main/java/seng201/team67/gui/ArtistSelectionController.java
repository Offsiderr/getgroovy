package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnviroment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.gui.controllers.instantiable.GachaController;

import java.io.IOException;

public class ArtistSelectionController {

    public final GameEnviroment gameEnviroment;

    @FXML private HBox artistOne;
    @FXML private HBox artistTwo;
    @FXML private HBox artistThree;
    @FXML private StackPane gachaContainer;

    public ArtistSelectionController(GameEnviroment gameEnviroment) {
        this.gameEnviroment = gameEnviroment;
    }

    @FXML
    public void initialize() throws IOException {
        // Load gacha
        FXMLLoader gachaLoader = new FXMLLoader(getClass().getResource("/fxml/Gatcha.fxml"));
        GachaController gachaController = new GachaController();
        gachaLoader.setController(gachaController);
        VBox gacha = gachaLoader.load();
        gachaContainer.getChildren().add(gacha);
    }
}