package seng201.team67.gui.controllers.instantiable;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GachaController {

    @FXML private ImageView recordImage;

    private final List<Image> recordStages = new ArrayList<>();
    private final Random random = new Random();

    private int clicksRequired;
    private int clicksSoFar;

    private static final int minimumClicks = 7;
    private static final int maximumClicks = 10;

    private Runnable onGatchaComplete;

    @FXML
    public void initialize() {
        for (int i = 1; i <= 4; i++) {
            recordStages.add(new Image(getClass().getResourceAsStream("/images/Gatcha/Stage" + i + ".png")));
        }
        recordImage.setOnMouseClicked(e -> onRecordClicked());
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

    //used for testing. won't reset obivously in the game
    private void resetRecord() {
        clicksRequired = random.nextInt(maximumClicks - minimumClicks + 1) + minimumClicks;
        clicksSoFar = 0;
        recordImage.setImage(recordStages.get(0));
        recordImage.setDisable(false);
    }

    private void onRecordClicked() {
        clicksSoFar++;

        int stageIndex = (int) Math.floor((double) clicksSoFar / clicksRequired * (recordStages.size() - 1));
        stageIndex = Math.min(stageIndex, recordStages.size() - 2);
        recordImage.setImage(recordStages.get(stageIndex));

        if (clicksSoFar >= clicksRequired) {
            System.out.println("gatcha opened");
            finishGacha();
            //resetRecord();
        }
    }
}