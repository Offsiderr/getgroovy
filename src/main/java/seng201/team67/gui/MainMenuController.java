package seng201.team67.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import seng201.team67.GameEnvironment;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.gui.dev.DevFunctionsController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.Artist;
import seng201.team67.services.SoundEffectsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMenuController {

    private GameEnvironment gameEnvironment;
    private SoundEffectsService soundEffectsService = new SoundEffectsService();

    private List<Artist> lineup;

    @FXML private Label labelName;
    @FXML private Label gameDifficulty;
    @FXML private Label moneyText;
    @FXML private Label gameTours;
    @FXML private SplitPane artistPane;
    @FXML private AnchorPane artistOne;
    @FXML private AnchorPane artistTwo;
    @FXML private AnchorPane artistThree;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    //not needed currently, but here if needed in the future.
    private final List<ArtistCardController> artistCards = new ArrayList<>();


    public MainMenuController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize() throws IOException
    {

        if (gameEnvironment.checkGameStatus())
        {
            Platform.runLater(() -> //we have to run it later so that we don't need an action event to init the scene
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

        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        gameDifficulty.setText(gameEnvironment.getDifficulty().name());
        moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
        gameTours.setText(gameEnvironment.getTourCount() + "/" + gameEnvironment.getSelectedNumTours() + " Tours");


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

        lineup = gameEnvironment.getLabelService().label.getLineUp();

        List<AnchorPane> slots = List.of(artistOne, artistTwo, artistThree);
        configureArtistPane(slots, lineup.size());

        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).getChildren().clear();
            if (i >= lineup.size()) {
                continue;
            }
            ArtistCardController cardController = new ArtistCardController(gameEnvironment, null);
            viewLoader.loadInto(slots.get(i), "/fxml/ArtistCard.fxml", cardController);
            cardController.setArtist(lineup.get(i));
            artistCards.add(cardController);
        }
    }

    private void configureArtistPane(List<AnchorPane> slots, int lineupSize) {
        List<Node> visibleSlots = new ArrayList<>();
        int visibleCount = Math.max(1, Math.min(lineupSize, slots.size()));

        for (int i = 0; i < slots.size(); i++) {
            AnchorPane slot = slots.get(i);
            if (i < visibleCount) {
                slot.setVisible(true);
                slot.setManaged(true);
                visibleSlots.add(slot);
            } else {
                slot.setVisible(false);
                slot.setManaged(false);
            }
        }

        artistPane.getItems().setAll(visibleSlots);
        artistPane.setPrefWidth(visibleCount == 1 ? 320 : visibleCount == 2 ? 650 : 977);
        artistPane.setLayoutX((1280 - artistPane.getPrefWidth()) / 2);
    }

    private void showLoseScreen() throws IOException {
        screenNavigator.navigate(labelName, "/fxml/LoseScreen.fxml", new LoseScreenController(gameEnvironment));
    }

    //Mainmenu buttons

    @FXML public void startTour(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/SelectTour.fxml", new SelectTourController(gameEnvironment));
    }

    @FXML public void startRoster(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/ArtistRoster.fxml", new RosterController(gameEnvironment));
    }

    @FXML public void startMarket()
    {
        soundEffectsService.playYes();

    }

    @FXML public void startStudio(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();
        screenNavigator.navigate(event, "/fxml/TheStudio.fxml", new TheStudioController(gameEnvironment));
    }

    private void openDevMenu() throws IOException
    {
        screenNavigator.navigate(labelName, "/fxml/devui/DevFunctions.fxml", new DevFunctionsController(gameEnvironment));
    }
}
