package seng201.team67.gui.instantiable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.RosterController;
import seng201.team67.gui.util.ItemDisplayFormatter;
import seng201.team67.models.items.Item;

import java.net.URL;
import java.util.stream.Collectors;

/**
 * Controls the item card view and coordinates its user interactions. Depreciated in favour of the item detail card
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class ItemCardController {

    //This class is no longer in use.

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The roster controller. */
    private final RosterController rosterController;

    /** The item. */
    public Item item;
    /** Whether selected. */
    private boolean selected = false;
    /** The on selection changed. */
    private Runnable onSelectionChanged;

    /** FXML reference for the card root control. */
    @FXML private VBox CardRoot;
    /** FXML reference for the name control. */
    @FXML private Label name;
    /** FXML reference for the description control. */
    @FXML private Label description;
    /** FXML reference for the item cost control. */
    @FXML private Label itemCost;
    /** FXML reference for the rarity control. */
    @FXML private Label rarity;
    /** FXML reference for the type control. */
    @FXML private Label type;
    /** FXML reference for the uses control. */
    @FXML private Label uses;
    /** FXML reference for the effects control. */
    @FXML private Label effects;
    /** FXML reference for the item image control. */
    @FXML private ImageView itemImage;
    /** FXML reference for the action button control. */
    @FXML private Button actionButton;

    /**
     * Creates a new item card controller.
     * @param gameEnvironment the active game environment
     * @param rosterController the roster controller to use
     */
    public ItemCardController(GameEnvironment gameEnvironment, RosterController rosterController)
    {
        this.gameEnvironment = gameEnvironment;
        this.rosterController = rosterController;
    }

    /**
     * Sets the selection handler.
     * @param onSelectionChanged the on selection changed
     */
    public void setSelectionHandler(Runnable onSelectionChanged)
    {
        this.onSelectionChanged = onSelectionChanged;
        CardRoot.setOnMouseClicked(e -> toggleSelected());
        CardRoot.setStyle("-fx-cursor: hand;");
    }

    /**
     * Returns whether the item card is selected.
     * @return True if selected, otherwise false.
     */
    public boolean isSelected()
    {
        return selected;
    }

    /**
     * Sets the item.
     * It normalizes the stored value to keep the state consistent.
     * @param item the item involved in the operation
     */
    public void setItem(Item item)
    {
        this.item = item;
        this.name.setText(item.getName());
        this.description.setText(item.getDescription());
        this.itemCost.setText(String.format("$%.2f", item.getCost()));
        this.rarity.setText(item.getRarity().toString());
        this.type.setText(item.getType());
        this.uses.setText(ItemDisplayFormatter.getRemainingUsesText(item));
        this.uses.setVisible(!this.uses.getText().isBlank());
        this.uses.setManaged(!this.uses.getText().isBlank());
        this.effects.setText(formatEffects(item));
        this.itemImage.setImage(loadItemImage(item));

        if (rosterController == null)
        {
            actionButton.setVisible(false);
            actionButton.setManaged(false);
        }
        else
        {
            actionButton.setVisible(true);
            actionButton.setManaged(true);
        }
    }

    /**
     * Loads the item image.
     * @param item the item involved in the operation
     * @return The image.
     */
    public Image loadItemImage(Item item)
    {
        URL resource = getClass().getResource(item.getImagePath());

        if (resource != null)
        {
            return new Image(resource.toExternalForm());
        }

        return new Image(getClass().getResource("/images/Artists/placeholder.png").toExternalForm());
    }

    private String formatEffects(Item item)
    {
        if (item.getEffects() == null || item.getEffects().isEmpty())
        {
            return "No effects";
        }

        return item.getEffects().stream()
                .map(effect -> effect.getName())
                .collect(Collectors.joining(", "));
    }

    private void toggleSelected()
    {
        selected = !selected;
        updateStyle();
        if (onSelectionChanged != null)
        {
            onSelectionChanged.run();
        }
    }

    /**
     * If the card is selected, then we give it a border and make it slightly transparent
     * to make it obvious to the player they have selected this item
     */
    private void updateStyle()
    {
        if (selected)
        {
            CardRoot.setStyle("-fx-border-color: #00cc66; -fx-border-width: 3; -fx-border-radius: 6; -fx-background-color: #e6fff2; -fx-cursor: hand;");
        }
        else
        {
            CardRoot.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-cursor: hand;");
        }
    }

    /**
     * Sets the selectable.
     * @param selectable whether selectable
     */
    public void setSelectable(boolean selectable)
    {
        if (selectable)
        {
            CardRoot.setOpacity(1.0);
            CardRoot.setOnMouseClicked(e -> toggleSelected());
        }
        else
        {
            CardRoot.setOpacity(0.4);
            CardRoot.setOnMouseClicked(null);
        }
    }

    /**
     * Returns the card root.
     * @return The card root.
     */
    public VBox getCardRoot()
    {
        return CardRoot;
    }

    /**
     * Processes the use button.
     */
    @FXML public void actionButton()
    {
        if (rosterController != null)
        {
            rosterController.refreshView();
        }
    }
}
