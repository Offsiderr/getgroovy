package seng201.team67.gui.dev;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import seng201.team67.gui.tour.MainConcertController;

public class DevFunctionsInGameController {

    private final MainConcertController mainConcertController;

    public DevFunctionsInGameController(MainConcertController mainConcertController)
    {
        this.mainConcertController = mainConcertController;
    }

    @FXML
    public void setCrowdToSeventyFive(ActionEvent event)
    {
        mainConcertController.debugSetCrowd(75);
    }

    @FXML
    public void setAnsweredQuestionsToFour(ActionEvent event)
    {
        mainConcertController.debugSetAnsweredQuestions(4);
    }

    @FXML
    public void forceGoodOutcome(ActionEvent event)
    {
        mainConcertController.debugForceGoodOutcome();
    }

    @FXML
    public void setWinStreakToThree(ActionEvent event)
    {
        mainConcertController.debugSetWinStreak(3);
    }

    @FXML
    public void runApplyItemConcertModifiers(ActionEvent event)
    {
        mainConcertController.debugApplyItemConcertModifiers();
    }

    @FXML
    public void setRetirementRiskForAllToOneHundred(ActionEvent event)
    {
        mainConcertController.debugSetRetirementRiskForAll(100);
    }
}
