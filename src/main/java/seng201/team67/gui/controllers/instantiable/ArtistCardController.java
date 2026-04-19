package seng201.team67.gui.controllers.instantiable;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng201.team67.gui.ArtistSelectionController;
import seng201.team67.models.Artist;

public class ArtistCardController {

    //The artist cards are instanciatable representations of artists. These can be placed anywhere, and just
    //created by passing in the artist object.

    public Artist artist;
    private boolean selected = false;
    private ArtistSelectionController parentController;

    @FXML private VBox CardRoot;
    @FXML private Label name;
    @FXML private Label description;
    @FXML private Label artistCost;
    @FXML private Label star_power;
    @FXML private Label type;

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
        this.star_power.setText(String.valueOf(artist.getStar_power()));
        this.type.setText(artist.getType());

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

}
