package seng201.team67;

import javafx.stage.Stage;
import seng201.team67.gui.TestSceneManager;
import seng201.team67.gui.TestWindow;

import static javafx.application.Application.launch;

/**
 * Default entry point class
 * @author seng201 teaching team
 */
public class App {
    /**
     * Entry point which runs the javaFX application
     * Due to how JavaFX works we must call MainWindow.launchWrapper() from here,
     * trying to run MainWindow itself will cause an error
     * @param args program arguments from command line
     */
    public static void main(String[] args) {

        //Using Louies GUI temporarily
        //MainWindow.launchWrapper(args)
        launch(args)
    }

    public void start(Stage stage)
    {
        GameEnviroment gameEnviroment = new GameEnviroment();
        TestSceneManager sceneManager = new TestSceneManager(stage, gameEnviroment);
    }
}
