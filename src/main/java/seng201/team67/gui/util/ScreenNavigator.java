package seng201.team67.gui.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ScreenNavigator {
    //Refactored to use the screen navigator to switch screens

    public void navigate(ActionEvent event, String fxmlPath, Object controller)
    {
        Node sourceNode = (Node) event.getSource();
        Stage sourceStage = (Stage) sourceNode.getScene().getWindow();
        navigate(event, fxmlPath, controller, sourceNode, sourceStage);
    }

    public void navigate(Node node, String fxmlPath, Object controller)
    {
        Stage stage = (Stage) node.getScene().getWindow();
        navigate(null, fxmlPath, controller, node, stage);
    }

    public void navigate(Stage stage, String fxmlPath, Object controller)
    {
        navigate(null, fxmlPath, controller, null, stage);
    }

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

            targetStage.setScene(new Scene(root));
            targetStage.show();
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
