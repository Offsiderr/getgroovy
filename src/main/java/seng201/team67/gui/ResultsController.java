package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.team67.GameEnviroment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.models.Artist;
import seng201.team67.services.ConcertService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsController {

    private GameEnviroment gameEnviroment;
    private ConcertService concertService;

    private List<Artist> lineup;

    //not needed currently, but here if needed in the future.
    private final List<ArtistCardController> artistCards = new ArrayList<>();

    //FXML stuff
    @FXML
    private SplitPane artistPane;
    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private Label ticketSales;
    @FXML private Label payText; //bonus money

    private Stage stage;
    private Scene scene;
    private Parent root;

    public ResultsController(GameEnviroment gameEnviroment, ConcertService concertService)
    {
        this.gameEnviroment = gameEnviroment;
        this.concertService = concertService;
    }

    @FXML private void initialize()
    {
        loadLineup();
        payText.setText(Double.toString(concertService.getIncome()));
    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnviroment.getLabelService().getLineup();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            cards[i].getChildren().clear();
            cards[i].setStyle("-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;");
            if (i < pool.size()) {
                Artist artist = pool.get(i);
                populateCard(cards[i], artist);
            }
        }
    }

    private void populateCard(VBox card, Artist artist)
    {
        Label nameLabel = new Label(artist.getName());
        Label typeLabel = new Label(artist.getType());
        Label starPowerLabel = new Label("Star Power: " + artist.getStar_power());
        Label staminaLabel = new Label("Stamina: " + artist.getStamina());
        Label healthLabel = new Label("Health: " + artist.getHealth());
        Label costLabel = new Label("Hire: $" + (int) artist.getCost());

        card.getChildren().addAll(nameLabel, typeLabel, starPowerLabel, staminaLabel, healthLabel, costLabel);
        card.setPadding(new Insets(8));
        card.setAlignment(Pos.CENTER);
    }

    @FXML private void continueGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainGame.fxml"));
        loader.setController(new MainGameController(gameEnviroment, concertService.getTourService()));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
