package seng201.team67.gui.results;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.setup.StartController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.services.gameplay.GameStatusService;

import java.io.IOException;

/**
 * Controls the lose screen view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class LoseScreenController {

    /** FXML reference for the label name control. */
    @FXML private Label labelName;
    /** FXML reference for the selected tours control. */
    @FXML private Label selectedTours;
    /** FXML reference for the tour count control. */
    @FXML private Label tourCount;
    /** FXML reference for the concert count control. */
    @FXML private Label concertCount;
    /** FXML reference for the total money spent control. */
    @FXML private Label totalMoneySpent;
    /** FXML reference for the total money earnt control. */
    @FXML private Label totalMoneyEarnt;
    /** FXML reference for the finishing balance control. */
    @FXML private Label finishingBalance;
    /** FXML reference for the final score control. */
    @FXML private Label finalScore;
    /** FXML reference for the loss reason control. */
    @FXML private Label lossReason;

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();


    /**
     * Creates a new lose screen controller.
     * @param gameEnvironment the active game environment
     */
    public LoseScreenController (GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize() {
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        if (gameEnvironment.getLabelService().getMoney() <= GameStatusService.BANKRUPTCY_LIMIT) {
            lossReason.setText("You went past -$500, so the bank repo'd your office equipment, and the artists all left.");
        }
        selectedTours.setText("Selected Tours: " + gameEnvironment.getSelectedNumTours());
        tourCount.setText("Total Tours: " + gameEnvironment.getTourCount());
        concertCount.setText("Total Concerts: " + gameEnvironment.getConcertCount());
        totalMoneySpent.setText("Total Money Spent: " + formatMoney(gameEnvironment.getTotalMoneySpent()));
        totalMoneyEarnt.setText("Total Money Earnt: " + formatMoney(gameEnvironment.getTotalMoneyEarnt()));
        finishingBalance.setText("Finishing Balance: " + formatMoney(gameEnvironment.getLabelService().getMoney()));
        finalScore.setText("Final Score: " + gameEnvironment.getGameScore());
    }

    private String formatMoney(double amount)
    {
        return String.format("$%.2f", amount);
    }

    /**
     * Quits the game
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void quitGame(ActionEvent event) throws IOException
    {
        System.exit(0);
    }

    /**
     * Starts a new game
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void startNewGame(ActionEvent event) throws IOException {

        GameEnvironment gameEnvironment = new GameEnvironment();
        screenNavigator.navigate(event, "/fxml/setup/startmenu.fxml", new StartController());
    }
}
