package seng201.team67.gui.market;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.selection.GachaSelectionController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ItemDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.items.Item;
import seng201.team67.services.audio.MusicService;
import seng201.team67.services.management.MarketService;

import java.io.IOException;
import java.util.ArrayList;

import static seng201.team67.models.enums.Rarity.*;

/**
 * Controls the the market view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class TheMarketController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The selected item. */
    private Item selectedItem;
    /** Service used to manage market behaviour. */
    private final MarketService marketService;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    /** The label name. */
    @FXML
    public javafx.scene.control.Label labelName;
    /** FXML reference for the money text control. */
    @FXML public Label moneyText;
    /** FXML reference for the artist card one control. */
    @FXML private VBox artistCardOne;
    /** FXML reference for the artist card two control. */
    @FXML private VBox artistCardTwo;
    /** FXML reference for the artist card three control. */
    @FXML private VBox artistCardThree;
    /** FXML reference for the buy item button control. */
    @FXML private Button buyItemButton;
    /** FXML reference for the insufficient funds warning control. */
    @FXML private Label insufficientFundsWarning;
    /** FXML reference for the bg1 control. */
    @FXML private ImageView bg1;
    /** FXML reference for the bg2 control. */
    @FXML private ImageView bg2;
    /** FXML reference for the bootleg text control. */
    @FXML private Label bootlegText;
    /** FXML reference for the certified text control. */
    @FXML private Label certifiedText;
    /** FXML reference for the godlike text control. */
    @FXML private Label godlikeText;

    /**
     * Creates a new the market controller.
     * @param gameEnvironment the active game environment
     */
    public TheMarketController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        this.marketService = new MarketService(gameEnvironment);
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     * @throws IOException if an input or output error occurs
     */
    @FXML public void initialize() throws IOException
    {
        this.labelName.setText(gameEnvironment.getLabelService().getLabelName());
        this.moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
        insufficientFundsWarning.setVisible(false);
        bootlegText.setText(String.format("$%d", gameEnvironment.getConfig().gachaStandardCost));
        certifiedText.setText(String.format("$%d", gameEnvironment.getConfig().gachaGoldenCost));
        godlikeText.setText(String.format("$%d", gameEnvironment.getConfig().gachaPlatinumCost));

        loadItemPool();

        gameEnvironment.getMusicService().playTheStudioMusic();

        AnimationTimer bgScroll = new AnimationTimer() {
            private boolean initialized = false;

            @Override
            public void handle(long now) {
                double width = bg1.getBoundsInParent().getWidth();

                if (!initialized) {
                    bg1.setLayoutX(0);
                    bg2.setLayoutX(width);
                    initialized = true;
                }

                if (!gameEnvironment.getConfig().movingBackgroundEnabled) {
                    return;
                }

                bg1.setLayoutX(bg1.getLayoutX() - 0.4);
                bg2.setLayoutX(bg2.getLayoutX() - 0.4);

                if (bg1.getLayoutX() + width <= 0) {
                    bg1.setLayoutX(bg1.getLayoutX() + width * 2);
                }

                if (bg2.getLayoutX() + width <= 0) {
                    bg2.setLayoutX(bg2.getLayoutX() + width * 2);
                }
            }
        };
        bgScroll.start();
    }

    private void loadItemPool()
    {
        ArrayList<Item> pool = getItemPool();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            VBox card = cards[i];
            if (i < pool.size()) {
                Item item = pool.get(i);
                card.setDisable(false);
                ItemDetailBoxFiller.populateMarketBox(card, item);
                card.setOnMouseClicked(e -> selectItem(card, item));
            } else {
                clearArtistCard(card);
            }
        }

        selectedItem = null;
        buyItemButton.setVisible(false);
        hideInsufficientFundsWarning();
    }

    private void clearArtistCard(VBox card) {
        card.getChildren().clear();
        card.setDisable(true);
        card.setOnMouseClicked(null);
        ArtistDetailBoxFiller.applyBaseStyle(card);
    }

    private void selectItem(VBox card, Item item)
    {
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };
        for (VBox c : cards) {
            if (!c.isDisable()) {
                ItemDetailBoxFiller.applyBaseStyle(c);
            }
        }
        ItemDetailBoxFiller.applySelectedStyle(card);
        selectedItem = item;
        buyItemButton.setVisible(true);
        hideInsufficientFundsWarning();
    }

    /**
     * Handles the buy item.
     * It updates related state as needed while performing the operation.
     */
    @FXML public void handleBuyItem()
    {
        if (selectedItem == null) {
            return;
        }

        if (gameEnvironment.getLabelService().buyItem(selectedItem))
        {
            gameEnvironment.removeItemFromPurchasePool(selectedItem);
            moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
            loadItemPool();
            return;
        }

        showInsufficientFundsWarning();
    }

    /**
     * Returns the player to the main menu
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void returnToMainMenu(ActionEvent event) throws IOException {
        gameEnvironment.getMusicService().stopAndReset();
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

    /**
     * Buys a standard item gacha
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void buyStandard(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaStandardCost))
        {
            hideInsufficientFundsWarning();
            screenNavigator.navigate(event, "/fxml/market/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, false, gameEnvironment.getConfig().gachaPoolSize, RARE));
            return;
        }

        showInsufficientFundsWarning();
    }

    /**
     * Buys a golden item gacha
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void buyGolden(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaGoldenCost))
        {
            hideInsufficientFundsWarning();
            screenNavigator.navigate(event, "/fxml/market/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, false, gameEnvironment.getConfig().gachaPoolSize, VERY_RARE));
            return;
        }

        showInsufficientFundsWarning();
    }

    /**
     * Buys a platinum item gacha
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void buyPlatinum(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaPlatinumCost))
        {
            hideInsufficientFundsWarning();
            screenNavigator.navigate(event, "/fxml/market/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, false, gameEnvironment.getConfig().gachaPoolSize, ULTRA));
            return;
        }

        showInsufficientFundsWarning();
    }

    private ArrayList<Item> getItemPool()
    {
        return marketService.getItemPurchasePool();
    }

    /**
     * Rerolls the purcahsable items
     */
    @FXML public void rerollItems()
    {
        if (gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().artistRerollCost)) {
            gameEnvironment.setItemPoolGenerated(false);
            moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
            loadItemPool();
            return;
        }

        showInsufficientFundsWarning();
    }

    private void showInsufficientFundsWarning() {
        insufficientFundsWarning.setVisible(true);
    }

    private void hideInsufficientFundsWarning() {
        insufficientFundsWarning.setVisible(false);
    }

}