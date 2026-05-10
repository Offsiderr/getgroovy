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

public class ItemCardController {

    private final GameEnvironment gameEnvironment;
    private final RosterController rosterController;

    public Item item;
    private boolean selected = false;
    private Runnable onSelectionChanged;

    @FXML private VBox CardRoot;
    @FXML private Label name;
    @FXML private Label description;
    @FXML private Label itemCost;
    @FXML private Label rarity;
    @FXML private Label type;
    @FXML private Label uses;
    @FXML private Label effects;
    @FXML private ImageView itemImage;
    @FXML private Button actionButton;

    public ItemCardController(GameEnvironment gameEnvironment, RosterController rosterController)
    {
        this.gameEnvironment = gameEnvironment;
        this.rosterController = rosterController;
    }

    public void setSelectionHandler(Runnable onSelectionChanged)
    {
        this.onSelectionChanged = onSelectionChanged;
        CardRoot.setOnMouseClicked(e -> toggleSelected());
        CardRoot.setStyle("-fx-cursor: hand;");
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setItem(Item item)
    {
        this.item = item;
        this.name.setText(item.getName());
        this.description.setText(item.getDescription());
        this.itemCost.setText(String.valueOf(item.getCost()));
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

    public VBox getCardRoot()
    {
        return CardRoot;
    }

    @FXML public void actionButton()
    {
        if (rosterController != null)
        {
            rosterController.refreshView();
        }
    }
}
