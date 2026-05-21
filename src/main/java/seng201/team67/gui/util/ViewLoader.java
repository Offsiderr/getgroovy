package seng201.team67.gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Represents the view loader used by the game. This class is used for loading an fxml file into an existing fxml screen.
 * the fxml screen can be loaded into a pre-existing pane that is passed in as a parameter
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ViewLoader {


    /**
     * Processes the load.
     * @param fxmlPath the text value for the fxml path
     * @param controller the controller that manages the loaded view
     * @return The parent.
     */
    public Parent load(String fxmlPath, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getResource(fxmlPath));
            loader.setController(controller);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlPath, e);
        }
    }

    /**
     * Loads the fxml view into a entered Pane
     * @param container the container
     * @param fxmlPath the text value for the fxml path
     * @param controller the controller that manages the loaded view
     * @return The parent.
     */
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
