package seng201.team67.gui.instantiable.questions;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import seng201.team67.GameEnvironment;
import seng201.team67.models.Skill;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.GameplayEffect;
import seng201.team67.models.enums.questions.PayoutType;
import seng201.team67.models.questionmodels.Outcome;
import seng201.team67.services.gameplay.PayoutService;
import seng201.team67.services.gameplay.StaminaService;
import seng201.team67.services.gameplay.TourService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the outcome view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class OutcomeController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** Service used to manage tour behaviour. */
    private final TourService tourService;
    /** The outcome. */
    private final Outcome outcome;
    /** The on continue. */
    private final Runnable onContinue;

    /** FXML reference for the outcome description control. */
    @FXML private Label outcomeDescription;
    /** FXML reference for the stat change box control. */
    @FXML private HBox statChangeBox;
    /** FXML reference for the credits change label control. */
    @FXML private Label creditsChangeLabel;
    /** FXML reference for the stamina change label control. */
    @FXML private Label staminaChangeLabel;
    /** FXML reference for the crowd energy change label control. */
    @FXML private Label crowdEnergyChangeLabel;
    /** FXML reference for the continue button control. */
    @FXML
    private Button continueButton;

    /**
     * Creates a new outcome controller.
     * @param gameEnvironment the active game environment
     * @param tourService the tour service for the current run
     * @param outcome the outcome to apply
     * @param onContinue the on continue
     */
    public OutcomeController(GameEnvironment gameEnvironment, TourService tourService, Outcome outcome, Runnable onContinue) {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
        this.outcome = outcome;
        this.onContinue = onContinue;
    }

    @FXML
    private void initialize() {
        StaminaService staminaService = new StaminaService();
        double staminaChange = outcome.hasExplicitStaminaChange()
                ? outcome.getStaminaChange()
                : staminaService.getStaminaChangeAmount(gameEnvironment, outcome.getOutcomeType());

        outcomeDescription.setText(outcome.getDescription());
        creditsChangeLabel.setText("Outcome: " + outcome.getOutcomeType().getDisplayName()
                + " | " + formatPayoutText());
        staminaChangeLabel.setText(formatStaminaChangeText(staminaChange));
        crowdEnergyChangeLabel.setText("Crowd Energy: " + formatSignedNumber(outcome.getCrowdEnergyChange()));

        creditsChangeLabel.setTextFill(getPayoutColor(outcome.getPayoutType()));
        staminaChangeLabel.setTextFill(getStaminaColor(staminaChange));
        crowdEnergyChangeLabel.setTextFill(getCrowdEnergyColor(outcome.getCrowdEnergyChange()));
    }

    /**
     * Takes the payout type and returns the name.
     * @return Payout name
     */
    private String formatPayoutType()
    {
        return switch (outcome.getPayoutType()) {
            case MAJOR_REWARD -> "Major Reward";
            case MINOR_REWARD -> "Minor Reward";
            case MINOR_PENALTY -> "Minor Penalty";
            case MAJOR_PENALTY -> "Major Penalty";
            case NONE -> "None";
        };
    }

    /**
     * Takes the payout type and returns the payout amount, formatting into a string ready for UI
     * @return Payout Amount
     */
    private String formatPayoutText()
    {
        if (outcome.getPayoutType() == PayoutType.NONE)
        {
            return "Money: " + formatPayoutType();
        }

        PayoutService payoutService = new PayoutService();
        int currentPayout = (int) Math.round(payoutService.getPayoutAmount(gameEnvironment, outcome.getPayoutType()));
        List<String> modifiers = new ArrayList<>();

        for (Artist artist : gameEnvironment.getLabelService().getLineup())
        {
            Skill skill = artist.getSkill();
            if (skill == null || !skill.hasPayoutModifier())
            {
                continue;
            }

            int modifiedPayout = skill.getPayoutModifier().apply(
                    artist,
                    currentPayout,
                    outcome,
                    gameEnvironment.getLabelService().getLineup(),
                    0,
                    tourService.getConcertResults().size(),
                    0,
                    0
            );
            if (modifiedPayout == currentPayout)
            {
                continue;
            }

            String modifierText = formatPayoutModifierText(artist, skill, currentPayout, modifiedPayout);
            if (!modifierText.isBlank())
            {
                modifiers.add(modifierText);
            }

            currentPayout = modifiedPayout;
        }

        String payoutText = "Credits: " + formatSignedNumber(currentPayout) + " to label";
        if (!modifiers.isEmpty())
        {
            payoutText += " (" + String.join(", ", modifiers) + ")";
        }
        return payoutText;
    }

    /**
     * Takes the payout modifier, if it exists, formatting into a string ready for UI
     * @return Payout modifier
     */
    private String formatPayoutModifierText(Artist artist, Skill skill, int originalPayout, int modifiedPayout)
    {
        int delta = modifiedPayout - originalPayout;

        if (skill.hasEffect(GameplayEffect.FLAT_CREDIT_BONUS))
        {
            return artist.getName() + " " + formatSignedNumber(delta);
        }

        if (skill.hasEffect(GameplayEffect.TERRIBLE_PAYOUT_REDUCTION))
        {
            int reductionPercent = calculatePercentDifference(originalPayout, modifiedPayout);
            return reductionPercent > 0
                    ? artist.getName() + " " + reductionPercent + "% penalty reduction"
                    : "";
        }

        int percentDifference = calculatePercentDifference(originalPayout, modifiedPayout);
        if (percentDifference == 0)
        {
            return "";
        }

        if (originalPayout < 0)
        {
            String penaltyDirection = delta < 0 ? "harsher penalty" : "softer penalty";
            return artist.getName() + " " + percentDifference + "% " + penaltyDirection;
        }

        String sign = delta >= 0 ? "+" : "-";
        return artist.getName() + " " + sign + percentDifference + "%";
    }

    private int calculatePercentDifference(int originalValue, int modifiedValue)
    {
        if (originalValue == 0)
        {
            return 0;
        }

        return (int) Math.round(Math.abs((modifiedValue - originalValue) * 100.0 / originalValue));
    }

    private String formatSignedNumber(double value)
    {
        int roundedValue = (int) Math.round(value);
        return roundedValue > 0 ? "+" + roundedValue : Integer.toString(roundedValue);
    }

    private String formatStaminaChangeText(double staminaChange)
    {
        int roundedChange = (int) Math.round(staminaChange);
        String signedChange = formatSignedNumber(roundedChange);

        if (roundedChange < 0)
        {
            return formatTargetedStaminaDrain(-roundedChange);
        }

        if (roundedChange > 0)
        {
            return "Stamina: " + signedChange + " to full lineup";
        }

        return "Stamina: " + signedChange;
    }

    private String formatTargetedStaminaDrain(int drainAmount)
    {
        Artist targetArtist = getMostRecentDrainedArtist();
        if (targetArtist == null)
        {
            return "Stamina: -" + drainAmount + " to 1 rotating artist";
        }

        int adjustedDrain = drainAmount;
        int reductionPercent = 0;

        if (targetArtist.hasSkill()
                && targetArtist.getSkill().hasStatModifier()
                && targetArtist.getSkill().hasEffect(GameplayEffect.STAMINA_COST_REDUCTION))
        {
            int staminaPercent = GameplayEffect.STAMINA_COST_REDUCTION
                    .createStatModifier(targetArtist.getSkill().getMultiplier())
                    .apply(targetArtist, drainAmount);
            adjustedDrain = Math.max(0, (int) Math.round(drainAmount * (staminaPercent / 100.0)));
            reductionPercent = Math.max(0, 100 - staminaPercent);
        }

        String staminaText = "Stamina: -" + adjustedDrain + " to " + targetArtist.getName();
        if (reductionPercent > 0)
        {
            staminaText += " (" + reductionPercent + "% reduction)";
        }
        return staminaText;
    }

    private Artist getMostRecentDrainedArtist()
    {
        List<Artist> lineup = gameEnvironment.getLabelService().getLineup();
        if (lineup.isEmpty())
        {
            return null;
        }

        int previousIndex = Math.floorMod(tourService.getCurrentLineupStaminaIndex() - 1, lineup.size());
        return lineup.get(previousIndex);
    }

    private Color getPayoutColor(PayoutType payoutType)
    {
        return switch (payoutType) {
            case MAJOR_REWARD -> Color.web("#006400");
            case MINOR_REWARD -> Color.web("#32CD32");
            case MINOR_PENALTY -> Color.web("#FF6B6B");
            case MAJOR_PENALTY -> Color.web("#8B0000");
            case NONE -> Color.BLACK;
        };
    }

    private Color getStaminaColor(double staminaChange)
    {
        if (staminaChange == 0)
        {
            return Color.BLACK;
        }

        if (staminaChange > 0)
        {
            return Math.abs(staminaChange) >= 10 ? Color.web("#006400") : Color.web("#32CD32");
        }

        return Math.abs(staminaChange) >= 15 ? Color.web("#8B0000") : Color.web("#FF6B6B");
    }

    private Color getCrowdEnergyColor(int crowdEnergyChange)
    {
        if (crowdEnergyChange == 0)
        {
            return Color.BLACK;
        }

        if (crowdEnergyChange > 0)
        {
            return crowdEnergyChange >= 15 ? Color.web("#006400") : Color.web("#32CD32");
        }

        return Math.abs(crowdEnergyChange) >= 15 ? Color.web("#8B0000") : Color.web("#FF6B6B");
    }

    @FXML
    private void onContinueClicked() {
        onContinue.run();
    }
}
