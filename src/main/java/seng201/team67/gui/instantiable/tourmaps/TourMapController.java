package seng201.team67.gui.instantiable.tourmaps;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.util.Duration;
import seng201.team67.services.gameplay.TourService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controls the tour map view and coordinates its user interactions.
 * No decisions are made about checking or unchecking stops in this class.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class TourMapController {

    /** The stop1. */
    @FXML
    private CheckBox stop1;
    /** The stop2. */
    @FXML
    private CheckBox stop2;
    /** The stop3. */
    @FXML
    private CheckBox stop3;
    /** The stop4. */
    @FXML
    private CheckBox stop4;
    /** The stop5. */
    @FXML
    private CheckBox stop5;
    /** The stop6. */
    @FXML
    private CheckBox stop6;
    /** The stop7. */
    @FXML
    private CheckBox stop7;
    /** The stop8. */
    @FXML
    private CheckBox stop8;
    /** The stop9. */
    @FXML
    private CheckBox stop9;
    /** The stop10. */
    @FXML
    private CheckBox stop10;
    /** The stop11. */
    @FXML
    private CheckBox stop11;
    /** The stop12. */
    @FXML
    private CheckBox stop12;
    /** The stop13. */
    @FXML
    private CheckBox stop13;
    /** The stop14. */
    @FXML
    private CheckBox stop14;
    /** The stop15. */
    @FXML
    private CheckBox stop15;
    /** The stop16. */
    @FXML
    private CheckBox stop16;
    /** The stop17. */
    @FXML
    private CheckBox stop17;
    /** The stop18. */
    @FXML
    private CheckBox stop18;
    /** The stop19. */
    @FXML
    private CheckBox stop19;
    /** The stop20. */
    @FXML
    private CheckBox stop20;
    /** The stop21. */
    @FXML
    private CheckBox stop21;
    /** The stop22. */
    @FXML
    private CheckBox stop22;
    /** The stop23. */
    @FXML
    private CheckBox stop23;
    /** The stop24. */
    @FXML
    private CheckBox stop24;
    /** The stop25. */
    @FXML
    private CheckBox stop25;
    /** The stop26. */
    @FXML
    private CheckBox stop26;

    /** Service used to manage tour behaviour. */
    private TourService tourService;

    /** The pulse timeline. */
    private Timeline pulseTimeline;

    /** Collection that stores the all stops. */
    private ArrayList<CheckBox> allStops;

    /** Collection that stores the stop order. */
    private List<Integer> stopOrder;
    /** Numeric value for the current stop. */
    private Integer currentStop = 0;

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     */
    @FXML
    public void initialize()
    {
        //Surely there is a better way of doing this
        allStops = new ArrayList<>(List.of(stop1, stop2, stop3, stop4, stop5, stop6, stop7,
                stop8, stop9, stop10, stop11, stop12, stop13,
                stop14, stop15, stop16, stop17, stop18, stop19,
                stop20, stop21, stop22, stop23, stop24, stop25, stop26));

        allStops.forEach(checkBox -> checkBox.setVisible(false));
        //stops the play from clicking them.
        allStops.forEach(checkBox -> checkBox.setMouseTransparent(true));

    }

    /**
     * Applies the stop order.
     * @param order the list of order
     */
    public void applyStopOrder(List<Integer> order)
    {
        this.stopOrder = new ArrayList<>(order);
    }

    /**
     * Applies the random order.
     */
    public void applyRandomOrder()
    {
        stopOrder = IntStream.range(0, allStops.size()).boxed().collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(stopOrder);
    }

    /**
     * Returns the stop order.
     * @return The stop order.
     */
    public List<Integer> getStopOrder()
    {
        return stopOrder;
    }

    /**
     * Processes the initialise stops.
     * It updates related state as needed while performing the operation.
     * @param count the numeric value for the count
     */
    public void initialiseStops(int count)
    {
        if(count < 1 || count > allStops.size())
        {
            throw new IllegalArgumentException(
                    "Stop count must be between 1 and " + allStops.size() + ", got: " + count);
        }

        for (int i = 0; i < allStops.size(); i++) {
            CheckBox cb = allStops.get(i);
            cb.setVisible(i < count);
            cb.setSelected(false);
        }
        pulseCheckBox(0);
    }

    /**
     * Processes the mark stop completed.
     * It updates related state as needed while performing the operation.
     * @param index the numeric value for the index
     */
    public void markStopCompleted(int index)
    {
        allStops.get(index).setSelected(true);
        currentStop = index + 1;

        long visibleCount = allStops.stream().filter(CheckBox::isVisible).count();
        if (currentStop < visibleCount) {
            pulseCheckBox(currentStop);
        } else {
            //all stops done
            stopPulse();
        }
    }

    /**
     * Returns the completed count.
     * @return The completed count.
     */
    public int getCompletedCount()
    {
        //Don't need this really
        return (int) allStops.stream()
                .filter(CheckBox::isVisible)
                .filter(CheckBox::isSelected)
                .count();
    }

    //Animation stuff

    /**
     * Processes the pulse check box.
     * It updates related state as needed while performing the operation.
     * @param index the numeric value for the index
     */
    public void pulseCheckBox(int index) {
        stopPulse(); //cancel any existing pulse. this makes sure that only one checkbox can pulse at a time.

        CheckBox target = allStops.get(index);
        pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> target.setOpacity(1.0)),
                new KeyFrame(Duration.millis(500), e -> target.setOpacity(0.2)),
                new KeyFrame(Duration.millis(1000),e -> target.setOpacity(1.0))
        );
        pulseTimeline.setCycleCount(Timeline.INDEFINITE);
        target.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null) {
                stopPulse();
            }
        });
        pulseTimeline.play();
    }

    /**
     * Processes the stop pulse.
     */
    public void stopPulse() {
        if (pulseTimeline != null) {
            pulseTimeline.stop();
            pulseTimeline = null;
        }
        allStops.forEach(cb -> cb.setOpacity(1.0));
    }
}
