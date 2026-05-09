package seng201.team67.gui.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.items.Item;

public class ItemDetailBoxFiller {
    private static final String BASE_STYLE =
            "-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;";

    private static final String SELECTED_STYLE =
            "-fx-border-color: #0078d7; -fx-border-width: 3; -fx-background-color: #dce9f7;";

    private ItemDetailBoxFiller() {
    }

    public static void populateArtistBox(VBox card, Item item) {
        card.getChildren().clear();

        applyBaseStyle(card);
        card.setPadding(new Insets(10));
        card.setSpacing(6);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setFillWidth(true);

        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);

        ImageView imageView = createItemImage(item);

        VBox textBox = new VBox(6);
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        textBox.getChildren().addAll(
                createLabel(item.getName()),
                createLabel(item.getDescription()),
                createLabel(item.getRarity().toString()),
                createLabel(item.getType()),
                createLabel("Effect(s): " + item.getEffects())
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

    private static ImageView createItemImage(Item item) {
        var stream = ItemDetailBoxFiller.class.getResourceAsStream(item.getImagePath());

        if (stream == null) {
            stream = ItemDetailBoxFiller.class.getResourceAsStream("/images/Artists/placeholder.png");
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
