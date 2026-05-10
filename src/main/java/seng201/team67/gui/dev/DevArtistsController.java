package seng201.team67.gui.dev;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.ArtistCardController;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;

import javafx.event.ActionEvent;

public class DevArtistsController {

    private GameEnvironment gameEnvironment;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();


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
        ArtistCardController card = new ArtistCardController(gameEnvironment, null);
        viewLoader.load("/fxml/components/ArtistCard.fxml", card);
        card.setArtist(artist);
        return card;
    }

    @FXML public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
