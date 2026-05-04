package seng201.team67.gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ViewLoader {

    //Refactored for classes to use the view loader to load fxml into existing view containers
    //gacha, artist selection etc

    public Parent load(String fxmlPath, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getResource(fxmlPath));
            loader.setController(controller);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlPath, e);
        }
    }

    public Parent loadInto(Pane container, String fxmlPath, Object controller) {
        Parent view = load(fxmlPath, controller);
        container.getChildren().setAll(view);
        return view;
    }

    private URL getResource(String fxmlPath) {
        return Objects.requireNonNull(
                getClass().getResource(fxmlPath),
                "FXML resource not found: " + fxmlPath
        );
    }
}
