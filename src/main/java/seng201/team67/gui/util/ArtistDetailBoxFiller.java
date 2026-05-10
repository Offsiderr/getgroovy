package seng201.team67.gui.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.items.Item;

import java.util.List;
import java.util.function.Consumer;

public class ArtistDetailBoxFiller {
    private static final double CARD_WIDTH = 315.0;
    private static final double CARD_HEIGHT = 250.0;
    private static final double IMAGE_SIZE = 84.0;
    private static final double ITEM_SLOT_SIZE = 48.0;

    private static final String BASE_STYLE =
            "-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;";

    private static final String SELECTED_STYLE =
            "-fx-border-color: #0078d7; -fx-border-width: 3; -fx-background-color: #dce9f7;";

    private static final String ITEM_SLOT_STYLE =
            "-fx-border-color: #aaaaaa; -fx-border-width: 1.5; -fx-background-color: #ececec;";

    private ArtistDetailBoxFiller() {
    }

    public static void populateArtistBox(VBox card, Artist artist, Consumer<String> onItemDropped) {
        populateArtistBox(card, artist, onItemDropped, null, null, null);
    }

    public static void populateArtistBox(VBox card, Artist artist, Consumer<String> onItemDropped, Consumer<Item> onItemClicked) {
        populateArtistBox(card, artist, onItemDropped, null, null, onItemClicked);
    }

