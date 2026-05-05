package seng201.team67;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng201.team67.gui.StartController;

import static javafx.application.Application.launch;

/**
 * Default entry point class
 * @author seng201 teaching team
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //The app loads the start menu screen, and then all logic sourrounding the
    //scenes is then passed over to the controllers, which are tied to each scene specifically

    //In our structure, the loader sets the controller, as this is how we pass the gameEnviroment through scenes.

    @Override
    public void start(Stage primaryStage) throws Exception {

        GameEnvironment gameEnvironment = new GameEnvironment();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/startmenu.fxml"));
        loader.setController(new StartController(gameEnvironment));

        primaryStage.setTitle("Twisted Fantasy");
        primaryStage.getIcons().add(new Image("/images/GameIcon.png"));

        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();

    }
}
