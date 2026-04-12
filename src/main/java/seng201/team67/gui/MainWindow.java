package seng201.team67.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class starts the javaFX application window
 * @author seng201 teaching team
 */
public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader baseLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/select_name.fxml"));
        baseLoader.setClassLoader(getClass().getClassLoader());
        Parent root = baseLoader.load();

        MainController baseController = baseLoader.getController();
        baseController.init(primaryStage);

        primaryStage.setTitle("Twisted Fantasy");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchWrapper(String [] args) {
        launch(args);
    }


}
