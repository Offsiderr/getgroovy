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
import seng201.team67.services.MinigamesService;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class SoundEngineerStandoffController {

    private GameEnvironment gameEnvironment;
    private MinigamesService minigamesService;

    private Consumer<MiniGameResult> gameResult;

    @FXML private Slider sliderOne;
    @FXML private Slider sliderTwo;
    @FXML private Slider sliderThree;
    @FXML private Slider sliderFour;

    @FXML private Button buttonOne;

    @FXML private Pane resultsPane;
    @FXML private Label resultsText;

    private List<Double> targetValues;

    private Boolean shown = false;

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
        //target values the player must recall — generated randomly
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
