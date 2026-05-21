package seng201.team67.gui.instantiable.minigames;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import seng201.team67.GameEnvironment;
import seng201.team67.models.minigames.MiniGameResult;
import seng201.team67.services.gameplay.MinigamesService;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Controls the sound engineer standoff view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class SoundEngineerStandoffController {

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;
    /** Service used to manage minigames behaviour. */
    private MinigamesService minigamesService;

    /** The game result. */
    private Consumer<MiniGameResult> gameResult;

    /** The slider one. */
    @FXML private Slider sliderOne;
    /** The slider two. */
    @FXML private Slider sliderTwo;
    /** The slider three. */
    @FXML private Slider sliderThree;
    /** The slider four. */
    @FXML private Slider sliderFour;

    /** FXML reference for the button one control. */
    @FXML private Button buttonOne;

    /** FXML reference for the results pane control. */
    @FXML private Pane resultsPane;
    /** FXML reference for the results text control. */
    @FXML private Label resultsText;

    /** Collection that stores the target values. */
    private List<Double> targetValues;

    /** Whether shown. */
    private Boolean shown = false;

    /**
     * Creates a new sound engineer standoff controller.
     * @param minigamesService the minigames service to use
     * @param gameResult the game result
     * @param gameEnvironment the active game environment
     */
    public SoundEngineerStandoffController(MinigamesService minigamesService, Consumer<MiniGameResult> gameResult, GameEnvironment gameEnvironment)
    {
        this.gameEnvironment = gameEnvironment;
        this.minigamesService = minigamesService;
        this.gameResult = gameResult;
    }

    @FXML private void initialize()
    {
        buttonOne.setText("Show Values");
        resultsPane.setVisible(false);
    }

    @FXML private void confirmButton(ActionEvent event) throws IOException
    {
        if(shown == false)
        {
            shown = true;
            buttonOne.setVisible(false);
            showValues();
        }
        else
        {
            confirmValues();
        }
    }

    private void showValues()
    {
        //target values the player must recall â€” generated randomly
        targetValues = List.of(
                gameEnvironment.getConfig().soundEngineerTargetMin + Math.random() * gameEnvironment.getConfig().soundEngineerTargetRange,
                gameEnvironment.getConfig().soundEngineerTargetMin + Math.random() * gameEnvironment.getConfig().soundEngineerTargetRange,
                gameEnvironment.getConfig().soundEngineerTargetMin + Math.random() * gameEnvironment.getConfig().soundEngineerTargetRange,
                gameEnvironment.getConfig().soundEngineerTargetMin + Math.random() * gameEnvironment.getConfig().soundEngineerTargetRange
        );

        double flashSeconds = gameEnvironment.getConfig().soundEngineerFlashDurationSeconds; //(stamina / 100.0);  //Stamina will affect this in the future
        showTargetBriefly(flashSeconds);
    }

    private void confirmValues()
    {
        List<Double> playerValues = List.of(
                sliderOne.getValue(),
                sliderTwo.getValue(),
                sliderThree.getValue(),
                sliderFour.getValue()
        );

        long matched = minigamesService.soundEngineerStandoff.countMatches(playerValues, targetValues, gameEnvironment.getConfig().soundEngineerMatchTolerance); // within 15 units

        resultsPane.setVisible(true);


        switch ((int) matched)
        {
            case 4:
                resultsText.setText("Perfect mix. The fans love it!");
                break;
            case 3, 2:
                resultsText.setText("Decent fix. The fans don't notice anything.");
                break;
            default:
                resultsText.setText("Awful mix! Your engineer storms off!");
                break;
        }


        MiniGameResult result = switch ((int) matched) {
            case 4    -> minigamesService.minigame.getPerfectResult();
            case 3, 2 -> minigamesService.minigame.getGoodResult();
            default   -> minigamesService.minigame.getBadResult();
        };

        buttonOne.setVisible(true);
        buttonOne.setText("Continue");
        buttonOne.setOnAction(e -> gameResult.accept(result));

    }



    private void showTargetBriefly(double seconds) {
        // Set sliders to target, then reset after delay
        sliderOne.setValue(targetValues.get(0));
        sliderTwo.setValue(targetValues.get(1));
        sliderThree.setValue(targetValues.get(2));
        sliderFour.setValue(targetValues.get(3));

        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(e -> {
            sliderOne.setValue(gameEnvironment.getConfig().soundEngineerSliderDefault);
            sliderTwo.setValue(gameEnvironment.getConfig().soundEngineerSliderDefault);
            sliderThree.setValue(gameEnvironment.getConfig().soundEngineerSliderDefault);
            sliderFour.setValue(gameEnvironment.getConfig().soundEngineerSliderDefault);
            buttonOne.setVisible(true);
            buttonOne.setText("Confirm Mix");
        });
        pause.play();
    }
}
