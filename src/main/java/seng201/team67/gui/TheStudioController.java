package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng201.team67.GameEnviroment;
import seng201.team67.models.Artist;

import java.io.IOException;
import java.util.ArrayList;

import static seng201.team67.models.enums.Rarity.*;

public class TheStudioController {

    private GameEnviroment gameEnviroment;

    @FXML public javafx.scene.control.Label labelName;
    @FXML public Label moneyText;

    public TheStudioController(GameEnviroment gameEnviroment)
    {
        this.gameEnviroment = gameEnviroment;
    }

    @FXML public void initialize() throws IOException
    {
        this.labelName.setText(gameEnviroment.getLabelService().getLabelName());
        this.moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));


    }

    @FXML public void returnToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        loader.setController(new MainMenuController(gameEnviroment));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML public void buyStandard(ActionEvent event) throws IOException
    {
        if(gameEnviroment.getLabelService().buyItem(100))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GatchaSelection.fxml"));
            loader.setController(new GatchaSelectionController(gameEnviroment, true, 3, RARE));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML public void buyGolden(ActionEvent event) throws IOException
    {
        if(gameEnviroment.getLabelService().buyItem(500))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GatchaSelection.fxml"));
            loader.setController(new GatchaSelectionController(gameEnviroment, true, 3, VERY_RARE));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML public void buyPlatinum(ActionEvent event) throws IOException
    {
        if(gameEnviroment.getLabelService().buyItem(1000))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GatchaSelection.fxml"));
            loader.setController(new GatchaSelectionController(gameEnviroment, true, 3, ULTRA));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    private ArrayList<Artist> getArtistPool()
    {
        return gameEnviroment.resetArtistPurchasePool();
    }

}
