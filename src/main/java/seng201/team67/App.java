package seng201.team67;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/startmenu.fxml"));


        primaryStage.setTitle("Twisted Fantasy");

        primaryStage.setScene(new Scene(root));

        primaryStage.show();


    }
}
