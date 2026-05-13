package seng201.team67.gui.selection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team67.GameEnvironment;
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

    @FXML private VBox itemOne;
    @FXML private VBox itemTwo;
    @FXML private VBox itemThree;
    @FXML private StackPane gachaContainer;
    @FXML private AnchorPane root;
    @FXML private ImageView bg1;
    @FXML private ImageView bg2;
    @FXML private ImageView bg3;
    @FXML private Button selectArtists;

    private int hboxSize;

    private Rarity rarity;

    //this needs to be implemented in the super class later... hard coded for now.
    private int selectionSize;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();

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

        if (!artists) {
            Image marketBg = new Image(getClass().getResourceAsStream("/images/MarketGatchaBackground.png"));
            bg1.setImage(marketBg);
            bg2.setImage(marketBg);
            bg3.setImage(marketBg);
        }
    }

    private void showArtistCards() {
        gachaContainer.setVisible(false);

        List<VBox> slots = List.of(itemOne, itemTwo, itemThree);

        List<Artist> picked = gachaService.getPickedArtists(slots.size(), rarity);
        populateArtistCards(slots, picked);
    }

    private void showItemCards() {
        gachaContainer.setVisible(false);
        itemCards.clear();

        List<VBox> slots = List.of(itemOne, itemTwo, itemThree);
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
        if (artists) {
            long selectedCount = updateArtistSelectionAvailability(getMaxArtistSelections());
            selectArtists.setDisable(selectedCount != getMaxArtistSelections());
        } else {
            long selectedCount = itemCards.stream().filter(ItemCardController::isSelected).count();
            itemCards.forEach(card -> {
                if (!card.isSelected()) {
                    card.setSelectable(selectedCount < 1);
                }
            });
            selectArtists.setDisable(selectedCount != 1);
        }
    }

    @Override
    protected int getMaxArtistSelections() {
        return gameEnvironment.getConfig().maxGachaPicks;
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
