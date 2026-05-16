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

public class TheMarketController {

    private final GameEnvironment gameEnvironment;
    private Item selectedItem;
    private final MarketService marketService;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    @FXML
    public javafx.scene.control.Label labelName;
    @FXML public Label moneyText;
    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;
    @FXML private Button buyItemButton;
    @FXML private Label insufficientFundsWarning;
    @FXML private ImageView bg1;
    @FXML private ImageView bg2;

    public TheMarketController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        this.marketService = new MarketService(gameEnvironment);
    }

    @FXML public void initialize() throws IOException
    {
        this.labelName.setText(gameEnvironment.getLabelService().getLabelName());
        this.moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
        insufficientFundsWarning.setVisible(false);

        loadItemPool();

        gameEnvironment.getMusicService().playTheStudioMusic();

        AnimationTimer bgScroll = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameEnvironment.getConfig().movingBackgroundEnabled) {
                    bg1.setLayoutX(0);
                    bg2.setLayoutX(785);
                    return;
                }

                bg1.setLayoutX(bg1.getLayoutX() - 0.4);
                bg2.setLayoutX(bg2.getLayoutX() - 0.4);

                if (bg1.getLayoutX() <= -785) {
                    bg1.setLayoutX(785);
                }

                if (bg2.getLayoutX() <= -785) {
                    bg2.setLayoutX(785);
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

    @FXML public void handleBuyItem()
    {
        if (selectedItem == null) {
            return;
        }

        if (gameEnvironment.getLabelService().buyItem(selectedItem))
        {
            gameEnvironment.removeItemFromPurchasePool(selectedItem);
            moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
            loadItemPool();
            return;
        }

        showInsufficientFundsWarning();
    }

    @FXML public void returnToMainMenu(ActionEvent event) throws IOException {
        gameEnvironment.getMusicService().stopAndReset();
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

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

    @FXML public void rerollItems()
    {
        if (gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().artistRerollCost)) {
            gameEnvironment.setItemPoolGenerated(false);
            moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
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
