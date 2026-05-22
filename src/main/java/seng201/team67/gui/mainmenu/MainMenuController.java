package seng201.team67.gui.mainmenu;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import seng201.team67.gui.dev.DevFunctionsController;
import seng201.team67.gui.market.TheMarketController;
import seng201.team67.gui.results.LoseScreenController;
import seng201.team67.gui.results.WinScreenController;
import seng201.team67.gui.studio.TheStudioController;
import seng201.team67.gui.tour.SelectTourController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;
import seng201.team67.services.audio.SoundEffectsService;
import seng201.team67.services.gameplay.GameStatusService;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;

/**
 * Controls the main menu view and coordinates its user interactions.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class MainMenuController {

    /** Shared game state for the current session. */
    private final GameEnvironment gameEnvironment;
    /** Service used to manage sound effects behaviour. */
    private final SoundEffectsService soundEffectsService;
    /** Service used to manage game status behaviour. */
    private final GameStatusService gameStatusService;

    /** Collection that stores the lineup. */
    private List<Artist> lineup;

    /** FXML reference for the label name control. */
    @FXML private Label labelName;
    /** FXML reference for the game difficulty control. */
    @FXML private Label gameDifficulty;
    /** FXML reference for the money text control. */
    @FXML private Label moneyText;
    /** FXML reference for the game tours control. */
    @FXML private Label gameTours;
    /** FXML reference for the score label control. */
    @FXML private Label scoreLabel;
    /** FXML reference for the artist pane control. */
    @FXML private SplitPane artistPane;
    /** FXML reference for the artist one control. */
    @FXML private VBox artistOne;
    /** FXML reference for the artist two control. */
    @FXML private VBox artistTwo;
    /** FXML reference for the artist three control. */
    @FXML private VBox artistThree;
    /** FXML reference for the settings holder control. */
    @FXML private AnchorPane settingsHolder;
    /** FXML reference for the bg1 control. */
    @FXML private ImageView bg1;
    /** FXML reference for the bg2 control. */
    @FXML private ImageView bg2;

    /** The screen navigator. */
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    /** The view loader. */
    private final ViewLoader viewLoader = new ViewLoader();

    /**
     * Creates a new main menu controller.
     * @param gameEnvironment the active game environment
     */
    public MainMenuController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
        this.soundEffectsService = new SoundEffectsService(gameEnvironment);
        this.gameStatusService = new GameStatusService();
    }

    /**
     * Initializes the controller state and populates the initial view data.
     * It also attaches any required event handlers for the screen.
     * @throws IOException if an input or output error occurs
     */
    @FXML
    public void initialize() throws IOException
    {

        if (gameStatusService.isGameLost(gameEnvironment))
        {
            Platform.runLater(() ->
            {
                try {
                    showLoseScreen();
                } catch (IOException e)
                {
                    throw new RuntimeException("Failed to load LoseScreen.fxml", e);
                }
            });
            return;
        }

        if (gameEnvironment.getTourCount() >= gameEnvironment.getSelectedNumTours())
        {
            Platform.runLater(() ->{
                screenNavigator.navigate(labelName, "/fxml/results/WinScreen.fxml", new WinScreenController(gameEnvironment));
            });
        }

        //Set the labels at the top of the screen
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        gameDifficulty.setText(gameEnvironment.getDifficulty().getDisplayName());
        moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
        gameTours.setText(gameEnvironment.getTourCount() + "/" + gameEnvironment.getSelectedNumTours() + " Tours");
        scoreLabel.setText(Integer.toString(gameEnvironment.getGameScore()));

        //Set up a runnable to check for control D, which opens the dev menu
        Platform.runLater(() -> {
            Scene scene = labelName.getScene();
            if (scene != null) {
                scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.isControlDown() && event.getCode() == KeyCode.D) {
                        try {
                            openDevMenu();
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to load DevFunctions.fxml", e);
                        }
                        event.consume();
                    }
                });
            }
        });

        //Set the artist detail boxes up
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

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (bg1.getScene() == null) {
                    stop();
                    return;
                }

                if (!gameEnvironment.getConfig().movingBackgroundEnabled) {
                    bg1.setLayoutX(0);
                    bg2.setLayoutX(bg1.getFitWidth());
                    return;
                }

                bg1.setLayoutX(bg1.getLayoutX() - 0.4);
                bg2.setLayoutX(bg2.getLayoutX() - 0.4);

                if (bg1.getLayoutX() + bg1.getFitWidth() <= 0) {
                    bg1.setLayoutX(bg2.getLayoutX() + bg2.getFitWidth());
                }

                if (bg2.getLayoutX() + bg2.getFitWidth() <= 0) {
                    bg2.setLayoutX(bg1.getLayoutX() + bg1.getFitWidth());
                }
            }
        };
        timer.start();
    }

    /**
     * Adjusts the artist detail boxes pane to account for the amount of artists in the lineup
     */
    private void configureArtistPane(List<VBox> slots, int artistCount) {
        int visibleCount = Math.max(0, Math.min(artistCount, slots.size()));
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

    private void showLoseScreen() throws IOException {
        screenNavigator.navigate(labelName, "/fxml/results/LoseScreen.fxml", new LoseScreenController(gameEnvironment));
    }

    /**
     * Opens the tour selection menu.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void startTour(ActionEvent event) throws IOException
    {
        gameEnvironment.getMusicService().stopAndReset();
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/tour/SelectTour.fxml", new SelectTourController(gameEnvironment));
    }

    /**
     * Opens the roster menu
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void startRoster(ActionEvent event) throws IOException
    {
        gameEnvironment.getMusicService().stopAndReset();
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/mainmenu/ArtistRoster.fxml", new RosterController(gameEnvironment));
    }

    /**
     * Opens the market
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void startMarket(ActionEvent event) throws IOException
    {
        gameEnvironment.getMusicService().stopAndReset();
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/market/TheMarket.fxml", new TheMarketController(gameEnvironment));
    }

    /**
     * Opens the studio
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void startStudio(ActionEvent event) throws IOException
    {
        gameEnvironment.getMusicService().stopAndReset();
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/studio/TheStudio.fxml", new TheStudioController(gameEnvironment));
    }

    private void openDevMenu() throws IOException
    {
        screenNavigator.navigate(labelName, "/fxml/devui/DevFunctions.fxml", new DevFunctionsController(gameEnvironment));
    }

    /**
     * Opens the settings menu.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void openSettings(ActionEvent event) throws IOException
    {
        settingsHolder.setDisable(false);
        settingsHolder.setVisible(true);
        settingsHolder.setManaged(true);
        viewLoader.loadInto(settingsHolder, "/fxml/mainmenu/MainSettings.fxml",
                new MainSettingsController(gameEnvironment, settingsHolder));
    }

    /**
     * Opens the help menu.
     * @param event the action event that triggered the request
     * @throws IOException if an input or output error occurs
     */
    @FXML public void openHelp(ActionEvent event) throws IOException
    {
        settingsHolder.setDisable(false);
        settingsHolder.setVisible(true);
        settingsHolder.setManaged(true);
        viewLoader.loadInto(settingsHolder, "/fxml/mainmenu/MainHelp.fxml", new MainHelpController(settingsHolder));
    }
}
