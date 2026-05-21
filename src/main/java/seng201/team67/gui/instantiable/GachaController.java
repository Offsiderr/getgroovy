package seng201.team67.gui.instantiable;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import seng201.team67.GameEnvironment;
import seng201.team67.services.audio.SoundEffectsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GachaController {

    private static final double SHAKE_DISTANCE = 8.0;
    private static final double SHAKE_DURATION_MILLIS = 45.0;
    private static final int SHAKE_CYCLES = 4;

    private GameEnvironment gameEnvironment;
    private SoundEffectsService soundEffectsService;

    @FXML private ImageView recordImage;

    private final List<Image> recordStages = new ArrayList<>();
    private final Random random = new Random();
    private TranslateTransition shakeAnimation;

    private int clicksRequired;
    private int clicksSoFar;

    private static final int minimumClicks = 7;
    private static final int maximumClicks = 10;

    private Runnable onGatchaComplete;

    private String source;

    public GachaController(GameEnvironment gameEnvironment)
    {
        this(gameEnvironment, "");
    }

    public GachaController(GameEnvironment gameEnvironment, String source)
    {
        this.gameEnvironment = gameEnvironment;
        this.source = source;
        this.soundEffectsService = new SoundEffectsService(gameEnvironment);
    }

    @FXML
    public void initialize() {
        for (int i = 1; i <= gameEnvironment.getConfig().gachaStageCount; i++) {
            String suffix = source.equals("market") ? "Market" : "";
            recordStages.add(new Image(getClass().getResourceAsStream("/images/Gatcha/Stage" + i + suffix + ".png")));
        }
        recordImage.setOnMouseClicked(e -> onRecordClicked());
        shakeAnimation = createShakeAnimation();
        resetRecord();
    }

    public void setOnGachaComplete(Runnable onGachaComplete)
    {
        this.onGatchaComplete = onGachaComplete;
    }

    private void finishGacha()
    {
        if (onGatchaComplete != null)
        {
            onGatchaComplete.run();
        }
    }

    private void resetRecord() {
        clicksRequired = random.nextInt(gameEnvironment.getConfig().gachaMaxClicks - gameEnvironment.getConfig().gachaMinClicks + 1) + minimumClicks;
        clicksSoFar = 0;
        recordImage.setImage(recordStages.get(0));
        recordImage.setDisable(false);
    }

    private void onRecordClicked() {
        playShakeAnimation();
        // Plays open crate sound on each click //
        soundEffectsService.playOpenCrate();
        clicksSoFar++;

        int stageIndex = (int) Math.floor((double) clicksSoFar / clicksRequired * (recordStages.size() - 1));
        stageIndex = Math.min(stageIndex, recordStages.size() - 2);
        recordImage.setImage(recordStages.get(stageIndex));

        if (clicksSoFar >= clicksRequired) {
            System.out.println("gatcha opened");
            finishGacha();
        }
    }

    private TranslateTransition createShakeAnimation() {
        TranslateTransition animation = new TranslateTransition(Duration.millis(SHAKE_DURATION_MILLIS), recordImage);
        animation.setFromX(-SHAKE_DISTANCE);
        animation.setByX(SHAKE_DISTANCE * 2);
        animation.setCycleCount(SHAKE_CYCLES);
        animation.setAutoReverse(true);
        animation.setOnFinished(event -> recordImage.setTranslateX(0));
        return animation;
    }

    private void playShakeAnimation() {
        shakeAnimation.stop();
        recordImage.setTranslateX(0);
        shakeAnimation.playFromStart();
    }
}