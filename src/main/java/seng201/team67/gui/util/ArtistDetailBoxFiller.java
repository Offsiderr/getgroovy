package seng201.team67.gui.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.items.Item;

import java.util.List;

public class ArtistDetailBoxFiller {

    private static final String BASE_STYLE =
            "-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;";

    private static final String SELECTED_STYLE =
            "-fx-border-color: #0078d7; -fx-border-width: 3; -fx-background-color: #dce9f7;";

    private static final String ITEM_SLOT_STYLE =
            "-fx-border-color: #aaaaaa; -fx-border-width: 1.5; -fx-background-color: #ececec;";

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
                createLabel("Hire: $" + (int) artist.getCost()),
                createItemSlots(artist)
        );

        container.getChildren().addAll(imageView, textBox);
        card.getChildren().add(container);
        card.setUserData(artist);
    }

    public static VBox createArtistBox(Artist artist) {
        VBox card = new VBox(8);
        card.setPrefWidth(315.0);
        card.setMinWidth(315.0);
        card.setPrefHeight(190.0);
        card.setAlignment(Pos.TOP_CENTER);

        populateArtistBox(card, artist);
        return card;
    }

    public static void addFireButton(VBox card, String buttonText, Runnable action) {
        Button actionButton = new Button(buttonText);
        actionButton.setMaxWidth(Double.MAX_VALUE);
        actionButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold;");
        actionButton.setOnAction(event -> {
            event.consume();
            action.run();
        });

        VBox.setVgrow(actionButton, Priority.NEVER);
        card.getChildren().add(actionButton);
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

    private static HBox createItemSlots(Artist artist) {
        HBox itemSlots = new HBox(8);
        itemSlots.setAlignment(Pos.CENTER);
        itemSlots.setMaxWidth(Double.MAX_VALUE);

        List<Item> items = artist.getItems();
        for (int i = 0; i < 3; i++) {
            Item item = i < items.size() ? items.get(i) : null;
            itemSlots.getChildren().add(createItemSlot(item));
        }

        return itemSlots;
    }

    private static StackPane createItemSlot(Item item) {
        StackPane slot = new StackPane();
        slot.setMinSize(56, 56);
        slot.setPrefSize(56, 56);
        slot.setMaxSize(56, 56);
        slot.setStyle(ITEM_SLOT_STYLE);

        if (item == null) {
            Label placeholder = new Label("Item");
            placeholder.setStyle("-fx-text-fill: #888888; -fx-font-size: 10px;");
            slot.getChildren().add(placeholder);
            return slot;
        }

        ImageView itemImage = createItemImage(item);
        itemImage.setFitWidth(42);
        itemImage.setFitHeight(42);

        slot.getChildren().add(itemImage);
        slot.setUserData(item);
        return slot;
    }

    private static ImageView createArtistImage(Artist artist) {
        var stream = ArtistDetailBoxFiller.class.getResourceAsStream(artist.getImagePath());

        if (stream == null) {
            stream = ArtistDetailBoxFiller.class.getResourceAsStream("/images/Artists/placeholder.png");
        }

        Image image = new Image(stream);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(90);
        imageView.setFitHeight(90);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        return imageView;
    }

    private static ImageView createItemImage(Item item) {
        var stream = ArtistDetailBoxFiller.class.getResourceAsStream(item.getImagePath());

        if (stream == null) {
            stream = ArtistDetailBoxFiller.class.getResourceAsStream("/images/Artists/placeholder.png");
        }

        Image image = new Image(stream);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }
}
