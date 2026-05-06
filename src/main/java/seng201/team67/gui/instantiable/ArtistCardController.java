package seng201.team67.gui.instantiable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.ArtistSelectionController;
import seng201.team67.gui.RosterController;
import seng201.team67.models.artists.Artist;

import java.io.IOException;
import java.net.URL;

public class ArtistCardController {

    //The artist cards are instanciatable representations of artists. These can be placed anywhere, and just
    //created by passing in the artist object.

    private GameEnvironment gameEnvironment;
    private RosterController rosterController;

    public Artist artist;
    private boolean selected = false;
    private ArtistSelectionController parentController;

    @FXML private VBox CardRoot;
    @FXML private Label name;
    @FXML private Label description;
    @FXML private Label artistCost;
    @FXML private Label star_power;
    @FXML private Label type;
    @FXML private ImageView artistImage;
    @FXML private Button retireButton;

    public ArtistCardController(GameEnvironment gameEnvironment, RosterController rosterController)
    {
        this.gameEnvironment = gameEnvironment;
        this.rosterController = rosterController;
    }

    //In the artist seleciton controller, we need to notify the parent controller when an artist is selected.
    public void setSelectionController(ArtistSelectionController parentController)
    {
        this.parentController = parentController;
        CardRoot.setOnMouseClicked(e -> toggleSelected());
        CardRoot.setStyle("-fx-cursor: hand;");
    }


    public boolean isSelected()
    {
        return selected;
    }



    public void setArtist(Artist artist)
    {
        this.artist = artist;
        this.name.setText(artist.getName());
        this.description.setText(artist.getDescription());
        this.artistCost.setText(String.valueOf(artist.getCost()));
        this.star_power.setText(String.valueOf(artist.getStarPower()));
        this.type.setText(artist.getType());

        this.artistImage.setImage(loadArtistImage(artist));

        if(rosterController == null)
        {
            retireButton.setVisible(false);
        }
        else
        {
            retireButton.setVisible(true);
        }

    }

    public Image loadArtistImage(Artist artist)
    {
        URL resource = getClass().getResource(artist.getImagePath());

        if (resource != null)
        {
            return new javafx.scene.image.Image(resource.toExternalForm());
        }
        else
        {
            return new javafx.scene.image.Image(getClass().getResource("/images/Artists/placeholder.png").toExternalForm());
        }
    }

    private void toggleSelected() {
        selected = !selected;
        updateStyle();
        if (parentController != null) {
            parentController.onSelectionChanged();
        }
    }

    private void updateStyle() {
        if (selected) {
            CardRoot.setStyle("-fx-border-color: #00cc66; -fx-border-width: 3; -fx-border-radius: 6; -fx-background-color: #e6fff2; -fx-cursor: hand;");
        } else {
            CardRoot.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-cursor: hand;");
        }
    }

    public void setSelectable(boolean selectable) {
        if (selectable) {
            CardRoot.setOpacity(1.0);
            CardRoot.setOnMouseClicked(e -> toggleSelected());
        } else {
            CardRoot.setOpacity(0.4);
            CardRoot.setOnMouseClicked(null);
        }
    }

    public VBox getCardRoot() {
        return CardRoot;
    }

    @FXML public void retireButton() throws IOException
    {
        gameEnvironment.getLabelService().retireArtist(artist);
        rosterController.refreshView();

    }
}
