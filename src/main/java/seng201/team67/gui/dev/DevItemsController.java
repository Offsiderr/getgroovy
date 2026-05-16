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

public class DevItemsController {

    private final GameEnvironment gameEnvironment;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    @FXML private FlowPane allItemsContainer;

    public DevItemsController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize()
    {
        populateAllItems();
    }

    private void populateAllItems()
    {
        allItemsContainer.getChildren().clear();

        for (Item item : gameEnvironment.getAllItems()) {
            allItemsContainer.getChildren().add(createItemDetailBox(item));
        }
    }

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

    private void addItemToLabel(Item item)
    {
        if (gameEnvironment.getLabelService().buyItem(item, 0))
        {
            populateAllItems();
        }
    }

    @FXML public void goBack(ActionEvent event)
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }
}
