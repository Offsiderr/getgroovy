package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;

import java.io.IOException;
import java.util.ArrayList;

import static seng201.team67.models.enums.Rarity.*;

public class TheStudioController {

    private final GameEnvironment gameEnvironment;
    private Artist selectedArtist;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

    @FXML public javafx.scene.control.Label labelName;
    @FXML public Label moneyText;
    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;
    @FXML private Button buyArtistButton;


    public TheStudioController(GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML public void initialize() throws IOException
    {
        this.labelName.setText(gameEnvironment.getLabelService().getLabelName());
        this.moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));

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
    }

    @FXML public void handleHireArtist()
    {
        if (selectedArtist != null && gameEnvironment.getLabelService().hireArtist(selectedArtist))
        {
            gameEnvironment.removeArtistFromPurchasePool(selectedArtist);
            moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
            loadArtistPool();
        }
    }

    @FXML public void returnToMainMenu(ActionEvent event) throws IOException {
        screenNavigator.navigate(event, "/fxml/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

    @FXML public void buyStandard(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaStandardCost))
        {
            screenNavigator.navigate(event, "/fxml/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, RARE));
        }
    }

    @FXML public void buyGolden(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaGoldenCost))
        {
            screenNavigator.navigate(event, "/fxml/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, VERY_RARE));
        }
    }

    @FXML public void buyPlatinum(ActionEvent event) throws IOException
    {
        if(gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().gachaPlatinumCost))
        {
            screenNavigator.navigate(event, "/fxml/GatchaSelection.fxml",
                    new GachaSelectionController(gameEnvironment, true, gameEnvironment.getConfig().gachaPoolSize, ULTRA));
        }
    }

    private ArrayList<Artist> getArtistPool()
    {
        return gameEnvironment.resetArtistPurchasePool();
    }

    @FXML public void rerollArtists()
    {
        if (gameEnvironment.getLabelService().buyItem(gameEnvironment.getConfig().artistRerollCost)) {
            gameEnvironment.setArtistPoolGenerated(false);
            moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
            loadArtistPool();
        }
    }

}
