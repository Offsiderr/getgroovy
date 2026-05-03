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
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.models.Artist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RosterController {

    private final GameEnvironment gameEnvironment;

    @FXML private AnchorPane artistOne;
    @FXML private AnchorPane artistTwo;
    @FXML private AnchorPane artistThree;
    @FXML private FlowPane allArtistsContainer;
    @FXML private Label lineupWarning;

    private final ArtistCardController[] lineupCards = new ArtistCardController[3];
    private final List<ArtistCardController> poolCards = new ArrayList<>();

    public RosterController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize() {
        populateLineup();
        populateAllArtists();
        lineupWarning.setText("You must have exactly " + gameEnvironment.getConfig().requiredLineupSize + " artist(s) in your lineup.");
        lineupWarning.setVisible(false);
    }

    private void populateLineup() {
        List<AnchorPane> slots = List.of(artistOne, artistTwo, artistThree);
        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();

        for (int i = 0; i < lineup.size() && i < 3; i++) {
            ArtistCardController card = loadCard(lineup.get(i));
            int slotIndex = i;
            card.getCardRoot().setOnMouseClicked(e -> removeFromLineup(card, slotIndex));
            card.getCardRoot().setStyle("-fx-cursor: hand;");
            slots.get(i).getChildren().setAll(card.getCardRoot());
            lineupCards[i] = card;
        }
    }

    private void populateAllArtists() {
        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();

        for (Artist artist : gameEnvironment.getLabelService().getAllArtists()) {
            if (lineup.contains(artist)) continue;

            ArtistCardController card = loadCard(artist);
            card.getCardRoot().setOnMouseClicked(e -> addToLineup(card));
            card.getCardRoot().setStyle("-fx-cursor: hand;");
            allArtistsContainer.getChildren().add(card.getCardRoot());
            poolCards.add(card);
        }
    }

    private void addToLineup(ArtistCardController card) {
        int emptySlot = findEmptySlot();
        if (emptySlot == -1) return;

        List<AnchorPane> slots = List.of(artistOne, artistTwo, artistThree);
        slots.get(emptySlot).getChildren().setAll(card.getCardRoot());

        int slotIndex = emptySlot;
        card.getCardRoot().setOnMouseClicked(e -> removeFromLineup(card, slotIndex));

        lineupCards[emptySlot] = card;
        allArtistsContainer.getChildren().remove(card.getCardRoot());
        poolCards.remove(card);
        allArtistsContainer.requestLayout();

        syncLineup();
    }

    private void removeFromLineup(ArtistCardController card, int slotIndex) {
        List<AnchorPane> slots = List.of(artistOne, artistTwo, artistThree);
        slots.get(slotIndex).getChildren().clear();
        lineupCards[slotIndex] = null;

        card.getCardRoot().setOnMouseClicked(e -> addToLineup(card));
        allArtistsContainer.getChildren().add(card.getCardRoot());
        poolCards.add(card);
        allArtistsContainer.requestLayout();

        syncLineup();
    }

    private void syncLineup() {
        List<Artist> newLineup = new ArrayList<>();
        for (ArtistCardController card : lineupCards) {
            if (card != null) newLineup.add(card.artist);
        }
        gameEnvironment.getLabelService().setLineUp(newLineup);
    }

    private int findEmptySlot() {
        for (int i = 0; i < lineupCards.length; i++) {
            if (lineupCards[i] == null) return i;
        }
        return -1;
    }

    private int lineupCount() {
        int count = 0;
        for (ArtistCardController card : lineupCards) {
            if (card != null) count++;
        }
        return count;
    }

    private ArtistCardController loadCard(Artist artist) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistCard.fxml"));
            ArtistCardController card = new ArtistCardController(gameEnvironment);
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
        if(lineupCount() != gameEnvironment.getConfig().requiredLineupSize)
        {
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
