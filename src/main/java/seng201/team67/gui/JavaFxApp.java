package seng201.team67.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.setup.StartController;
import seng201.team67.gui.util.ViewLoader;

/**
 * Launches the JavaFX application for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class JavaFxApp extends Application {

    /** The view loader. */
    private final ViewLoader viewLoader = new ViewLoader();

    /**
     * Processes the start.
     * @param primaryStage the primary stage
     * @throws Exception if exception occurs
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {

        primaryStage.setTitle("Get Groovy");
        primaryStage.getIcons().add(new Image("/images/GameIcon.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(viewLoader.load("/fxml/setup/startmenu.fxml", new StartController())));
        primaryStage.show();
    }
}