    public static void populateArtistBox(VBox card,
                                         Artist artist,
                                         Consumer<String> onItemDropped,
                                         Consumer<Item> onItemDragged,
                                         Consumer<Item> onItemDragReleasedOutside,
                                         Consumer<Item> onItemClicked) {
        card.getChildren().clear();

        applyBaseStyle(card);
        card.setPadding(new Insets(8));
        card.setSpacing(5);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setFillWidth(true);

        HBox container = new HBox(10);
        container.setAlignment(Pos.TOP_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(container, Priority.ALWAYS);

        ImageView imageView = createArtistImage(artist);

        VBox detailsBox = new VBox(4);
        detailsBox.setAlignment(Pos.TOP_LEFT);
        detailsBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        detailsBox.getChildren().addAll(
                createLabel(artist.getName()),
                createLabel(artist.getType()),
                createLabel("Star Power: " + artist.getStarPower()),
                createLabel("Stamina: " + artist.getStamina()),
                createLabel("Health: " + artist.getHealth()),
                createLabel("Hire: $" + (int) artist.getCost())
        );

        VBox itemSlots = createItemSlots(artist, onItemDropped, onItemDragged, onItemDragReleasedOutside, onItemClicked);

        HBox detailsRow = new HBox(10);
        detailsRow.setAlignment(Pos.TOP_LEFT);
        detailsRow.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(detailsRow, Priority.ALWAYS);
        detailsRow.getChildren().addAll(detailsBox, itemSlots);

        container.getChildren().addAll(imageView, detailsRow);
        card.getChildren().add(container);
        card.setUserData(artist);
    }

    public static VBox createArtistBox(Artist artist, Consumer<String> onItemDropped) {
        return createArtistBox(artist, onItemDropped, null, null, null);
    }

    public static VBox createArtistBox(Artist artist, Consumer<String> onItemDropped, Consumer<Item> onItemClicked) {
        return createArtistBox(artist, onItemDropped, null, null, onItemClicked);
    }

    public static VBox createArtistBox(Artist artist,
                                       Consumer<String> onItemDropped,
                                       Consumer<Item> onItemDragged,
                                       Consumer<Item> onItemDragReleasedOutside,
                                       Consumer<Item> onItemClicked) {
        VBox card = new VBox(8);
        card.setPrefWidth(CARD_WIDTH);
        card.setMinWidth(CARD_WIDTH);
        card.setPrefHeight(CARD_HEIGHT);
        card.setMinHeight(CARD_HEIGHT);
        card.setAlignment(Pos.TOP_CENTER);

        populateArtistBox(card, artist, onItemDropped, onItemDragged, onItemDragReleasedOutside, onItemClicked);
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
        label.setWrapText(true);
        return label;
    }

    private static VBox createItemSlots(Artist artist,
                                        Consumer<String> onItemDropped,
                                        Consumer<Item> onItemDragged,
                                        Consumer<Item> onItemDragReleasedOutside,
                                        Consumer<Item> onItemClicked) {
        VBox itemSlots = new VBox(6);
        itemSlots.setAlignment(Pos.TOP_CENTER);
        itemSlots.setMinWidth(ITEM_SLOT_SIZE);
        itemSlots.setPrefWidth(ITEM_SLOT_SIZE);

        List<Item> items = artist.getItems();
        for (int i = 0; i < 3; i++) {
            Item item = i < items.size() ? items.get(i) : null;
            itemSlots.getChildren().add(createItemSlot(item, onItemDropped, onItemDragged, onItemDragReleasedOutside, onItemClicked));
        }

        return itemSlots;
    }

    private static StackPane createItemSlot(Item item,
                                            Consumer<String> onItemDropped,
                                            Consumer<Item> onItemDragged,
                                            Consumer<Item> onItemDragReleasedOutside,
                                            Consumer<Item> onItemClicked) {
        StackPane slot = new StackPane();
        slot.setMinSize(ITEM_SLOT_SIZE, ITEM_SLOT_SIZE);
        slot.setPrefSize(ITEM_SLOT_SIZE, ITEM_SLOT_SIZE);
        slot.setMaxSize(ITEM_SLOT_SIZE, ITEM_SLOT_SIZE);
        slot.setStyle(ITEM_SLOT_STYLE);

        slot.setOnDragOver(dragEvent ->
        {
            if (dragEvent.getGestureSource() != slot && dragEvent.getDragboard().hasString())
            {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
            dragEvent.consume();
        });

        slot.setOnDragExited(dragEvent ->
        {
            slot.setStyle(ITEM_SLOT_STYLE);
            dragEvent.consume();
        });

        slot.setOnDragDropped(dragEvent ->
        {
            Dragboard db = dragEvent.getDragboard();
            if (db.hasString() && onItemDropped != null)
            {
                onItemDropped.accept(db.getString());
                dragEvent.setDropCompleted(true);
            }
            else
            {
                dragEvent.setDropCompleted(false);
            }
            dragEvent.consume();
        });


        if (item == null) {
            Label placeholder = new Label("Item");
            placeholder.setStyle("-fx-text-fill: #888888; -fx-font-size: 10px;");
            slot.getChildren().add(placeholder);
            return slot;
        }

        ImageView itemImage = createItemImage(item);
        itemImage.setFitWidth(36);
        itemImage.setFitHeight(36);

        slot.getChildren().add(itemImage);
        slot.setUserData(item);
        if (onItemDragged != null || onItemDragReleasedOutside != null)
        {
            slot.setOnDragDetected(mouseEvent -> {
                Dragboard dragboard = slot.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString("equipped:" + item.getName());
                dragboard.setContent(content);
                dragboard.setDragView(itemImage.snapshot(null, null), 18, 18);
                if (onItemDragged != null) {
                    onItemDragged.accept(item);
                }
                mouseEvent.consume();
            });
        }
        if (onItemDragReleasedOutside != null)
        {
            slot.setOnDragDone(dragEvent -> {
                if (!dragEvent.isDropCompleted()) {
                    onItemDragReleasedOutside.accept(item);
                }
                dragEvent.consume();
            });
        }
        if (onItemClicked != null)
        {
            slot.setOnMouseClicked(mouseEvent -> onItemClicked.accept(item));
        }

        String remainingUsesText = ItemDisplayFormatter.getRemainingUsesText(item);
        String tooltipText = item.getName() + "\n" + item.getDescription();
        if (!remainingUsesText.isBlank()) {
            tooltipText += "\n" + remainingUsesText;
        }
        Tooltip.install(slot, new Tooltip(tooltipText));
        return slot;
    }

    private static ImageView createArtistImage(Artist artist) {
        var stream = ArtistDetailBoxFiller.class.getResourceAsStream(artist.getImagePath());

        if (stream == null) {
            stream = ArtistDetailBoxFiller.class.getResourceAsStream("/images/Artists/placeholder.png");
        }

        Image image = new Image(stream);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);
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
