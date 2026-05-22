package seng201.team67.gui.dev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import seng201.team67.gui.tour.MainConcertController;

/**
 * Controls the dev functions in game view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class DevFunctionsInGameController {

    /** The main concert controller. */
    private final MainConcertController mainConcertController;

    /**
     * Creates a new dev functions in game controller.
     * @param mainConcertController the main concert controller to use
     */
    public DevFunctionsInGameController(MainConcertController mainConcertController)
    {
        this.mainConcertController = mainConcertController;
    }

    /**
     * Sets the crowd meter to seventy five.
     * @param event the action event that triggered the request
     */
    @FXML
    public void setCrowdToSeventyFive(ActionEvent event)
    {
        mainConcertController.debugSetCrowd(75);
    }

    /**
     * Sets the answered questions to four.
     * @param event the action event that triggered the request
     */
    @FXML
    public void setAnsweredQuestionsToFour(ActionEvent event)
    {
        mainConcertController.debugSetAnsweredQuestions(4);
    }

    /**
     * Forces a good outcome.
     * @param event the action event that triggered the request
     */
    @FXML
    public void forceGoodOutcome(ActionEvent event)
    {
        mainConcertController.debugForceGoodOutcome();
    }

    /**
     * Sets the win streak to three.
     * @param event the action event that triggered the request
     */
    @FXML
    public void setWinStreakToThree(ActionEvent event)
    {
        mainConcertController.debugSetWinStreak(3);
    }

    /**
     * Applies the item modifiers that have an effect on the concert.
     * @param event the action event that triggered the request
     */
    @FXML
    public void runApplyItemConcertModifiers(ActionEvent event)
    {
        mainConcertController.debugApplyItemConcertModifiers();
    }

    /**
     * Sets the retirement risk for all artists to one hundred.
     * @param event the action event that triggered the request
     */
    @FXML
    public void setRetirementRiskForAllToOneHundred(ActionEvent event)
    {
        mainConcertController.debugSetRetirementRiskForAll(100);
    }

    /**
     * Sets the stamina of all artists to five.
     * @param event the action event that triggered the request
     */
    @FXML
    public void setStaminaForAllToFive(ActionEvent event)
    {
        mainConcertController.debugSetStaminaForAll(5);
    }

    /**
     * Sets the player money to negative one thousand.
     * @param event the action event that triggered the request
     */
    @FXML
    public void setMoneyToNegativeOneThousand(ActionEvent event)
    {
        mainConcertController.debugSetMoney(-1000);
    }

    /**
     * Gives the player one thousand dollars.
     * @param event the action event that triggered the request
     */
    @FXML
    public void givePlayerOneThousand(ActionEvent event)
    {
        mainConcertController.debugGiveMoney(1000);
    }
}
