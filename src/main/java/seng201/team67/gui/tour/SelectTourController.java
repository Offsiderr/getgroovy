package seng201.team67.gui.tour;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.gameplay.TourService;

import java.io.IOException;
import java.util.List;

import static seng201.team67.models.enums.TourType.COUNTRY;
import static seng201.team67.models.enums.TourType.WORLD;

/**
 * Controls the select tour view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class SelectTourController {

    /** Shared game state for the current session. */
    private GameEnvironment gameEnvironment;

    /** Collection that stores the lineup. */
    private List<Artist> lineup;


    /** FXML reference for the artist pane control. */
    @FXML private SplitPane artistPane;
    /** FXML reference for the artist one control. */
    @FXML private VBox artistOne;
    /** FXML reference for the artist two control. */
    @FXML private VBox artistTwo;
    /** FXML reference for the artist three control. */
    @FXML private VBox artistThree;

    /** FXML reference for the country tour text control. */
    @FXML private Label countryTourText;
    /** FXML reference for the world tour text control. */
    @FXML private Label worldTourText;

    /** FXML reference for the country tour pane control. */
    @FXML private Pane countryTourPane;
    /** FXML reference for the world tour pane control. */
    @FXML private Pane worldTourPane;
    /** FXML reference for the bg1 control. */
    @FXML private ImageView bg1;
    /** FXML reference for the bg2 control. */
    @FXML private ImageView bg2;
    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();


    /**
     * Creates a new select tour controller.
     * @param gameEnvironment the active game environment
     */
    public SelectTourController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void initialize() throws IOException
    {
        lineup = gameEnvironment.getLabelService().getLineup();

        List<VBox> slots = List.of(artistOne, artistTwo, artistThree);
        configureArtistPane(slots, lineup.size());

        for (int i = 0; i < slots.size(); i++) {
            VBox slot = slots.get(i);
            if (i < lineup.size()) {
                slot.setDisable(false);
                ArtistDetailBoxFiller.populateArtistBox(slot, lineup.get(i), null);
            } else {
                clearArtistCard(slot);
            }
        }

        checkTours(countryTourText, countryTourPane, COUNTRY);
        checkTours(worldTourText, worldTourPane, WORLD);

        AnimationTimer bgTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameEnvironment.getConfig().movingBackgroundEnabled) {
                    bg1.setLayoutX(0);
                    bg2.setLayoutX(1280);
                    return;
                }

                double speed = 0.5;

                bg1.setLayoutX(bg1.getLayoutX() - speed);
                bg2.setLayoutX(bg2.getLayoutX() - speed);

                if (bg1.getLayoutX() <= -1280) {
                    bg1.setLayoutX(bg2.getLayoutX() + 1280);
                }

                if (bg2.getLayoutX() <= -1280) {
                    bg2.setLayoutX(bg1.getLayoutX() + 1280);
                }
            }
        };
        bgTimer.start();
    }

    private void configureArtistPane(List<VBox> slots, int lineupSize) {
        int visibleCount = Math.max(0, Math.min(lineupSize, slots.size()));
        artistPane.getItems().setAll(slots.subList(0, visibleCount).toArray(Node[]::new));

        if (visibleCount == 2) {
            artistPane.setDividerPositions(0.5);
        } else if (visibleCount >= 3) {
            artistPane.setDividerPositions(1.0 / 3.0, 2.0 / 3.0);
        }
    }

    private void clearArtistCard(VBox card) {
        card.getChildren().clear();
        card.setDisable(true);
        ArtistDetailBoxFiller.applyBaseStyle(card);
    }

    private void checkTours(Label label, Pane pane, TourType tour)
    {
        if(gameEnvironment.getTourCount() < tour.getExpeditionsUnlocked())
        {
            pane.setVisible(true);
            label.setText("Complete " + Integer.toString(tour.getExpeditionsUnlocked() - gameEnvironment.getTourCount()) +
                    " more tours to unlock the " + tour + " tour.");
        }
        else
        {
            pane.setVisible(false);
        }
    }


    /**
     * Starts the local tour.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void startLocalTour(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/tour/MainGame.fxml",
                new MainGameController(gameEnvironment, new TourService(new Tour(TourType.LOCAL), gameEnvironment)));
    }

    /**
     * Starts the country tour.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void startCountryTour(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/tour/MainGame.fxml",
                new MainGameController(gameEnvironment, new TourService(new Tour(COUNTRY), gameEnvironment)));
    }

    /**
     * Starts the world tour.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void startWorldTour(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/tour/MainGame.fxml",
                new MainGameController(gameEnvironment, new TourService(new Tour(TourType.WORLD), gameEnvironment)));
    }

    /**
     * Returns the player back to the main menu
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void cancelTour(ActionEvent event) throws IOException {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

}
