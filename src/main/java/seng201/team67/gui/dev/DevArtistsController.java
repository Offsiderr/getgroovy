package seng201.team67.gui.dev;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.MainMenuController;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.models.Artist;

import javafx.event.ActionEvent;
import java.io.IOException;

public class DevArtistsController {

    private GameEnvironment gameEnvironment;


    @FXML private FlowPane allArtistsContainer;

    public DevArtistsController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize()
    {
        populateAllArtists();
    }

    private void populateAllArtists()
    {
        allArtistsContainer.getChildren().clear();

        for (Artist artist : gameEnvironment.getArtistPool()) {
            allArtistsContainer.getChildren().add(loadCard(artist).getCardRoot());
        }
    }

    private ArtistCardController loadCard(Artist artist)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistCard.fxml"));
            ArtistCardController card = new ArtistCardController(gameEnvironment, null);
            loader.setController(card);
            loader.load();
            card.setArtist(artist);
            return card;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ArtistCard.fxml", e);
        }
    }

    @FXML public void goBack(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        loader.setController(new MainMenuController(gameEnvironment));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
