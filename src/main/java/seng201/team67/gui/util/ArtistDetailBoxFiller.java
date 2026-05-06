package seng201.team67.gui.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng201.team67.models.Artist;

public final class ArtistDetailBoxFiller {

    private static final String BASE_STYLE =
            "-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;";

    private static final String SELECTED_STYLE =
            "-fx-border-color: #0078d7; -fx-border-width: 3; -fx-background-color: #dce9f7;";

    private ArtistDetailBoxFiller() {
    }

    public static void populateArtistBox(VBox card, Artist artist) {
        card.getChildren().clear();

        applyBaseStyle(card);
        card.setPadding(new Insets(10));
        card.setSpacing(6);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setFillWidth(true);

        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);

        ImageView imageView = createArtistImage(artist);

        VBox textBox = new VBox(6);
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        textBox.getChildren().addAll(
                createLabel(artist.getName()),
                createLabel(artist.getType()),
                createLabel("Star Power: " + artist.getStarPower()),
                createLabel("Stamina: " + artist.getStamina()),
                createLabel("Health: " + artist.getHealth()),
                createLabel("Hire: $" + (int) artist.getCost())
        );

        container.getChildren().addAll(imageView, textBox);
        card.getChildren().add(container);
    }

    public static void applyBaseStyle(VBox card) {
        card.setStyle(BASE_STYLE);
    }

    public static void applySelectedStyle(VBox card) {
        card.setStyle(SELECTED_STYLE);
    }

    private static Label createLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private static ImageView createArtistImage(Artist artist) {
        var stream = ArtistDetailBoxFiller.class.getResourceAsStream(artist.getImagePath());

        if (stream == null) {
            throw new IllegalArgumentException("Image not found: " + artist.getImagePath());
        }

        Image image = new Image(stream);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(90);
        imageView.setFitHeight(90);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        return imageView;
    }
}