package seng201.team67.gui.selection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.ArtistCardController;
import seng201.team67.gui.instantiable.GachaController;
import seng201.team67.gui.instantiable.ItemCardController;
import seng201.team67.gui.market.TheMarketController;
import seng201.team67.gui.studio.TheStudioController;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.items.Item;
import seng201.team67.services.setup.GachaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GachaSelectionController extends ArtistSelectionController {

    //This is quite similar to the starting artist selection. A decision was made not to make it modular due to the difficulty
    //in making the split panes adjustable.

    public final GameEnvironment gameEnvironment;
    private final GachaService gachaService;
    private boolean artists; //Yes if artists are being opened, otherwise it is items.

    @FXML private HBox itemOne;
    @FXML private HBox itemTwo;
    @FXML private HBox itemThree;
    @FXML private StackPane gachaContainer;
    @FXML private Button selectArtists;

    private int hboxSize;

    private Rarity rarity;

    //this needs to be implemented in the super class later... hard coded for now.
    private int selectionSize;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

    private final List<ArtistCardController> artistCards = new ArrayList<>();
    private final List<ItemCardController> itemCards = new ArrayList<>();

    public GachaSelectionController(GameEnvironment gameEnvironment, Boolean artists, int hboxSize, Rarity rarity) {
        super(gameEnvironment);
        this.gameEnvironment = gameEnvironment;
        this.artists = artists;
        this.hboxSize = hboxSize;
        this.rarity = rarity;
        gachaService = new GachaService(gameEnvironment);
    }

    @FXML
    public void initialize() throws IOException {
        selectArtists.setDisable(true);
        if (artists)
        {
            selectArtists.setText("Select Arist");
        }
        else
        {
            selectArtists.setText("Select Item");
        }

        String source = artists ? "studio" : "market";
        GachaController gachaController = new GachaController(gameEnvironment, source);
        gachaController.setOnGachaComplete(() -> {if(artists){showArtistCards();}else{showItemCards();}});
        viewLoader.loadInto(gachaContainer, "/fxml/components/Gatcha.fxml", gachaController);
    }

    private void showArtistCards() {
        gachaContainer.setVisible(false);
        artistCards.clear();

        List<HBox> slots = List.of(itemOne, itemTwo, itemThree);

        List<Artist> picked = gachaService.getPickedArtists(slots.size(), rarity);


        for (int i = 0; i < picked.size(); i++) {
            ArtistCardController cardController = new ArtistCardController(gameEnvironment, null);
            viewLoader.loadInto(slots.get(i), "/fxml/components/ArtistCard.fxml", cardController);
            cardController.setArtist(picked.get(i));
            cardController.setSelectionController(this);
            artistCards.add(cardController);
        }
    }

    private void showItemCards() {
        gachaContainer.setVisible(false);
        itemCards.clear();

        List<HBox> slots = List.of(itemOne, itemTwo, itemThree);
        List<Item> picked = gachaService.getPickedItems(slots.size(), rarity);

        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).getChildren().clear();
            if (i >= picked.size()) {
                continue;
            }

            ItemCardController cardController = new ItemCardController(gameEnvironment, null);
            viewLoader.loadInto(slots.get(i), "/fxml/components/ItemCard.fxml", cardController);
            cardController.setItem(picked.get(i));
            cardController.setSelectionHandler(this::onSelectionChanged);
            itemCards.add(cardController);
        }
    }

    public void onSelectionChanged() {
        long selectedCount = artists
                ? artistCards.stream().filter(ArtistCardController::isSelected).count()
                : itemCards.stream().filter(ItemCardController::isSelected).count();

        if (artists) {
            artistCards.forEach(card -> {
                if (!card.isSelected()) {
                    card.setSelectable(selectedCount < 1);
                }
            });
        } else {
            itemCards.forEach(card -> {
                if (!card.isSelected()) {
                    card.setSelectable(selectedCount < 1);
                }
            });
        }

        selectArtists.setDisable(selectedCount != 1);
    }

    public List<Artist> getSelectedArtists() {
        return artistCards.stream()
                .filter(ArtistCardController::isSelected)
                .map(c -> c.artist)
                .toList();
    }

    public List<Item> getSelectedItems() {
        return itemCards.stream()
                .filter(ItemCardController::isSelected)
                .map(c -> c.item)
                .toList();
    }

    @FXML
    public void artistsSelected(ActionEvent event) throws IOException
    {
        if (artists) {
            gameEnvironment.getLabelService().hireArtist(getSelectedArtists().get(0), 0);
        } else {
            gameEnvironment.getLabelService().buyItem(getSelectedItems().get(0), 0);
        }
        moveScene(event);
    }

    @FXML
    private void moveScene(ActionEvent event) throws IOException
    {
        if (artists) {
            screenNavigator.navigate(event, "/fxml/studio/TheStudio.fxml", new TheStudioController(gameEnvironment));
        } else {
            screenNavigator.navigate(event, "/fxml/market/TheMarket.fxml", new TheMarketController(gameEnvironment));
        }
    }
}
