package seng201.team67.gui.util;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * The screen navigator. This class can navigate the player to different fxml screens,
 * and has a number of different method signitures with different parameters so a navigation
 * can be completed anywhere easily.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ScreenNavigator {

    /**
     * Navigates to a new fxml screen.
     * @param event the action event that triggered the request
     * @param fxmlPath the text value for the fxml path
     * @param controller the controller that manages the loaded view
     */
    public void navigate(ActionEvent event, String fxmlPath, Object controller)
    {
        Node sourceNode = (Node) event.getSource();
        Stage sourceStage = (Stage) sourceNode.getScene().getWindow();
        navigate(event, fxmlPath, controller, sourceNode, sourceStage);
    }

    /**
     * Navigates to a new fxml screen.
     * @param node the node
     * @param fxmlPath the text value for the fxml path
     * @param controller the controller that manages the loaded view
     */
    public void navigate(Node node, String fxmlPath, Object controller)
    {
        Stage stage = (Stage) node.getScene().getWindow();
        navigate(null, fxmlPath, controller, node, stage);
    }

    /**
     * Navigates to a new fxml screen.
     * @param stage the stage
     * @param fxmlPath the text value for the fxml path
     * @param controller the controller that manages the loaded view
     */
    public void navigate(Stage stage, String fxmlPath, Object controller)
    {
        navigate(null, fxmlPath, controller, null, stage);
    }

    /**
     * Navigates to a new fxml screen.
     * It updates related state as needed while performing the operation.
     * @param event the action event that triggered the request
     * @param fxmlPath the text value for the fxml path
     * @param controller the controller that manages the loaded view
     * @param node the node
     * @param stage the stage
     */
    public void navigate(ActionEvent event, String fxmlPath, Object controller, Node node, Stage stage)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getResource(fxmlPath));
            loader.setController(controller);
            Parent root = loader.load();

            Stage targetStage = stage;
            if (targetStage == null && node != null) {
                targetStage = (Stage) node.getScene().getWindow();
            }

            if (targetStage == null && event != null && event.getSource() instanceof Node eventNode) {
                targetStage = (Stage) eventNode.getScene().getWindow();
            }

            if (targetStage == null) {
                throw new IllegalStateException("Unable to determine which stage to navigate on.");
            }

            boolean isTitleScreen = targetStage.getScene() != null &&
                    targetStage.getScene().getRoot().getId() != null &&
                    targetStage.getScene().getRoot().getId().equals("titleRoot");

            if (isTitleScreen) {
                final Stage finalStage = targetStage;

                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), finalStage.getScene().getRoot());
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);

                fadeOut.setOnFinished(e -> {
                    finalStage.setScene(new Scene(root));

                    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                });

                fadeOut.play();
            } else {
                targetStage.setScene(new Scene(root));
                targetStage.show();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlPath, e);
        }
    }

    private URL getResource(String fxmlPath) {
        return Objects.requireNonNull(
                getClass().getResource(fxmlPath),
                "FXML resource not found: " + fxmlPath
        );
    }
}