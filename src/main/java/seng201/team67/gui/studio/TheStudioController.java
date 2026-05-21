package seng201.team67.gui.studio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.selection.GachaSelectionController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;
import seng201.team67.services.audio.MusicService;
import seng201.team67.services.management.StudioService;

import java.io.IOException;
import java.util.ArrayList;

import static seng201.team67.models.enums.Rarity.*;

/**
 * Controls the the studio view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class TheStudioController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The selected artist. */
    private Artist selectedArtist;
    /** Service used to manage studio behaviour. */
    private final StudioService studioService;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    /** The label name. */
    @FXML public javafx.scene.control.Label labelName;
    /** FXML reference for the money text control. */
    @FXML public Label moneyText;
    /** FXML reference for the artist card one control. */
    @FXML private VBox artistCardOne;
    /** FXML reference for the artist card two control. */
    @FXML private VBox artistCardTwo;
    /** FXML reference for the artist card three control. */
    @FXML private VBox artistCardThree;
    /** FXML reference for the buy artist button control. */
    @FXML private Button buyArtistButton;
    /** FXML reference for the insufficient funds warning control. */
    @FXML private Label insufficientFundsWarning;
    /** FXML reference for the standard record text control. */
    @FXML private Label standardRecordText;
    /** FXML reference for the golden record text control. */
    @FXML private Label goldenRecordText;
    /** FXML reference for the platinum record text control. */
    @FXML private Label platinumRecordText;


    /**
     * Creates a new the studio controller.
     * @param gameEnvironment the active game environment
     */
    public TheStudioController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        this.studioService = new StudioService(gameEnvironment);
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

        standardRecordText.setText(String.format("$%d", gameEnvironment.getConfig().gachaStandardCost));
        goldenRecordText.setText(String.format("$%d", gameEnvironment.getConfig().gachaGoldenCost));
        platinumRecordText.setText(String.format("$%d", gameEnvironment.getConfig().gachaPlatinumCost));


        loadArtistPool();

        gameEnvironment.getMusicService().playTheStudioMusic();
    }

    private void loadArtistPool()
    {
        ArrayList<Artist> pool = getArtistPool();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            VBox card = cards[i];
            if (i < pool.size()) {
                Artist artist = pool.get(i);
                card.setDisable(false);
                ArtistDetailBoxFiller.populateArtistBox(card, artist, null);
                card.setOnMouseClicked(e -> selectArtist(card, artist));
            } else {
                clearArtistCard(card);
            }
        }

        selectedArtist = null;
        buyArtistButton.setVisible(false);
        hideInsufficientFundsWarning();
    }

    private void clearArtistCard(VBox card) {
        card.getChildren().clear();
        card.setDisable(true);
        card.setOnMouseClicked(null);
        ArtistDetailBoxFiller.applyBaseStyle(card);
    }

    private void selectArtist(VBox card, Artist artist)
    {
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };
        for (VBox c : cards) {
            if (!c.isDisable()) {
                ArtistDetailBoxFiller.applyBaseStyle(c);
            }
        }
        ArtistDetailBoxFiller.applySelectedStyle(card);
        selectedArtist = artist;
        buyArtistButton.setVisible(true);
        hideInsufficientFundsWarning();
    }

    /**
     * Handles hiring an artist from the purchasable artists
     */
    @FXML public void handleHireArtist()
    {
        if (selectedArtist == null) {
            return;
        }

        if (gameEnvironment.getLabelService().hireArtist(selectedArtist))
        {
            gameEnvironment.removeArtistFromPurchasePool(selectedArtist);
            moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
            loadArtistPool();
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
     * Processes buying a standard gacha
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void buyStandard(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaStandardCost))
        {
            hideInsufficientFundsWarning();
            screenNavigator.navigate(event, "/fxml/market/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, RARE));
            return;
        }

        showInsufficientFundsWarning();
    }

    /**
     * Processes buying a golden gacha
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void buyGolden(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaGoldenCost))
        {
            hideInsufficientFundsWarning();
            screenNavigator.navigate(event, "/fxml/market/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, VERY_RARE));
            return;
        }

        showInsufficientFundsWarning();
    }

    /**
     * Processes buying a platinum gacha
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void buyPlatinum(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaPlatinumCost))
        {
            hideInsufficientFundsWarning();
            screenNavigator.navigate(event, "/fxml/market/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, ULTRA));
            return;
        }

        showInsufficientFundsWarning();
    }

    private ArrayList<Artist> getArtistPool()
    {
        return studioService.getArtistPurchasePool();
    }

    /**
     * Rerolls the purchasable artists
     */
    @FXML public void rerollArtists()
    {
        if (gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().artistRerollCost)) {
            gameEnvironment.setArtistPoolGenerated(false);
            moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
            loadArtistPool();
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
