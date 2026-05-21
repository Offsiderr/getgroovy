package seng201.team67.gui.dev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ItemDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.items.Item;

/**
 * Controls the dev items view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class DevItemsController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    /** The all items container. */
    @FXML private FlowPane allItemsContainer;

    /**
     * Creates a new dev items controller.
     * @param gameEnvironment the active game environment
     */
    public DevItemsController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Initialises the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize()
    {
        populateAllItems();
    }

    /**
     * Fills the all items container with item detail boxes
     */
    private void populateAllItems()
    {
        allItemsContainer.getChildren().clear();

        for (Item item : gameEnvironment.getAllItems()) {
            allItemsContainer.getChildren().add(createItemDetailBox(item));
        }
    }

    /**
     * Takes an item and creates a item detail box in a VBox
     * @param item
     * @return A item detail box (vbox)
     */
    private VBox createItemDetailBox(Item item)
    {
        VBox card = new VBox();
        card.setPrefWidth(315.0);
        card.setMinWidth(315.0);
        card.setPrefHeight(190.0);
        ItemDetailBoxFiller.populateArtistBox(card, item);
        ItemDetailBoxFiller.addActionButton(card, "Add to Inventory", () -> addItemToLabel(item));
        return card;
    }

    /**
     * Takes an item and adds it to the player's label
     * @param item
     */
    private void addItemToLabel(Item item)
    {
        if (gameEnvironment.getLabelService().buyItem(item, 0))
        {
            populateAllItems();
        }
    }

    /**
     * Sends the user back to the main menu
     * @param event the action event that triggered the request
     */
    @FXML public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
