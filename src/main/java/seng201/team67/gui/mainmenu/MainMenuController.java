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

public class MainMenuController {

    private final GameEnvironment gameEnvironment;
    private final SoundEffectsService soundEffectsService;
    private final GameStatusService gameStatusService;

    private List<Artist> lineup;

    @FXML private Label labelName;
    @FXML private Label gameDifficulty;
    @FXML private Label moneyText;
    @FXML private Label gameTours;
    @FXML private Label scoreLabel;
    @FXML private SplitPane artistPane;
    @FXML private VBox artistOne;
    @FXML private VBox artistTwo;
    @FXML private VBox artistThree;
    @FXML private AnchorPane settingsHolder;
    @FXML private ImageView bg1;
    @FXML private ImageView bg2;

    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    public MainMenuController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
        this.soundEffectsService = new SoundEffectsService(gameEnvironment);
        this.gameStatusService = new GameStatusService();
    }

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

        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        gameDifficulty.setText(gameEnvironment.getDifficulty().name());
        moneyText.setText(String.format("$%.2f", gameEnvironment.getLabelService().getMoney()));
        gameTours.setText(gameEnvironment.getTourCount() + "/" + gameEnvironment.getSelectedNumTours() + " Tours");
        scoreLabel.setText(Integer.toString(gameEnvironment.getGameScore()));

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

    @FXML public void startTour(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/tour/SelectTour.fxml", new SelectTourController(gameEnvironment));
    }

    @FXML public void startRoster(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/mainmenu/ArtistRoster.fxml", new RosterController(gameEnvironment));
    }

    @FXML public void startMarket(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/market/TheMarket.fxml", new TheMarketController(gameEnvironment));
    }

    @FXML public void startStudio(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/studio/TheStudio.fxml", new TheStudioController(gameEnvironment));
    }

    private void openDevMenu() throws IOException
    {
        screenNavigator.navigate(labelName, "/fxml/devui/DevFunctions.fxml", new DevFunctionsController(gameEnvironment));
    }

    @FXML public void openSettings(ActionEvent event) throws IOException
    {
        settingsHolder.setDisable(false);
        settingsHolder.setVisible(true);
        settingsHolder.setManaged(true);
        viewLoader.loadInto(settingsHolder, "/fxml/mainmenu/MainSettings.fxml",
                new MainSettingsController(gameEnvironment, settingsHolder));
    }

    @FXML public void openHelp(ActionEvent event) throws IOException
    {
        settingsHolder.setDisable(false);
        settingsHolder.setVisible(true);
        settingsHolder.setManaged(true);
        viewLoader.loadInto(settingsHolder, "/fxml/mainmenu/MainHelp.fxml", new MainHelpController(settingsHolder));
    }
}
