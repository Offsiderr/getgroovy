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
import seng201.team67.services.setup.ArtistSelectionService;
import seng201.team67.services.setup.GameSetupService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls the artist selection view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ArtistSelectionController {

    /** Shared game state for the current session. */
    public final GameEnvironment gameEnvironment;
    /** Service used to manage artist selection behaviour. */
    private final ArtistSelectionService artistSelectionService;
    /** Service used to manage game setup behaviour. */
    private final GameSetupService gameSetupService;
    private final SoundEffectsService soundEffectsService;

    /** FXML reference for the artist one control. */
    @FXML private VBox artistOne;
    /** FXML reference for the artist two control. */
    @FXML private VBox artistTwo;
    /** FXML reference for the artist three control. */
    @FXML private VBox artistThree;
    /** FXML reference for the artist four control. */
    @FXML private VBox artistFour;
    /** FXML reference for the artist five control. */
    @FXML private VBox artistFive;
    /** The gacha container. */
    @FXML private StackPane gachaContainer;
    /** FXML reference for the select artists control. */
    @FXML private Button selectArtists;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** The view loader. */
    private final ViewLoader viewLoader = new ViewLoader();

    /** Collection that stores the artist cards. */
    protected final List<VBox> artistCards = new ArrayList<>();

    /**
     * Creates a new artist selection controller.
     * @param gameEnvironment the active game environment
     */
    public ArtistSelectionController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
        this.artistSelectionService = new ArtistSelectionService(gameEnvironment);
        this.gameSetupService = new GameSetupService();
        this.soundEffectsService = new SoundEffectsService(gameEnvironment);
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     * @throws IOException if an input or output error occurs
     */
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

    /**
     * Populates the artist cards.
     * @param slots the list of slots
     * @param picked the list of picked
     */
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

    /**
     * We animate the gacha cards in on the artists selection screen.
     */
    private void animateCardsIn()
    {
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

            ParallelTransition popIn = new ParallelTransition(
                    new ParallelTransition(growTransition, fadeTransition),
                    new SequentialTransition(growTransition, settleTransition)
            );

            sequence.getChildren().addAll(
                    new PauseTransition(Duration.millis(150)),
                    new ParallelTransition(
                            fadeTransition,
                            new SequentialTransition(growTransition, settleTransition)
                    )
            );
        }

        sequence.play();
    }

    /**
     * Processes the on selection changed.
     */
    public void onSelectionChanged() {
        long selectedCount = updateArtistSelectionAvailability(getMaxArtistSelections());
        selectArtists.setDisable(selectedCount != gameEnvironment.getConfig().maxStartingArtists);
    }

    /**
     * Returns the selected artists.
     * @return The selected artists.
     */
    public List<Artist> getSelectedArtists() {
        return artistCards.stream()
                .filter(this::isSelected)
                .map(card -> (Artist) card.getUserData())
                .toList();
    }

    /**
     * Updates the artist selection availability. If the artists cards selected are over the select limit,
     * the reset get greyed out.
     * @param selectionLimit the numeric value for the selection limit
     * @return The artist selection availability.
     */
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

    /**
     * Returns the max artist selections.
     * @return The max artist selections.
     */
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

    /**
     * Processes the artists selected.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void artistsSelected(ActionEvent event) throws IOException
    {
        gameSetupService.createLabel(gameEnvironment, getSelectedArtists());
        gameEnvironment.getLabelService().getAllArtists().forEach(artist -> {
            artist.owned = true;
        });

        moveScene(event);
    }

    @FXML
    private void moveScene(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
