package seng201.team67.gui.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import seng201.team67.models.items.Item;

public class ItemDetailBoxFiller {
    private static final String BASE_STYLE =
            "-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;";

    private static final String SELECTED_STYLE =
            "-fx-border-color: #0078d7; -fx-border-width: 3; -fx-background-color: #dce9f7;";

    private ItemDetailBoxFiller() {
    }

    public static void populateArtistBox(VBox card, Item item) {
        populateItemBox(card, item, false);
    }

    public static void populateMarketBox(VBox card, Item item) {
        populateItemBox(card, item, true);
    }

    private static void populateItemBox(VBox card, Item item, boolean showPrice) {
        card.getChildren().clear();

        applyBaseStyle(card);
        card.setPadding(new Insets(10));
        card.setSpacing(6);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setFillWidth(true);

        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);

        //container.setMouseTransparent(true);

        ImageView imageView = createItemImage(item);

        VBox textBox = new VBox(6);
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        textBox.getChildren().addAll(
                createLabel(item.getName()),
                createLabel(item.getDescription()),
                createOptionalLabel(showPrice ? "Price: $" + item.getCost() : null),
                createLabel(item.getRarity().toString()),
                createLabel(item.getType()),
                createOptionalLabel(ItemDisplayFormatter.getRemainingUsesText(item)),
                createLabel("Effect(s): " + item.getEffects())
        );

        container.getChildren().addAll(imageView, textBox);
        container.setMouseTransparent(true);
        card.getChildren().add(container);

    }

    public static void addActionButton(VBox card, String buttonText, Runnable action) {
        card.getChildren().removeIf(node -> node instanceof Button button && Boolean.TRUE.equals(button.getUserData()));

        Button actionButton = new Button(buttonText);
        actionButton.setUserData(true);
        actionButton.setMaxWidth(Double.MAX_VALUE);
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

    private static Label createOptionalLabel(String text) {
        Label label = createLabel(text);
        boolean hasText = text != null && !text.isBlank();
        label.setVisible(hasText);
        label.setManaged(hasText);
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
