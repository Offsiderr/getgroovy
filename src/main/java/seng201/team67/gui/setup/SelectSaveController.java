package seng201.team67.gui.setup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.services.data.GameSaveService;

public class SelectSaveController {

    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final GameSaveService gameSaveService = new GameSaveService();

    @FXML private AnchorPane anchorPane;
    @FXML private Button startButton1;
    @FXML private Button startButton11;
    @FXML private Button startButton111;

    @FXML
    public void initialize()
    {
        configureSaveButton(startButton1, 1);
        configureSaveButton(startButton11, 2);
        configureSaveButton(startButton111, 3);
    }

    @FXML
    public void onSave1(ActionEvent event)
    {
        openSave(event, 1);
    }

    @FXML
    public void onSave2(ActionEvent event)
    {
        openSave(event, 2);
    }

    @FXML
    public void onSave3(ActionEvent event)
    {
        openSave(event, 3);
    }

    @FXML
    public void onStartGame()
    {
        screenNavigator.navigate(anchorPane, "/fxml/setup/PlayerSelections.fxml", new SetupController(new GameEnvironment()));
    }

    private void openSave(ActionEvent event, int slot)
    {
        if (!gameSaveService.hasSave(slot)) {
            return;
        }

        GameEnvironment gameEnvironment = gameSaveService.loadGame(slot);
        if (gameEnvironment == null) {
            return;
        }
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

    private void configureSaveButton(Button button, int slot)
    {
        if (!gameSaveService.hasSave(slot)) {
            button.setText("Empty Slot");
            return;
        }

        GameEnvironment gameEnvironment = gameSaveService.loadGame(slot);
        if (gameEnvironment == null || gameEnvironment.getLabel() == null) {
            button.setText("Empty Slot");
            return;
        }

        String labelName = "Unknown Label";
        String expeditionText = "Expeditions: 0/0";

        if (gameEnvironment.getLabel().getName() != null) {
            labelName = gameEnvironment.getLabel().getName();
        }
        expeditionText = "Expeditions: " + gameEnvironment.getTourCount() + "/" + gameEnvironment.getSelectedNumTours();

        button.setText(labelName + "\n" + expeditionText);
    }
}
