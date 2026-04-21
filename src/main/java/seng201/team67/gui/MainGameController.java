package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnviroment;

public class MainGameController {

    private GameEnviroment gameEnviroment;

    @FXML private Label labelName;
    @FXML private Label moneyText;
    @FXML private Label expeditionCount;
    @FXML private Label payText;

    @FXML private ProgressBar tourProgressBar;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    public MainGameController(GameEnviroment gameEnviroment)
    {
        this.gameEnviroment = gameEnviroment;
    }

    @FXML public void initialize()
    {
        labelName.setText(gameEnviroment.getLabelService().getLabelName());
        moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));

    }

    public void endTourEarly()
    {

    }
}
