package seng201.team67.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.setup.StartController;
import seng201.team67.gui.util.ViewLoader;

public class JavaFxApp extends Application {

    private final ViewLoader viewLoader = new ViewLoader();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        GameEnvironment gameEnvironment = new GameEnvironment();

        primaryStage.setTitle("Get Groovy");
        primaryStage.getIcons().add(new Image("/images/GameIcon.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(viewLoader.load("/fxml/setup/startmenu.fxml", new StartController(gameEnvironment))));
        primaryStage.show();
    }
}
