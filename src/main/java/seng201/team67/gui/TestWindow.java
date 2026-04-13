package seng201.team67.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader baseLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/luetestgui/testmainmenu.fxml"));
        baseLoader.setClassLoader(getClass().getClassLoader());
        Parent root = baseLoader.load();

        TestMainController baseController = baseLoader.getController();
        baseController.init(primaryStage);

        primaryStage.setTitle("Twisted Fantasy");
        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchWrapper(String [] args) {
        launch(args);
    }
}
