package seng201.team67.gui.controllers.instantiable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seng201.team67.models.questionmodels.Outcome;

public class OutcomeController {

    private final Outcome outcome;
    private final Runnable onContinue;

    @FXML private Label outcomeDescription;
    @FXML private HBox statChangeBox;
    @FXML private Label creditsChangeLabel;
    @FXML private Label staminaChangeLabel;
    @FXML private Label crowdEnergyChangeLabel;
    @FXML
    private Button continueButton;

    public OutcomeController(Outcome outcome, Runnable onContinue) {
        this.outcome = outcome;
        this.onContinue = onContinue;
    }

    @FXML
    private void initialize() {
        outcomeDescription.setText(outcome.getDescription());
        creditsChangeLabel.setText("Credits: " + outcome.getPayoutType().toString());
        staminaChangeLabel.setText("Stamina: " + outcome.getStaminaChange());
        crowdEnergyChangeLabel.setText("Crowd Energy " + outcome.getCrowdEnergyChange());
    }

    @FXML
    private void onContinueClicked() {
        onContinue.run();
    }
}
