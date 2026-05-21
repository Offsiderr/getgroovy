package seng201.team67.gui.selection;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.GachaController;
import seng201.team67.gui.market.TheMarketController;
import seng201.team67.gui.studio.TheStudioController;
import seng201.team67.gui.util.ItemDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.items.Item;
import seng201.team67.services.setup.GachaService;

import java.io.IOException;
import java.util.List;

/**
 * Controls the gacha selection view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class GachaSelectionController extends ArtistSelectionController {

    //This is quite similar to the starting artist selection. A decision was made not to make it modular due to the difficulty
    //in making the split panes adjustable.

    /** Shared game state for the current session. */
    public final GameEnvironment gameEnvironment;
    /** Service used to manage gacha behaviour. */
    private final GachaService gachaService;
    /** Whether artists. */
    private boolean artists; //Yes if artists are being opened, otherwise it is items.

    /** FXML reference for the item one control. */
    @FXML private VBox itemOne;
    /** FXML reference for the item two control. */
    @FXML private VBox itemTwo;
    /** FXML reference for the item three control. */
    @FXML private VBox itemThree;
    /** The gacha container. */
    @FXML private StackPane gachaContainer;
    /** FXML reference for the root control. */
    @FXML private AnchorPane root;
    /** FXML reference for the bg1 control. */
    @FXML private ImageView bg1;
    /** FXML reference for the bg2 control. */
    @FXML private ImageView bg2;
    /** FXML reference for the bg3 control. */
    @FXML private ImageView bg3;
    /** FXML reference for the select artists control. */
    @FXML private Button selectArtists;

    /** Numeric value for the hbox size. */
    private int hboxSize;

    /** The rarity. */
    private Rarity rarity;

    //this needs to be implemented in the super class later... hard coded for now.
    /** Numeric value for the selection size. */
    private int selectionSize;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** The view loader. */
    private final ViewLoader viewLoader = new ViewLoader();

    /**
     * Creates a new gacha selection controller.
     * It initializes the state needed for the surrounding game flow.
     * @param gameEnvironment the active game environment
     * @param artists whether artists
     * @param hboxSize the numeric value for the hbox size
     * @param rarity the rarity
     */
    public GachaSelectionController(GameEnvironment gameEnvironment, Boolean artists, int hboxSize, Rarity rarity) {
        super(gameEnvironment);
        this.gameEnvironment = gameEnvironment;
        this.artists = artists;
        this.hboxSize = hboxSize;
        this.rarity = rarity;
        gachaService = new GachaService(gameEnvironment);
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void initialize() throws IOException {
        selectArtists.setDisable(true);
        if (artists)
        {
            selectArtists.setText("Select Arist");
        }
        else
        {
            selectArtists.setText("Select Item");
        }

        String source = artists ? "studio" : "market";
        GachaController gachaController = new GachaController(gameEnvironment, source);
        gachaController.setOnGachaComplete(() -> {if(artists){showArtistCards();}else{showItemCards();}});
        viewLoader.loadInto(gachaContainer, "/fxml/components/Gatcha.fxml", gachaController);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameEnvironment.getConfig().movingBackgroundEnabled) {
                    bg1.setLayoutX(0);
                    bg2.setLayoutX(bg1.getFitWidth());
                    return;
                }

                bg1.setLayoutX(bg1.getLayoutX() - 0.5);
                bg2.setLayoutX(bg2.getLayoutX() - 0.5);

                if (bg1.getLayoutX() + bg1.getFitWidth() <= 0) {
                    bg1.setLayoutX(bg2.getLayoutX() + bg2.getFitWidth());
                }

                if (bg2.getLayoutX() + bg2.getFitWidth() <= 0) {
                    bg2.setLayoutX(bg1.getLayoutX() + bg1.getFitWidth());
                }
            }
        };
        timer.start();
    }

    private void showArtistCards() {
        gachaContainer.setVisible(false);

        List<VBox> slots = List.of(itemOne, itemTwo, itemThree);

        List<Artist> picked = gachaService.getPickedArtists(slots.size(), rarity);
        populateArtistCards(slots, picked);
    }

    private void showItemCards() {
        gachaContainer.setVisible(false);

        List<VBox> slots = List.of(itemOne, itemTwo, itemThree);
        List<Item> picked = gachaService.getPickedItems(slots.size(), rarity);

        for (int i = 0; i < slots.size(); i++) {
            VBox slot = slots.get(i);
            slot.getChildren().clear();
            if (i >= picked.size()) {
                clearItemSlot(slot);
                continue;
            }

            Item item = picked.get(i);
            ItemDetailBoxFiller.populateArtistBox(slot, item);
            slot.setUserData(item);
            slot.setOpacity(1.0);
            slot.setDisable(false);
            setItemSelected(slot, false);
            slot.setOnMouseClicked(e -> toggleItemSelection(slot));
        }
    }

    /**
     * Processes the on selection changed.
     * It updates related state as needed while performing the operation.
     */
    public void onSelectionChanged() {
        if (artists) {
            long selectedCount = updateArtistSelectionAvailability(getMaxArtistSelections());
            selectArtists.setDisable(selectedCount != getMaxArtistSelections());
        } else {
            long selectedCount = updateItemSelectionAvailability();
            selectArtists.setDisable(selectedCount != 1);
        }
    }

    /**
     * Returns the max artist selections.
     * @return The max artist selections.
     */
    @Override
    protected int getMaxArtistSelections() {
        return gameEnvironment.getConfig().maxGachaPicks;
    }

    /**
     * Returns the selected items.
     * It derives the value from the current state before returning it.
     * @return The selected items.
     */
    public List<Item> getSelectedItems() {
        List<VBox> itemSlots = List.of(itemOne, itemTwo, itemThree);

        return itemSlots.stream()
                .filter(slot -> !slot.isDisable())
                .filter(this::isItemSelected)
                .map(slot -> (Item) slot.getUserData())
                .toList();
    }

    private void toggleItemSelection(VBox slot) {
        setItemSelected(slot, !isItemSelected(slot));
        onSelectionChanged();
    }

    private long updateItemSelectionAvailability() {
        List<VBox> itemSlots = List.of(itemOne, itemTwo, itemThree);
        long selectedCount = itemSlots.stream()
                .filter(slot -> !slot.isDisable())
                .filter(this::isItemSelected)
                .count();

        itemSlots.stream()
                .filter(slot -> !slot.isDisable())
                .forEach(slot -> {
                    boolean selected = isItemSelected(slot);
                    updateItemCardStyle(slot, selected);
                    if (selected) {
                        slot.setOpacity(1.0);
                        slot.setOnMouseClicked(e -> toggleItemSelection(slot));
                        return;
                    }

                    boolean selectable = selectedCount < 1;
                    slot.setOpacity(selectable ? 1.0 : 0.4);
                    slot.setOnMouseClicked(selectable ? e -> toggleItemSelection(slot) : null);
                });

        return selectedCount;
    }

    private void clearItemSlot(VBox slot) {
        slot.getChildren().clear();
        slot.setUserData(null);
        slot.setDisable(true);
        slot.setOnMouseClicked(null);
        slot.setOpacity(0.4);
        ItemDetailBoxFiller.applyBaseStyle(slot);
    }

    private boolean isItemSelected(VBox slot) {
        return Boolean.TRUE.equals(slot.getProperties().get("selected"));
    }

    private void setItemSelected(VBox slot, boolean selected) {
        slot.getProperties().put("selected", selected);
        updateItemCardStyle(slot, selected);
    }

    private void updateItemCardStyle(VBox slot, boolean selected) {
        if (selected) {
            ItemDetailBoxFiller.applySelectedStyle(slot);
        } else {
            ItemDetailBoxFiller.applyBaseStyle(slot);
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
        if (artists) {
            gameEnvironment.getLabelService().hireArtist(getSelectedArtists().get(0), 0);
        } else {
            gameEnvironment.getLabelService().buyItem(getSelectedItems().get(0), 0);
        }
        moveScene(event);
    }

    @FXML
    private void moveScene(ActionEvent event) throws IOException
    {
        if (artists) {
            screenNavigator.navigate(event, "/fxml/studio/TheStudio.fxml", new TheStudioController(gameEnvironment));
        } else {
            screenNavigator.navigate(event, "/fxml/market/TheMarket.fxml", new TheMarketController(gameEnvironment));
        }
    }
}
