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

public class TheStudioController {

    private final GameEnvironment gameEnvironment;
    private Artist selectedArtist;
    private final StudioService studioService;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    @FXML public javafx.scene.control.Label labelName;
    @FXML public Label moneyText;
    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;
    @FXML private Button buyArtistButton;
    @FXML private Label insufficientFundsWarning;


    public TheStudioController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        this.studioService = new StudioService(gameEnvironment);
    }

    @FXML public void initialize() throws IOException
    {
        this.labelName.setText(gameEnvironment.getLabelService().getLabelName());
        this.moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
        insufficientFundsWarning.setVisible(false);

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

    @FXML public void handleHireArtist()
    {
        if (selectedArtist == null) {
            return;
        }

        if (gameEnvironment.getLabelService().hireArtist(selectedArtist))
        {
            gameEnvironment.removeArtistFromPurchasePool(selectedArtist);
            moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
            loadArtistPool();
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
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, RARE));
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
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, VERY_RARE));
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
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, ULTRA));
            return;
        }

        showInsufficientFundsWarning();
    }

    private ArrayList<Artist> getArtistPool()
    {
        return studioService.getArtistPurchasePool();
    }

    @FXML public void rerollArtists()
    {
        if (gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().artistRerollCost)) {
            gameEnvironment.setArtistPoolGenerated(false);
            moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
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
