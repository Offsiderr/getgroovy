package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.Artist;
import seng201.team67.services.MusicService;

import java.io.IOException;
import java.util.ArrayList;

import static seng201.team67.models.enums.Rarity.*;

public class TheStudioController {

    private GameEnvironment gameEnvironment;
    private MusicService musicService;
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
        this.musicService = new MusicService(gameEnvironment);
    }

    @FXML public void initialize() throws IOException
    {
        this.labelName.setText(gameEnvironment.getLabelService().getLabelName());
        this.moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));

        loadArtistPool();

        musicService.playTheStudioMusic();
    }

    private void loadArtistPool()
    {
        ArrayList<Artist> pool = getArtistPool();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            cards[i].getChildren().clear();
            cards[i].setOnMouseClicked(null);
            cards[i].setStyle("-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;");
            if (i < pool.size()) {
                Artist artist = pool.get(i);
                populateCard(cards[i], artist);
            }
        }

        selectedArtist = null;
        buyArtistButton.setVisible(false);
    }

    private void populateCard(VBox card, Artist artist)
    {
        //Ended up using a different thing than the artist card for the studio,
        //this was for sizing but reminds me I need to fix the labels on the card bc it isn't clear.
        //insets are a new thing I discovered that we should be using later.
        Label nameLabel = new Label(artist.getName());
        Label typeLabel = new Label(artist.getType());
        Label starPowerLabel = new Label("Star Power: " + artist.getStarPower());
        Label staminaLabel = new Label("Stamina: " + artist.getStamina());
        Label healthLabel = new Label("Health: " + artist.getHealth());
        Label costLabel = new Label("Hire: $" + (int) artist.getCost());

        card.getChildren().addAll(nameLabel, typeLabel, starPowerLabel, staminaLabel, healthLabel, costLabel);
        card.setPadding(new Insets(8));
        card.setAlignment(Pos.CENTER);

        card.setOnMouseClicked(e -> selectArtist(card, artist));
    }

    private void selectArtist(VBox card, Artist artist)
    {
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };
        for (VBox c : cards) {
            c.setStyle("-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;");
        }
        card.setStyle("-fx-border-color: #0078d7; -fx-border-width: 3; -fx-background-color: #dce9f7;");
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
            gameEnvironment.setPoolGenerated(false);
            moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
            loadArtistPool();
        }
    }

}
