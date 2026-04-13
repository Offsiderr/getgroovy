package seng201.team67;

import seng201.team67.gui.TestSceneManager;
import seng201.team67.models.Label;
import seng201.team67.models.enums.Difficulty;

public class GameEnviroment {

    //This is the main hub, this class wires everything together.

    //UI variables
    private TestSceneManager sceneManager;

    //Game variables
    private Label label;
    private int money;
    private int currentTour;
    private int totalTours;
    private Difficulty difficulty;

    //Still set to test until we switch to the main
    public TestSceneManager getSceneManager()
    {
        return sceneManager;
    }

}
