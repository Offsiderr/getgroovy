package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.models.Artist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RosterController {

    private final GameEnvironment gameEnvironment;

    @FXML private HBox lineupPane;
    @FXML private FlowPane allArtistsContainer;
    @FXML private Label lineupWarning;

    private final List<AnchorPane> lineupSlots = new ArrayList<>();
    private final List<ArtistCardController> lineupCards = new ArrayList<>();
    private final List<ArtistCardController> poolCards = new ArrayList<>();

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
    }

    private void buildLineupSlots() {
        lineupPane.getChildren().clear();
        lineupSlots.clear();
        lineupCards.clear();

        int slotCount = gameEnvironment.getLabelService().getLineupLimit();
        for (int i = 0; i < slotCount; i++) {
            AnchorPane slot = new AnchorPane();
            slot.setMinHeight(0.0);
            slot.setPrefHeight(200.0);
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
            ArtistCardController card = loadCard(lineup.get(i));
            int slotIndex = i;
            card.getCardRoot().setOnMouseClicked(e -> removeFromLineup(card, slotIndex));
            card.getCardRoot().setStyle("-fx-cursor: hand;");
            lineupSlots.get(i).getChildren().setAll(card.getCardRoot());
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

            ArtistCardController card = loadCard(artist);
            card.getCardRoot().setOnMouseClicked(e -> addToLineup(card));
            card.getCardRoot().setStyle("-fx-cursor: hand;");
            allArtistsContainer.getChildren().add(card.getCardRoot());
            poolCards.add(card);
        }
    }

    private void addToLineup(ArtistCardController card) {
        int emptySlot = findEmptySlot();
        if (emptySlot == -1) {
            lineupWarning.setVisible(true);
            return;
        }

        lineupSlots.get(emptySlot).getChildren().setAll(card.getCardRoot());

        int slotIndex = emptySlot;
        card.getCardRoot().setOnMouseClicked(e -> removeFromLineup(card, slotIndex));

        lineupCards.set(emptySlot, card);
        allArtistsContainer.getChildren().remove(card.getCardRoot());
        poolCards.remove(card);
        allArtistsContainer.requestLayout();
        lineupWarning.setVisible(false);

        syncLineup();
    }

    private void removeFromLineup(ArtistCardController card, int slotIndex) {
        lineupSlots.get(slotIndex).getChildren().clear();
        lineupCards.set(slotIndex, null);

        card.getCardRoot().setOnMouseClicked(e -> addToLineup(card));
        allArtistsContainer.getChildren().add(card.getCardRoot());
        poolCards.add(card);
        allArtistsContainer.requestLayout();
        lineupWarning.setVisible(false);

        syncLineup();
    }

    private void syncLineup() {
        List<Artist> newLineup = new ArrayList<>();
        for (ArtistCardController card : lineupCards) {
            if (card != null) {
                newLineup.add(card.artist);
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
        for (ArtistCardController card : lineupCards) {
            if (card != null) {
                count++;
            }
        }
        return count;
    }

    private ArtistCardController loadCard(Artist artist) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistCard.fxml"));
            ArtistCardController card = new ArtistCardController(gameEnvironment, this);
            loader.setController(card);
            loader.load();
            card.setArtist(artist);
            return card;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ArtistCard.fxml", e);
        }
    }

    @FXML
    private void returnToMainMenu(ActionEvent event) throws IOException {
        int lineupCount = lineupCount();
        int lineupLimit = gameEnvironment.getLabelService().getLineupLimit();

        if (lineupCount < 1 || lineupCount > lineupLimit) {
            lineupWarning.setVisible(true);
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        loader.setController(new MainMenuController(gameEnvironment));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
