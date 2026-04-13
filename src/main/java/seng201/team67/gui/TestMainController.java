package seng201.team67.gui;

import javafx.fxml.FXML;
import seng201.team67.GameEnviroment;

public class TestMainController {

    private GameEnviroment gameEnviroment;

    @FXML
    public void initController(GameEnviroment gameEnviroment)
    {
        this.gameEnviroment = gameEnviroment;
    }

    @FXML
    private void onStartGame()
    {
        System.out.println("Starting the game");
        gameEnviroment.getSceneManager().switchToLabelNaming();
    }
}
