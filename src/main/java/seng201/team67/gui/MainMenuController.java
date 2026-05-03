package seng201.team67.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
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

    private Stage stage;
    private Scene scene;
    private Parent root;

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

        lineup = gameEnvironment.getLabelService().label.getLineUp();

        List<AnchorPane> slots = List.of(artistOne, artistTwo, artistThree);
        configureArtistPane(slots, lineup.size());

        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).getChildren().clear();
            if (i >= lineup.size()) {
                continue;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistCard.fxml"));
                ArtistCardController cardController = new ArtistCardController(gameEnvironment);
                loader.setController(cardController);
                slots.get(i).getChildren().add(loader.load());
                cardController.setArtist(lineup.get(i));
                artistCards.add(cardController);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load ArtistCard.fxml", e);
            }
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoseScreen.fxml"));
        loader.setController(new LoseScreenController(gameEnvironment));

        Parent loseScreenRoot = loader.load();
        stage = (Stage) labelName.getScene().getWindow();
        scene = new Scene(loseScreenRoot);
        stage.setScene(scene);
        stage.show();
    }

    //Mainmenu buttons

    @FXML public void startTour(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectTour.fxml"));
        loader.setController(new SelectTourController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void startRoster(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistRoster.fxml"));
        loader.setController(new RosterController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void startMarket()
    {
        soundEffectsService.playYes();

    }

    @FXML public void startStudio(ActionEvent event) throws IOException
    {
        soundEffectsService.playYes();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TheStudio.fxml"));
        loader.setController(new TheStudioController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
