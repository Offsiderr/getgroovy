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

/**
 * Controls the dev artists view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class DevArtistsController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();


    /** The all artists container. */
    @FXML private FlowPane allArtistsContainer;

    /**
     * Creates a new dev artists controller.
     * @param gameEnvironment the active game environment
     */
    public DevArtistsController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize()
    {
        populateAllArtists();
    }

    /**
     * Populates the artists container with every artist in the game.
     */
    private void populateAllArtists()
    {
        allArtistsContainer.getChildren().clear();

        for (Artist artist : gameEnvironment.getArtistPool()) {
            allArtistsContainer.getChildren().add(createArtistDetailBox(artist));
        }
    }

    /**
     * populateAllArtists uses this method to create all of the ArtistDetailBoxes.
     * @param artist the artist to create a detail box for
     */
    private VBox createArtistDetailBox(Artist artist)
    {
        VBox card = ArtistDetailBoxFiller.createArtistBox(artist, null);
        ArtistDetailBoxFiller.addActionButton(card, "Add to Lineup", () -> addArtistToLabel(artist));
        return card;
    }

    /**
     * If the user would like to add an artist to their label's all artists, then this method is used.
     * @param artist the artist to add.
     */
    private void addArtistToLabel(Artist artist)
    {
        if (gameEnvironment.getLabelService().hireArtist(artist, 0))
        {
            populateAllArtists();
        }
    }

    /**
     * Processes the go back.
     * @param event the action event that triggered the request
     */
    @FXML public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
