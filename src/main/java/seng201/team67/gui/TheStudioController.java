package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.team67.GameEnviroment;
import seng201.team67.models.Artist;

import java.io.IOException;
import java.util.ArrayList;

import static seng201.team67.models.enums.Rarity.*;

public class TheStudioController {

    private GameEnviroment gameEnviroment;
    private Artist selectedArtist;

    @FXML public javafx.scene.control.Label labelName;
    @FXML public Label moneyText;
    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;
    @FXML private Button buyArtistButton;


    public TheStudioController(GameEnviroment gameEnviroment)
    {
        this.gameEnviroment = gameEnviroment;
    }

    @FXML public void initialize() throws IOException
    {
        this.labelName.setText(gameEnviroment.getLabelService().getLabelName());
        this.moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));

        loadArtistPool();

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
        Label starPowerLabel = new Label("Star Power: " + artist.getStar_power());
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
        if (selectedArtist != null && gameEnviroment.getLabelService().hireArtist(selectedArtist))
        {
            moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));
            loadArtistPool();
        }
    }

    @FXML public void returnToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        loader.setController(new MainMenuController(gameEnviroment));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML public void buyStandard(ActionEvent event) throws IOException
    {
        if(gameEnviroment.getLabelService().buyItem(100))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GatchaSelection.fxml"));
            loader.setController(new GatchaSelectionController(gameEnviroment, true, 3, RARE));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML public void buyGolden(ActionEvent event) throws IOException
    {
        if(gameEnviroment.getLabelService().buyItem(500))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GatchaSelection.fxml"));
            loader.setController(new GatchaSelectionController(gameEnviroment, true, 3, VERY_RARE));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML public void buyPlatinum(ActionEvent event) throws IOException
    {
        if(gameEnviroment.getLabelService().buyItem(1000))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GatchaSelection.fxml"));
            loader.setController(new GatchaSelectionController(gameEnviroment, true, 3, ULTRA));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    private ArrayList<Artist> getArtistPool()
    {
        return gameEnviroment.resetArtistPurchasePool();
    }

    @FXML public void rerollArtists()
    {
        if (gameEnviroment.getLabelService().buyItem(100)) {
            gameEnviroment.setPoolGenerated(false);
            moneyText.setText(Double.toString(gameEnviroment.getLabelService().getMoney()));
            loadArtistPool();
        }
    }

}
