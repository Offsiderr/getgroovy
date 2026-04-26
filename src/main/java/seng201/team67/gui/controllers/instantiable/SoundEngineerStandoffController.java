package seng201.team67.gui.controllers.instantiable;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import seng201.team67.GameEnviroment;
import seng201.team67.interfaces.Minigame;
import seng201.team67.models.GameConfig;
import seng201.team67.models.MiniGameResult;
import seng201.team67.models.minigames.SoundEngineerStandoff;
import seng201.team67.services.MinigamesService;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class SoundEngineerStandoffController {

    private GameEnviroment gameEnviroment;
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

    public SoundEngineerStandoffController(MinigamesService minigamesService, Consumer<MiniGameResult> gameResult, GameEnviroment gameEnviroment)
    {
        this.gameEnviroment = gameEnviroment;
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
                gameEnviroment.getConfig().soundEngineerTargetMin + Math.random() * gameEnviroment.getConfig().soundEngineerTargetRange,
                gameEnviroment.getConfig().soundEngineerTargetMin + Math.random() * gameEnviroment.getConfig().soundEngineerTargetRange,
                gameEnviroment.getConfig().soundEngineerTargetMin + Math.random() * gameEnviroment.getConfig().soundEngineerTargetRange,
                gameEnviroment.getConfig().soundEngineerTargetMin + Math.random() * gameEnviroment.getConfig().soundEngineerTargetRange
        );

        double flashSeconds = gameEnviroment.getConfig().soundEngineerFlashDurationSeconds; //(stamina / 100.0);  //Stamina will affect this in the future
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

        long matched = minigamesService.soundEngineerStandoff.countMatches(playerValues, targetValues, gameEnviroment.getConfig().soundEngineerMatchTolerance); // within 15 units

        resultsPane.setVisible(true);


        switch ((int) matched)
        {
            case 4:
                resultsText.setText("Perfect mix. The fans love it!");
            case 3, 2:
                resultsText.setText("Decent fix. The fans don't notice anything.");
            default:
                resultsText.setText("Awful mix! Your engineer storms off!");
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
            sliderOne.setValue(gameEnviroment.getConfig().soundEngineerSliderDefault);
            sliderTwo.setValue(gameEnviroment.getConfig().soundEngineerSliderDefault);
            sliderThree.setValue(gameEnviroment.getConfig().soundEngineerSliderDefault);
            sliderFour.setValue(gameEnviroment.getConfig().soundEngineerSliderDefault);
            buttonOne.setVisible(true);
            buttonOne.setText("Confirm Mix");
        });
        pause.play();
    }
}
