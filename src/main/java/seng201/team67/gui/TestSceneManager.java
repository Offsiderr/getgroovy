package seng201.team67.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class TestSceneManager {

    public void switchToLabelNaming()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/luetestgui/testlabelenter.fxml"));

            Parent root = loader.load();
        }catch (IOException e)
        {
            throw  new RuntimeException("Could not loadScene");
        }
    }
}
