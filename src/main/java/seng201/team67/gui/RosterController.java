package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ItemDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.items.Item;

import java.util.ArrayList;
import java.util.List;

public class RosterController {

    private final GameEnvironment gameEnvironment;

    @FXML private HBox lineupPane;
    @FXML private FlowPane allArtistsContainer;
    @FXML private FlowPane allItemsContainer;
    @FXML private Label lineupWarning;

    private final List<AnchorPane> lineupSlots = new ArrayList<>();
    private final List<VBox> lineupCards = new ArrayList<>();
    private final List<VBox> poolCards = new ArrayList<>();
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    public RosterController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize() {
        refreshView();
        lineupWarning.setText("You must have between 1 and " + gameEnvironment.getLabelService().getLineupLimit() + " artist(s) in your lineup.");
        lineupWarning.setVisible(false);
    }

    public void refreshView() {
        buildLineupSlots();
        populateLineup();
        populateAllArtists();
        populateAllItems();
    }

    private void buildLineupSlots() {
        lineupPane.getChildren().clear();
        lineupSlots.clear();
        lineupCards.clear();

        int slotCount = gameEnvironment.getLabelService().getLineupLimit();
        for (int i = 0; i < slotCount; i++) {
            AnchorPane slot = new AnchorPane();
            slot.setMinHeight(0.0);
            slot.setPrefHeight(260.0);
            HBox.setHgrow(slot, javafx.scene.layout.Priority.ALWAYS);
            lineupPane.getChildren().add(slot);
            lineupSlots.add(slot);
            lineupCards.add(null);
        }
    }

    private void populateLineup() {
        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        int maxLineupSize = gameEnvironment.getLabelService().getLineupLimit();

        if (lineup.size() > maxLineupSize) {
            lineup = new ArrayList<>(lineup.subList(0, maxLineupSize));
            gameEnvironment.getLabelService().setLineUp(lineup);
        }

        for (int i = 0; i < lineup.size(); i++) {
            VBox card = createCard(lineup.get(i));
            int slotIndex = i;
            card.setOnMouseClicked(e -> removeFromLineup(card, slotIndex));
            card.setStyle(card.getStyle() + "-fx-cursor: hand;");
            mountCardInSlot(lineupSlots.get(i), card);
            lineupCards.set(i, card);
        }
    }

    private void populateAllArtists() {
        allArtistsContainer.getChildren().clear();
        poolCards.clear();

        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();

        for (Artist artist : gameEnvironment.getLabelService().getAllArtists()) {
            if (lineup.contains(artist)) {
                continue;
            }

            VBox card = createCard(artist);
            card.setOnMouseClicked(e -> addToLineup(card));
            card.setStyle(card.getStyle() + "-fx-cursor: hand;");
            allArtistsContainer.getChildren().add(card);
            poolCards.add(card);
        }
    }

    private void populateAllItems()
    {
        allItemsContainer.getChildren().clear();

        for (Item item : gameEnvironment.getLabelService().getAllItems()) {
            VBox itemCard = createItemCard(item);
            allItemsContainer.getChildren().add(itemCard);
        }
    }

    private void addToLineup(VBox card) {
        int emptySlot = findEmptySlot();
        if (emptySlot == -1) {
            lineupWarning.setVisible(true);
            return;
        }

        mountCardInSlot(lineupSlots.get(emptySlot), card);

        int slotIndex = emptySlot;
        card.setOnMouseClicked(e -> removeFromLineup(card, slotIndex));

        lineupCards.set(emptySlot, card);
        allArtistsContainer.getChildren().remove(card);
        poolCards.remove(card);
        allArtistsContainer.requestLayout();
        lineupWarning.setVisible(false);

        syncLineup();
    }

    private void removeFromLineup(VBox card, int slotIndex) {
        lineupSlots.get(slotIndex).getChildren().clear();
        lineupCards.set(slotIndex, null);

        card.setOnMouseClicked(e -> addToLineup(card));
        allArtistsContainer.getChildren().add(card);
        poolCards.add(card);
        allArtistsContainer.requestLayout();
        lineupWarning.setVisible(false);

        syncLineup();
    }

    private void syncLineup() {
        List<Artist> newLineup = new ArrayList<>();
        for (VBox card : lineupCards) {
            if (card != null) {
                newLineup.add((Artist) card.getUserData());
            }
        }
        gameEnvironment.getLabelService().setLineUp(newLineup);
    }

    private int findEmptySlot() {
        for (int i = 0; i < lineupCards.size(); i++) {
            if (lineupCards.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private int lineupCount() {
        int count = 0;
        for (VBox card : lineupCards) {
            if (card != null) {
                count++;
            }
        }
        return count;
    }

    private VBox createCard(Artist artist) {
        VBox root = ArtistDetailBoxFiller.createArtistBox(artist, itemName -> {
            // Find the item by name from the player's inventory
            Item item = gameEnvironment.getLabelService().getAllItems().stream()
                    .filter(i -> i.getName().equals(itemName))
                    .findFirst()
                    .orElse(null);

            if (item != null) {
                boolean equipped = gameEnvironment.getLabelService().equipItem(artist, item);
                if (equipped) {
                    refreshView(); // rebuild all cards to reflect the change
                }
            }
        });

        ArtistDetailBoxFiller.addFireButton(root, "Fire", () -> {
            boolean retired = gameEnvironment.getLabelService().retireArtist(artist);
            if (retired) {
                lineupWarning.setText("You must have between 1 and " + gameEnvironment.getLabelService().getLineupLimit() + " artist(s) in your lineup.");
                lineupWarning.setVisible(false);
                refreshView();
                return;
            }

            lineupWarning.setText("You can't fire your last remaining artist.");
            lineupWarning.setVisible(true);
        });
        return root;
    }

    private VBox createItemCard(Item item) {
        VBox root = new VBox();
        root.setPrefWidth(315.0);
        root.setMinWidth(315.0);
        root.setPrefHeight(190.0);
        ItemDetailBoxFiller.populateArtistBox(root, item);
        root.setUserData(item);

        //Allow it to be dragged
        root.setOnDragDetected(mouseEvent -> {
            Dragboard db = root.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(item.getName());
            db.setContent(content);

            //set the drag cursor to the item's image
            var stream = getClass().getResourceAsStream(item.getImagePath());
            if (stream == null) {
                stream = getClass().getResourceAsStream("/images/Artists/placeholder.png");
            }
            if (stream != null) {
                //TODO: standardize sizes
                Image dragImage = new Image(stream, 48, 48, true, true);
                db.setDragView(dragImage, 24, 24);
            }

            mouseEvent.consume();
        });

        root.setOnDragDone(dragEvent -> dragEvent.consume());

        return root;
    }

    private void mountCardInSlot(AnchorPane slot, VBox cardRoot) {
        slot.getChildren().setAll(cardRoot);
        AnchorPane.setTopAnchor(cardRoot, 0.0);
        AnchorPane.setRightAnchor(cardRoot, 0.0);
        AnchorPane.setBottomAnchor(cardRoot, 0.0);
        AnchorPane.setLeftAnchor(cardRoot, 0.0);
    }

    @FXML
    private void returnToMainMenu(ActionEvent event) {
        int lineupCount = lineupCount();
        int lineupLimit = gameEnvironment.getLabelService().getLineupLimit();

        if (lineupCount < 1 || lineupCount > lineupLimit) {
            lineupWarning.setVisible(true);
            return;
        }

        screenNavigator.navigate(event, "/fxml/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
