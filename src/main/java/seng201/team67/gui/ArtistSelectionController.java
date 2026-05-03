package seng201.team67.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.controllers.instantiable.ArtistCardController;
import seng201.team67.gui.controllers.instantiable.GachaController;
import seng201.team67.models.Artist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtistSelectionController {

    //This class stores the artist selection for the start of the game,
    //but will be split out into a basic selection interface super class
    //that other classes can inherit from in the future.

    public final GameEnvironment gameEnvironment;

    @FXML private HBox artistOne;
    @FXML private HBox artistTwo;
    @FXML private HBox artistThree;
    @FXML private HBox artistFour;
    @FXML private HBox artistFive;
    @FXML private StackPane gachaContainer;
    @FXML private Button selectArtists;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final List<ArtistCardController> artistCards = new ArrayList<>();

    public ArtistSelectionController(GameEnvironment gameEnvironment) {
        this.gameEnvironment = gameEnvironment;
    }

    @FXML
    public void initialize() throws IOException {
        selectArtists.setDisable(true);

        FXMLLoader gachaLoader = new FXMLLoader(getClass().getResource("/fxml/Gatcha.fxml"));
        GachaController gachaController = new GachaController(gameEnvironment);
        gachaLoader.setController(gachaController);

        gachaController.setOnGachaComplete(() -> showArtistCards());

        VBox gacha = gachaLoader.load();
        gachaContainer.getChildren().add(gacha);
    }

    private void showArtistCards() {
        gachaContainer.setVisible(false);

        List<Artist> pool = gameEnvironment.getArtistPool();
        Collections.shuffle(pool);

        List<Artist> picked = new ArrayList<>();
        //pick artists
        for(int i = 0; i < gameEnvironment.getConfig().startingArtistPoolSize; i++)
        {
            int z = i;
            while (pool.get(z).getStarPower() > gameEnvironment.getConfig().maxSPInStartingSelection || picked.contains(pool.get(z)))
            {
                z += 1;
            }
            picked.add(pool.get(z));
        }

        List<HBox> slots = List.of(artistOne, artistTwo, artistThree, artistFour, artistFive);

        for (int i = 0; i < slots.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArtistCard.fxml"));
                ArtistCardController cardController = new ArtistCardController(gameEnvironment);
                loader.setController(cardController);
                slots.get(i).getChildren().add(loader.load());
                cardController.setArtist(picked.get(i));
                cardController.setSelectionController(this);
                artistCards.add(cardController);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load ArtistCard.fxml", e);
            }
        }
    }

    public void onSelectionChanged() {
        long selectedCount = artistCards.stream()
                .filter(ArtistCardController::isSelected)
                .count();

        artistCards.forEach(c -> {
            if (!c.isSelected()) {
                c.setSelectable(selectedCount < gameEnvironment.getConfig().maxStartingArtists);
            }
        });

        selectArtists.setDisable(selectedCount != gameEnvironment.getConfig().maxStartingArtists);
    }

    public List<Artist> getSelectedArtists() {
        return artistCards.stream()
                .filter(ArtistCardController::isSelected)
                .map(c -> c.artist)
                .toList();
    }

    @FXML
    public void artistsSelected(ActionEvent event) throws IOException
    {
        gameEnvironment.createLabel(getSelectedArtists());
        gameEnvironment.getLabelService().getAllArtists().forEach(artist -> {
            artist.owned = true;
        });

        moveScene(event);
    }

    @FXML
    private void moveScene(ActionEvent event) throws IOException
    {
        //Now let's load the artist selection scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        loader.setController(new MainMenuController(gameEnvironment));

        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}