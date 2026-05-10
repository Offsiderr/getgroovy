package seng201.team67.gui.tour;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import seng201.team67.GameEnvironment;
import seng201.team67.gui.instantiable.tourmaps.TourMapController;
import seng201.team67.gui.mainmenu.MainMenuController;
import seng201.team67.gui.results.TourResultsController;
import seng201.team67.gui.util.ArtistDetailBoxFiller;
import seng201.team67.gui.util.ScreenNavigator;
import seng201.team67.gui.util.ViewLoader;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.TourType;
import seng201.team67.models.items.CosumableItem;
import seng201.team67.models.items.Item;
import seng201.team67.services.audio.SoundEffectsService;
import seng201.team67.services.gameplay.TourService;

import java.io.IOException;
import java.util.List;

public class MainGameController {

    //TODO: implement different maps

    private GameEnvironment gameEnvironment;
    private TourService tourService;
    private SoundEffectsService sfx;

    private Boolean initStops = false;

    @FXML private Label labelName;
    @FXML private Label moneyText;
    @FXML private Label expeditionCount;
    @FXML private Label payText;
    @FXML private Label cancelTourLabel;
    @FXML private Label staminaText;
    @FXML private Label effectText;

    @FXML private ProgressBar tourProgressBar;

    @FXML private VBox artistCardOne;
    @FXML private VBox artistCardTwo;
    @FXML private VBox artistCardThree;

    @FXML private Button startConcertButton;
    @FXML private Button endTourEarlyButton;

    @FXML private AnchorPane mapAnchorPane;
    @FXML private AnchorPane cancelTourPane;
    private final ScreenNavigator screenNavigator = new ScreenNavigator();
    private final ViewLoader viewLoader = new ViewLoader();


    public MainGameController(GameEnvironment gameEnvironment, TourService tourService)
    {
        this.gameEnvironment = gameEnvironment;
        this.tourService = tourService;
    }

    @FXML public void initialize() throws IOException {
        cancelTourPane.setVisible(false);
        labelName.setText(gameEnvironment.getLabelService().getLabelName());
        moneyText.setText(Double.toString(gameEnvironment.getLabelService().getMoney()));
        payText.setText(Double.toString(tourService.getCreditsEarned()));

        staminaText.setText(Integer.toString((int) Math.round(tourService.getTotalStamina())));
        effectText.setText(tourService.getConditionalEffectText());
        effectText.setVisible(!tourService.getConditionalEffectText().isBlank());

        loadLineup();

        if(tourService.getConcertStatus())
        {
            initStops = true;
            concertFinished();
        }

        loadMap(tourService.getTourType());

        refreshTourActionButtons();

        tourProgressBar.setProgress((double) tourService.getStopIndex() / tourService.getTourType().getStops());
    }

    private void refreshTourActionButtons()
    {
        boolean isTourComplete = tourService.isTourComplete();
        startConcertButton.setText(isTourComplete ? "Finish Tour" : "Start Concert");
        endTourEarlyButton.setDisable(isTourComplete);
    }

    private void loadLineup()
    {
        List<Artist> pool = gameEnvironment.getLabelService().getLineup();
        VBox[] cards = { artistCardOne, artistCardTwo, artistCardThree };

        for (int i = 0; i < cards.length; i++) {
            if (i < pool.size()) {
                cards[i].setDisable(false);
                Artist artist = pool.get(i);
                ArtistDetailBoxFiller.populateArtistBox(cards[i], artist, null, item -> handleItemUse(artist, item));
            } else {
                clearArtistCard(cards[i]);
            }
        }
    }

    private void handleItemUse(Artist artist, Item item)
    {
        if (!(item instanceof CosumableItem))
        {
            return;
        }

        String result = gameEnvironment.getLabelService().useConsumable(artist, item);
        if (result.isBlank())
        {
            return;
        }

        effectText.setText(result);
        effectText.setVisible(true);
        loadLineup();
    }

    private void clearArtistCard(VBox card) {
        card.getChildren().clear();
        card.setDisable(true);
        ArtistDetailBoxFiller.applyBaseStyle(card);
    }

    private void loadMap(TourType type) throws IOException {
        //TODO: make the world tour controller a class variable instead of local.
        String path = "";
        //TODO: something weird is up with this switch statement. investigate.
        switch (type)
        {
            case LOCAL:
                path = "/fxml/maps/regionMap.fxml";
                break;
            case COUNTRY:
                path = "/fxml/maps/countryMap.fxml";
                break;
            case WORLD:
                path = "/fxml/maps/worldMap.fxml";
        }

        TourMapController worldMapController = new TourMapController();
        viewLoader.loadInto(mapAnchorPane, path, worldMapController);

        TourMapController mapController = worldMapController;

        if (tourService.hasStopOrder()) {
            mapController.applyStopOrder(tourService.getStopOrder());
        } else {
            mapController.applyRandomOrder();
            tourService.setStopOrder(mapController.getStopOrder());
        }

        mapController.initialiseStops(tourService.getTourType().getStops());

        for (int i = 0; i < tourService.getStopIndex(); i++) {
            mapController.markStopCompleted(i);
        }

    }

    public void endTourEarly()
    {

    }

    @FXML public void startConcert(ActionEvent event) throws IOException
    {
        //sfx.playYes(); TODO: wire this up.

        if(tourService.isTourComplete())
        {
            finishTour(event);
            return;
        }

        screenNavigator.navigate(event, "/fxml/tour/MainConcert.fxml", new MainConcertController(gameEnvironment, tourService));
    }

    private boolean concertFinished()
    {
        //Returns false if the tour is finished
        return true;
    }

    private void returnToMainMenu(ActionEvent event) throws IOException
    {
        screenNavigator.navigate(event, "/fxml/mainmenu/MainMenu.fxml", new MainMenuController(gameEnvironment));
    }

    @FXML private void endTourEarly(ActionEvent event) throws IOException
    {
        if (tourService.isTourComplete()) {
            return;
        }

        cancelTourPane.setVisible(true);
        cancelTourLabel.setText("Are you sure you want to end the tour early? You will have to refund $" + gameEnvironment.getConfig().cancelTourPenalty + " of tickets");
    }

    @FXML private void notCancellingTour(ActionEvent event) throws IOException
    {
    }


     @FXML private void finishTour(ActionEvent event) throws IOException {
         if (cancelTourPane.isVisible() && !tourService.isTourComplete()) {
             tourService.addCreditsEarned((double) -gameEnvironment.getConfig().cancelTourPenalty);
         }

         tourService.tourEnded();
         gameEnvironment.setArtistPoolGenerated(false);
         if (tourService.isEndedByExhaustion())
         {
             screenNavigator.navigate(event, "/fxml/results/TourResults.fxml",
                     new TourResultsController(gameEnvironment, tourService, true));
         }
         else
         {
             screenNavigator.navigate(event, "/fxml/results/TourResults.fxml",
                     new TourResultsController(gameEnvironment, tourService, false));
         }
    }

    @FXML private void closePane(ActionEvent event) throws IOException
    {
        cancelTourPane.setVisible(false);
    }
}
