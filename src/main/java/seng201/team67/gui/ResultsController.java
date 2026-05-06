package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;
import seng201.team67.services.ConcertService;

import java.io.IOException;
import java.util.List;

public class ResultsController {

    private GameEnvironment gameEnvironment;
    private ConcertService concertService;

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
    private final ScreenNavigator screenNavigator = new ScreenNavigator();

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
            if (i < pool.size()) {
                card.setDisable(false);
                ArtistDetailBoxFiller.populateArtistBox(card, pool.get(i));
            } else {
                clearArtistCard(card);
            }
        }
    }

    private void configureArtistPane(List<VBox> cards, int artistCount) {
        int visibleCount = Math.max(0, Math.min(artistCount, cards.size()));
        artistPane.getItems().setAll(cards.subList(0, visibleCount).toArray(Node[]::new));

        if (visibleCount == 2) {
            artistPane.setDividerPositions(0.5);
        } else if (visibleCount >= 3) {
            artistPane.setDividerPositions(1.0 / 3.0, 2.0 / 3.0);
        }
    }

    private void clearArtistCard(VBox card) {
        card.getChildren().clear();
        card.setDisable(true);
        ArtistDetailBoxFiller.applyBaseStyle(card);
    }

    @FXML private void continueGame(ActionEvent event) throws IOException {
        if (concertService.getTourService().isEndedByExhaustion()) {
            concertService.getTourService().tourEnded();
            gameEnvironment.setArtistPoolGenerated(false);

            if(concertService.getTourService().isEndedByExhaustion())
            {
                screenNavigator.navigate(event, "/fxml/TourResults.fxml",
                        new TourResultsController(gameEnvironment, concertService.getTourService(), true));
            }
            else
            {
                screenNavigator.navigate(event, "/fxml/TourResults.fxml",
                        new TourResultsController(gameEnvironment, concertService.getTourService(), false));
            }
            return;
        }

        screenNavigator.navigate(event, "/fxml/MainGame.fxml",
                new MainGameController(gameEnvironment, concertService.getTourService()));
    }
}
