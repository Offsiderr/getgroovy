package seng201.team67.gui.instantiable;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import seng201.team67.GameEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controls the gacha view and coordinates its user interactions. Works with the GachaService to provide the gacha
 * functionality
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class GachaController {

    /** Numeric value for the shake distance. */
    private static final double SHAKE_DISTANCE = 8.0;
    /** Numeric value for the shake duration millis. */
    private static final double SHAKE_DURATION_MILLIS = 45.0;
    /** Numeric value for the shake cycles. */
    private static final int SHAKE_CYCLES = 4;

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;

    /** FXML reference for the record image control. */
    @FXML private ImageView recordImage;

    /** Collection that stores the record stages. */
    private final List<Image> recordStages = new ArrayList<>();
    /** The random. */
    private final Random random = new Random();
    /** The shake animation. */
    private TranslateTransition shakeAnimation;

    /** Numeric value for the clicks required. */
    private int clicksRequired;
    /** Numeric value for the clicks so far. */
    private int clicksSoFar;

    /** Numeric value for the minimum clicks. */
    private static final int minimumClicks = 7;
    /** Numeric value for the maximum clicks. */
    private static final int maximumClicks = 10;

    /** The on gatcha complete. */
    private Runnable onGatchaComplete;

    /** Text value for the source. */
    private String source;

    /**
     * Creates a new gacha controller.
     * @param gameEnvironment the active game environment
     */
    public GachaController(GameEnvironment gameEnvironment)
    {
        this(gameEnvironment, "");
    }

    /**
     * Creates a new gacha controller.
     * @param gameEnvironment the active game environment
     * @param source the text value for the source
     */
    public GachaController(GameEnvironment gameEnvironment, String source)
    {
        this.gameEnvironment = gameEnvironment;
        this.source = source;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize() {
        for (int i = 1; i <= gameEnvironment.getConfig().gachaStageCount; i++) {
            String suffix = source.equals("market") ? "Market" : "";
            recordStages.add(new Image(getClass().getResourceAsStream("/images/Gatcha/Stage" + i + suffix + ".png")));
        }
        recordImage.setOnMouseClicked(e -> onGachaClicked());
        shakeAnimation = createShakeAnimation();
        resetGacha();
    }

    /**
     * Sets the on gacha complete.
     * @param onGachaComplete the on gacha complete
     */
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

    /**
     * This method resets the Gatcha. Was originally in here for testing, now is just used for ensuring the gacha is reset at initialisation.
     */
    private void resetGacha() {
        clicksRequired = random.nextInt(gameEnvironment.getConfig().gachaMaxClicks - gameEnvironment.getConfig().gachaMinClicks + 1) + minimumClicks;
        clicksSoFar = 0;
        recordImage.setImage(recordStages.get(0));
        recordImage.setDisable(false);
    }

    /**
        Handles when the gacha image is clicked in the game. Plays the shake animation, and works out both what stage
        the gacha is at, as well as if it is opened.
     */
    private void onGachaClicked() {
        playShakeAnimation();
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
