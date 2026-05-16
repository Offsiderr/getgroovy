package seng201.team67.gui.dev;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;

import javafx.event.ActionEvent;

public class DevArtistsController {

    private final GameEnvironment gameEnvironment;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();


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
            allArtistsContainer.getChildren().add(createArtistDetailBox(artist));
        }
    }

    private VBox createArtistDetailBox(Artist artist)
    {
        VBox card = ArtistDetailBoxFiller.createArtistBox(artist, null);
        ArtistDetailBoxFiller.addActionButton(card, "Add to Lineup", () -> addArtistToLabel(artist));
        return card;
    }

    private void addArtistToLabel(Artist artist)
    {
        if (gameEnvironment.getLabelService().hireArtist(artist, 0))
        {
            populateAllArtists();
        }
    }

    @FXML public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
