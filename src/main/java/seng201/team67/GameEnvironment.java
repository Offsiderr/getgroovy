package seng201.team67;

import seng201.team67.models.GameConfig;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.models.enums.PayoutTier;
import seng201.team67.models.items.Item;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.audio.MusicService;
import seng201.team67.services.data.SkillLoaderService;
import seng201.team67.services.management.LabelService;
import seng201.team67.services.setup.GameInitialisationService;
import seng201.team67.services.setup.SetupService;

import java.util.ArrayList;
import java.util.List;

public class GameEnvironment {

    private Label label;
    private String tempName;

    private int currentTour;
    private int selectedNumTours;
    private int tourCount = 0;
    private int concertCount = 0;
    private Difficulty difficulty;
    private int gameScore = 0;

    private final MusicService musicService;

    private ArrayList<Artist> artistPool = new ArrayList<>();
    private ArrayList<Artist> artistPurchasePool = new ArrayList<>();
    private boolean artistPoolGenerated = false;

    private ArrayList<Item> itemPurchasePool = new ArrayList<>();
    private boolean itemPoolGenerated = false;

    private GameConfig gameConfig;
    private PayoutTier payoutTier;

    private ArrayList<Question> commonQuestionPool = new ArrayList<>();
    private ArrayList<Question> localQuestionPool = new ArrayList<>();
    private ArrayList<Question> countryQuestionPool = new ArrayList<>();
    private ArrayList<Question> worldQuestionPool = new ArrayList<>();

    private ArrayList<Item> allItems = new ArrayList<>();

    private SkillLoaderService skillLoaderService;

    public GameEnvironment()
    {
        this.musicService = new MusicService(this);
        this.skillLoaderService = new SkillLoaderService();
        new GameInitialisationService().initialise(this);
    }

    public void setLabelName(String name)
    {
        tempName = new SetupService(this).requireValidLabelName(name);
    }

    public String getTempName()
    {
        return tempName;
    }

    public void setDifficulty(Difficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty()
    {
        return difficulty;
    }

    public void setSelectedNumTours(int selectedNumTours)
    {
        this.selectedNumTours = selectedNumTours;
    }

    public int getSelectedNumTours()
    {
        return selectedNumTours;
    }

    public void setArtistPool(List<Artist> artistList)
    {
        artistPool.clear();
        artistPool.addAll(artistList);
    }

    public ArrayList<Artist> getArtistPool()
    {
        return artistPool;
    }

    public Label getLabel()
    {
        return label;
    }

    public void setLabel(Label label)
    {
        this.label = label;
    }

    public LabelService getLabelService()
    {
        return new LabelService(this);
    }

    public MusicService getMusicService()
    {
        return musicService;
    }

    public int getTourCount()
    {
        return tourCount;
    }

    public void increaseTours()
    {
        tourCount += 1;
    }

    public int getConcertCount()
    {
        return concertCount;
    }

    public void increaseConcertCount()
    {
        concertCount += 1;
    }

    public ArrayList<Artist> getArtistPurchasePool()
    {
        return artistPurchasePool;
    }

    public void setArtistPurchasePool(List<Artist> artistPurchasePool)
    {
        this.artistPurchasePool.clear();
        this.artistPurchasePool.addAll(artistPurchasePool);
    }

    public void removeArtistFromPurchasePool(Artist artist)
    {
        artistPurchasePool.remove(artist);
    }

    public boolean isArtistPoolGenerated()
    {
        return artistPoolGenerated;
    }

    public void setArtistPoolGenerated(boolean artistPoolGenerated)
    {
        this.artistPoolGenerated = artistPoolGenerated;
    }

    public ArrayList<Item> getItemPurchasePool()
    {
        return itemPurchasePool;
    }

    public void setItemPurchasePool(List<Item> itemPurchasePool)
    {
        this.itemPurchasePool.clear();
        this.itemPurchasePool.addAll(itemPurchasePool);
    }

    public void removeItemFromPurchasePool(Item item)
    {
        itemPurchasePool.remove(item);
    }

    public boolean isItemPoolGenerated()
    {
        return itemPoolGenerated;
    }

    public void setItemPoolGenerated(boolean itemPoolGenerated)
    {
        this.itemPoolGenerated = itemPoolGenerated;
    }

    public GameConfig getConfig()
    {
        if(gameConfig == null)
        {
            return GameConfig.easy();
        }
        return gameConfig;
    }

    public void setGameConfig(GameConfig gameConfig)
    {
        this.gameConfig = gameConfig;
    }

    public PayoutTier getPayoutTier()
    {
        return payoutTier;
    }

    public void setPayoutTier(PayoutTier payoutTier)
    {
        this.payoutTier = payoutTier;
    }

    public ArrayList<Question> getCommonQuestionPool()
    {
        return commonQuestionPool;
    }

    public void setCommonQuestionPool(List<Question> commonQuestionPool)
    {
        this.commonQuestionPool = new ArrayList<>(commonQuestionPool);
    }

    public ArrayList<Question> getLocalQuestionPool()
    {
        return localQuestionPool;
    }

    public void setLocalQuestionPool(List<Question> localQuestionPool)
    {
        this.localQuestionPool = new ArrayList<>(localQuestionPool);
    }

    public ArrayList<Question> getCountryQuestionPool()
    {
        return countryQuestionPool;
    }

    public void setCountryQuestionPool(List<Question> countryQuestionPool)
    {
        this.countryQuestionPool = new ArrayList<>(countryQuestionPool);
    }

    public ArrayList<Question> getWorldQuestionPool()
    {
        return worldQuestionPool;
    }

    public void setWorldQuestionPool(List<Question> worldQuestionPool)
    {
        this.worldQuestionPool = new ArrayList<>(worldQuestionPool);
    }

    public ArrayList<Item> getAllItems()
    {
        return allItems;
    }

    public void setAllItems(List<Item> allItems)
    {
        this.allItems = new ArrayList<>(allItems);
    }

    public int getGameScore()
    {
        return gameScore;
    }

    public void addGameScore(int score)
    {
        gameScore += score;
    }
}
