package seng201.team67.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng201.team67.GameEnviroment;

import java.io.IOException;

public class TestSceneManager {

    private final Stage stage;

    private final GameEnviroment gameEnviroment;

    public TestSceneManager(Stage stage, GameEnviroment gameEnviroment)
    {
        this.stage = stage;
        this.gameEnviroment = gameEnviroment;
    }

    public void switchToLabelNaming()
    {
        loadScene("/luetestgui/testlabelenter.fxml", TestMainController.class);
    }

    private void loadScene(String path, Class<TestMainController> testMainControllerClass)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            TestMainController controller = loader.getController();
            controller.initController(gameEnviroment);
            stage.setScene(new Scene(root));
        }catch (IOException e)
        {
            throw  new RuntimeException("Could not loadScene");
        }
    }
}
