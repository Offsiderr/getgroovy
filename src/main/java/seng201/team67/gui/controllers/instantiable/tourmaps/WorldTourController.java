package seng201.team67.gui.controllers.instantiable.tourmaps;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.util.Duration;
import seng201.team67.services.TourService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorldTourController {

    //This controller handles the map checkpoints, visibility and checked state.
    //no decisions are made about checking or unchecking stops in this class.

    //TODO: this is awful, find a better way to handle this later
    @FXML
    private CheckBox stop1;
    @FXML
    private CheckBox stop2;
    @FXML
    private CheckBox stop3;
    @FXML
    private CheckBox stop4;
    @FXML
    private CheckBox stop5;
    @FXML
    private CheckBox stop6;
    @FXML
    private CheckBox stop7;
    @FXML
    private CheckBox stop8;
    @FXML
    private CheckBox stop9;
    @FXML
    private CheckBox stop10;
    @FXML
    private CheckBox stop11;
    @FXML
    private CheckBox stop12;
    @FXML
    private CheckBox stop13;
    @FXML
    private CheckBox stop14;
    @FXML
    private CheckBox stop15;
    @FXML
    private CheckBox stop16;
    @FXML
    private CheckBox stop17;
    @FXML
    private CheckBox stop18;
    @FXML
    private CheckBox stop19;
    @FXML
    private CheckBox stop20;
    @FXML
    private CheckBox stop21;
    @FXML
    private CheckBox stop22;
    @FXML
    private CheckBox stop23;
    @FXML
    private CheckBox stop24;
    @FXML
    private CheckBox stop25;
    @FXML
    private CheckBox stop26;

    private TourService tourService;

    //not sure what I am really doing with timelines
    //TODO: make improvements to the pulsing anim.
    private Timeline pulseTimeline;

    private ArrayList<CheckBox> allStops;

    private List<Integer> stopOrder;
    private Integer currentStop = 0;

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

    public void applyStopOrder(List<Integer> order)
    {
        this.stopOrder = new ArrayList<>(order);
    }

    public void applyRandomOrder()
    {
        stopOrder = IntStream.range(0, allStops.size()).boxed().collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(stopOrder);
    }

    public List<Integer> getStopOrder()
    {
        return stopOrder;
    }

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

    public int getCompletedCount()
    {
        //Don't need this really
        return (int) allStops.stream()
                .filter(CheckBox::isVisible)
                .filter(CheckBox::isSelected)
                .count();
    }

    //Animation stuff

    public void pulseCheckBox(int index) {
        stopPulse(); //cancel any existing pulse. this makes sure that only one checkbox can pulse at a time.

        CheckBox target = allStops.get(index);
        pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> target.setOpacity(1.0)),
                new KeyFrame(Duration.millis(500), e -> target.setOpacity(0.2)),
                new KeyFrame(Duration.millis(1000),e -> target.setOpacity(1.0))
        );
        pulseTimeline.setCycleCount(Timeline.INDEFINITE);
        pulseTimeline.play();
    }

    public void stopPulse() {
        if (pulseTimeline != null) {
            pulseTimeline.stop();
            pulseTimeline = null;
        }
        allStops.forEach(cb -> cb.setOpacity(1.0));
    }
}
