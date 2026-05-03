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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.models.Artist;
import seng201.team67.services.ConcertService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsController {

    private GameEnvironment gameEnvironment;
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
    @FXML private Label crowdHype;
    @FXML private Label staminaChange;
    @FXML private Label artistPay;
    @FXML private Label totalMoney;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public ResultsController(GameEnvironment gameEnvironment, ConcertService concertService)
    {
        this.gameEnvironment = gameEnvironment;
        this.concertService = concertService;
    }

    @FXML private void initialize()
    {
        loadLineup();
        payText.setText("$" + Double.toString(concertService.getIncome()));
        ticketSales.setText("$" + Double.toString(gameEnvironment.getConfig().ticketSalesAmount));
        crowdHype.setText(Integer.toString(concertService.getCrowdEnergyChange()));
        staminaChange.setText("-" + Double.toString(concertService.totalStaminaDrain()));
        artistPay.setText("-$" + Double.toString(gameEnvironment.getLabelService().getLineupTotalPay()));
        totalMoney.setText("$" + Double.toString(gameEnvironment.getConfig().ticketSalesAmount + concertService.getIncome() - gameEnvironment.getLabelService().getLineupTotalPay()));
    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnvironment.getLabelService().getLineup();
        List<VBox> cards = List.of(artistCardOne, artistCardTwo, artistCardThree);
        configureArtistPane(cards, pool.size());

        for (int i = 0; i < cards.size(); i++) {
            VBox card = cards.get(i);
            card.getChildren().clear();
            if (i < pool.size()) {
                Artist artist = pool.get(i);
                card.setStyle("-fx-border-color: #888888; -fx-border-width: 2; -fx-background-color: #f5f5f5;");
                populateCard(card, artist);
            }
        }
    }

    private void configureArtistPane(List<VBox> cards, int lineupSize) {
        List<VBox> visibleCards = new ArrayList<>();
        int visibleCount = Math.max(1, Math.min(lineupSize, cards.size()));

        for (int i = 0; i < cards.size(); i++) {
            VBox card = cards.get(i);
            if (i < visibleCount) {
                card.setVisible(true);
                card.setManaged(true);
                visibleCards.add(card);
            } else {
                card.setVisible(false);
                card.setManaged(false);
            }
        }

        artistPane.getItems().setAll(visibleCards);
        artistPane.setPrefWidth(visibleCount == 1 ? 320 : visibleCount == 2 ? 650 : 977);
        artistPane.setLayoutX((1280 - artistPane.getPrefWidth()) / 2);
    }

    private void populateCard(VBox card, Artist artist)
    {
        Label nameLabel = new Label(artist.getName());
        Label typeLabel = new Label(artist.getType());
        Label starPowerLabel = new Label("Star Power: " + artist.getStarPower());
        Label staminaLabel = new Label("Stamina: " + artist.getStamina());
        Label healthLabel = new Label("Health: " + artist.getHealth());
        Label costLabel = new Label("Hire: $" + (int) artist.getCost());

        card.getChildren().addAll(nameLabel, typeLabel, starPowerLabel, staminaLabel, healthLabel, costLabel);
        card.setPadding(new Insets(8));
        card.setAlignment(Pos.CENTER);
    }

    @FXML private void continueGame(ActionEvent event) throws IOException {
        if (concertService.getTourService().isEndedByExhaustion()) {
            concertService.getTourService().tourEnded();
            gameEnvironment.setPoolGenerated(false);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TourResults.fxml"));
            if(concertService.getTourService().isEndedByExhaustion())
            {
                loader.setController(new TourResultsController(gameEnvironment, concertService.getTourService(), true));
            }
            else
            {
                loader.setController(new TourResultsController(gameEnvironment, concertService.getTourService(), false));
            }

            Parent root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainGame.fxml"));
        loader.setController(new MainGameController(gameEnvironment, concertService.getTourService()));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
