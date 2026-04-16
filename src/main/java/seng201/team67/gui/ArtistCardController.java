package seng201.team67.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng201.team67.models.Artist;

public class ArtistCardController {

    //The artist cards are instanciatable representations of artists. These can be placed anywhere, and just
    //created by passing in the artist object.

    public Artist artist;

    @FXML private Label name;
    @FXML private Label description;
    @FXML private Label artistCost;
    @FXML private Label artistStamina;
    @FXML private Label star_power;
    @FXML private Label type;

    public void setArtist(Artist artist)
    {
        this.artist = artist;
        this.name.setText(artist.getName());
        this.description.setText(artist.getDescription());
        this.artistCost.setText(String.valueOf(artist.getCost()));
        this.artistStamina.setText(String.valueOf(artist.getStamina()));
        this.star_power.setText(String.valueOf(artist.getStar_power()));
        this.type.setText(artist.getType());

    }
}
