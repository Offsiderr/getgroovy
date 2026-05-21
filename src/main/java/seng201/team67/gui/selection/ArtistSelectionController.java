package seng201.team67.gui.selection;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.GachaController;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;
import seng201.team67.services.audio.SoundEffectsService;
import seng201.team67.services.setup.ArtistSelectionService;
import seng201.team67.services.setup.GameSetupService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArtistSelectionController {

    public final GameEnvironment gameEnvironment;
    private final ArtistSelectionService artistSelectionService;
    private final GameSetupService gameSetupService;
    private final SoundEffectsService soundEffectsService;

    @FXML private VBox artistOne;
    @FXML private VBox artistTwo;
    @FXML private VBox artistThree;
    @FXML private VBox artistFour;
    @FXML private VBox artistFive;
    @FXML private StackPane gachaContainer;
    @FXML private Button selectArtists;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    protected final List<VBox> artistCards = new ArrayList<>();

    public ArtistSelectionController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
        this.artistSelectionService = new ArtistSelectionService(gameEnvironment);
        this.gameSetupService = new GameSetupService();
        this.soundEffectsService = new SoundEffectsService(gameEnvironment);
    }

    @FXML
    public void initialize() throws IOException {
        selectArtists.setDisable(true);

        GachaController gachaController = new GachaController(gameEnvironment);
        gachaController.setOnGachaComplete(() -> showArtistCards());
        viewLoader.loadInto(gachaContainer, "/fxml/components/Gatcha.fxml", gachaController);
    }

    private void showArtistCards() {
        gachaContainer.setVisible(false);

        List<Artist> picked = artistSelectionService.pickArtists();
        List<VBox> slots = List.of(artistOne, artistTwo, artistThree, artistFour, artistFive);
        populateArtistCards(slots, picked);
    }

    protected void populateArtistCards(List<VBox> slots, List<Artist> picked) {
        artistCards.clear();

        for (int i = 0; i < slots.size(); i++) {
            VBox slot = slots.get(i);
            slot.getChildren().clear();
            Artist artist = picked.get(i);
            ArtistDetailBoxFiller.populateArtistBox(slot, artist, null);
            slot.setUserData(artist);
            slot.setOpacity(1.0);
            slot.setDisable(false);
            setSelected(slot, false);
            slot.setOnMouseClicked(e -> toggleSelected(slot));
            artistCards.add(slot);

            slot.setOpacity(0);
            slot.setScaleX(0);
            slot.setScaleY(0);
        }

        animateCardsIn();
    }

    private void animateCardsIn() {
        SequentialTransition sequence = new SequentialTransition();

        for (VBox card : artistCards) {
            ScaleTransition growTransition = new ScaleTransition(Duration.millis(200), card);
            growTransition.setToX(1.15);
            growTransition.setToY(1.15);
            growTransition.setInterpolator(Interpolator.EASE_OUT);

            ScaleTransition settleTransition = new ScaleTransition(Duration.millis(100), card);
            settleTransition.setToX(1.0);
            settleTransition.setToY(1.0);
            settleTransition.setInterpolator(Interpolator.EASE_IN);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), card);
            fadeTransition.setToValue(1.0);

            PauseTransition pause = new PauseTransition(Duration.millis(150));
            pause.setOnFinished(e -> soundEffectsService.playCard());

            sequence.getChildren().addAll(
                    pause,
                    new ParallelTransition(
                            fadeTransition,
                            new SequentialTransition(growTransition, settleTransition)
                    )
            );
        }

        sequence.play();
    }

    public void onSelectionChanged() {
        long selectedCount = updateArtistSelectionAvailability(getMaxArtistSelections());
        selectArtists.setDisable(selectedCount != gameEnvironment.getConfig().maxStartingArtists);
    }

    public List<Artist> getSelectedArtists() {
        return artistCards.stream()
                .filter(this::isSelected)
                .map(card -> (Artist) card.getUserData())
                .toList();
    }

    protected long updateArtistSelectionAvailability(int selectionLimit) {
        long selectedCount = artistCards.stream()
                .filter(this::isSelected)
                .count();

        artistCards.forEach(card -> {
            boolean selected = isSelected(card);
            updateCardStyle(card, selected);
            if (!selected) {
                boolean selectable = selectedCount < selectionLimit;
                card.setOpacity(selectable ? 1.0 : 0.4);
                card.setOnMouseClicked(selectable ? e -> toggleSelected(card) : null);
            } else {
                card.setOpacity(1.0);
                card.setOnMouseClicked(e -> toggleSelected(card));
            }
        });

        return selectedCount;
    }

    protected int getMaxArtistSelections() {
        return gameEnvironment.getConfig().maxStartingArtists;
    }

    private void toggleSelected(VBox card) {
        setSelected(card, !isSelected(card));
        onSelectionChanged();
    }

    private boolean isSelected(VBox card) {
        return Boolean.TRUE.equals(card.getProperties().get("selected"));
    }

    private void setSelected(VBox card, boolean selected) {
        card.getProperties().put("selected", selected);
        updateCardStyle(card, selected);
    }

    private void updateCardStyle(VBox card, boolean selected) {
        if (selected) {
            ArtistDetailBoxFiller.applySelectedStyle(card);
        } else {
            ArtistDetailBoxFiller.applyBaseStyle(card);
        }
    }

    @FXML
    public void artistsSelected(ActionEvent event) throws IOException {
        gameSetupService.createLabel(gameEnvironment, getSelectedArtists());
        gameEnvironment.getLabelService().getAllArtists().forEach(artist -> {
            artist.owned = true;
        });
        moveScene(event);
    }

    @FXML
    private void moveScene(ActionEvent event) throws IOException {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}