package seng201.team67;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import seng201.team67.models.GameConfig;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.models.enums.questions.PayoutTier;
import seng201.team67.models.enums.questions.StaminaTier;
import seng201.team67.models.items.Item;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.audio.MusicService;
import seng201.team67.services.data.SkillLoaderService;
import seng201.team67.services.management.LabelService;
import seng201.team67.services.setup.GameInitialisationService;
import seng201.team67.services.setup.SetupService;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the shared game environment state used across the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameEnvironment {

    /** The label. */
    private Label label;
    /** Text value for the temp name. */
    private String tempName;

    /** Numeric value for the current tour. */
    private int currentTour;
    /** Numeric value for the selected num tours. */
    private int selectedNumTours;
    /** Numeric value for the tour count. */
    private int tourCount = 0;
    /** Numeric value for the concert count. */
    private int concertCount = 0;
    /** The difficulty. */
    private Difficulty difficulty;
    /** Numeric value for the game score. */
    private int gameScore = 0;
    /** Numeric value for the total money spent. */
    private double totalMoneySpent = 0;
    /** Numeric value for the total money earnt. */
    private double totalMoneyEarnt = 0;

    /** Service used to manage music behaviour. */
    private final MusicService musicService;

    /** Collection that stores the artist pool. */
    private ArrayList<Artist> artistPool = new ArrayList<>();
    /** Collection that stores the artist purchase pool. */
    private ArrayList<Artist> artistPurchasePool = new ArrayList<>();
    /** Whether artist pool generated. */
    private boolean artistPoolGenerated = false;

    /** Collection that stores the item purchase pool. */
    private ArrayList<Item> itemPurchasePool = new ArrayList<>();
    /** Whether item pool generated. */
    private boolean itemPoolGenerated = false;

    /** The game config. */
    private GameConfig gameConfig;
    /** The payout tier. */
    private PayoutTier payoutTier;
    /** The stamina tier. */
    private StaminaTier staminaTier;

    /** Collection that stores the common question pool. */
    private ArrayList<Question> commonQuestionPool = new ArrayList<>();
    /** Collection that stores the local question pool. */
    private ArrayList<Question> localQuestionPool = new ArrayList<>();
    /** Collection that stores the country question pool. */
    private ArrayList<Question> countryQuestionPool = new ArrayList<>();
    /** Collection that stores the world question pool. */
    private ArrayList<Question> worldQuestionPool = new ArrayList<>();

    /** Collection that stores the all items. */
    private ArrayList<Item> allItems = new ArrayList<>();

    /** Service used to manage skill loader behaviour. */
    private SkillLoaderService skillLoaderService;

    /**
     * Creates a new game environment.
     */
    public GameEnvironment()
    {
        this.musicService = new MusicService(this);
        this.skillLoaderService = new SkillLoaderService();
        new GameInitialisationService().initialise(this);
    }

    /**
     * Sets the label name.
     * @param name the name value to use
     */
    public void setLabelName(String name)
    {
        tempName = new SetupService(this).requireValidLabelName(name);
    }

    /**
     * Returns the temp name.
     * @return The temp name.
     */
    public String getTempName()
    {
        return tempName;
    }

    /**
     * Sets the difficulty.
     * @param difficulty the difficulty
     */
    public void setDifficulty(Difficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    /**
     * Returns the difficulty.
     * @return The difficulty.
     */
    public Difficulty getDifficulty()
    {
        return difficulty;
    }

    /**
     * Sets the selected num tours.
     * @param selectedNumTours the numeric value for the selected num tours
     */
    public void setSelectedNumTours(int selectedNumTours)
    {
        this.selectedNumTours = selectedNumTours;
    }

    /**
     * Returns the selected num tours.
     * @return The selected num tours.
     */
    public int getSelectedNumTours()
    {
        return selectedNumTours;
    }

    /**
     * Sets the artist pool.
     * @param artistList the list of artist list
     */
    public void setArtistPool(List<Artist> artistList)
    {
        artistPool.clear();
        artistPool.addAll(artistList);
    }

    /**
     * Returns the artist pool.
     * @return The artist pool.
     */
    public ArrayList<Artist> getArtistPool()
    {
        return artistPool;
    }

    /**
     * Returns the label.
     * @return The label.
     */
    public Label getLabel()
    {
        return label;
    }

    /**
     * Sets the label.
     * @param label the label
     */
    public void setLabel(Label label)
    {
        this.label = label;
    }

    /**
     * Returns the label service.
     * @return The label service.
     */
    @JsonIgnore
    public LabelService getLabelService()
    {
        return new LabelService(this);
    }

    /**
     * Returns the music service.
     * @return The music service.
     */
    @JsonIgnore
    public MusicService getMusicService()
    {
        return musicService;
    }

    /**
     * Returns the tour count.
     * @return The tour count.
     */
    public int getTourCount()
    {
        return tourCount;
    }

    /**
     * Increases the tours.
     */
    public void increaseTours()
    {
        tourCount += 1;
    }

    /**
     * Returns the concert count.
     * @return The concert count.
     */
    public int getConcertCount()
    {
        return concertCount;
    }

    /**
     * Increases the concert count.
     */
    public void increaseConcertCount()
    {
        concertCount += 1;
    }

    /**
     * Returns the artist purchase pool.
     * @return The artist purchase pool.
     */
    public ArrayList<Artist> getArtistPurchasePool()
    {
        return artistPurchasePool;
    }

    /**
     * Sets the artist purchase pool.
     * @param artistPurchasePool the list of artist purchase pool
     */
    public void setArtistPurchasePool(List<Artist> artistPurchasePool)
    {
        this.artistPurchasePool.clear();
        this.artistPurchasePool.addAll(artistPurchasePool);
    }

    /**
     * Removes the artist from purchase pool.
     * @param artist the artist
     */
    public void removeArtistFromPurchasePool(Artist artist)
    {
        artistPurchasePool.remove(artist);
    }

    /**
     * Returns whether artist pool generated.
     * @return True if artist pool generated, otherwise false.
     */
    public boolean isArtistPoolGenerated()
    {
        return artistPoolGenerated;
    }

    /**
     * Sets the artist pool generated.
     * @param artistPoolGenerated whether artist pool generated
     */
    public void setArtistPoolGenerated(boolean artistPoolGenerated)
    {
        this.artistPoolGenerated = artistPoolGenerated;
    }

    /**
     * Returns the item purchase pool.
     * @return The item purchase pool.
     */
    public ArrayList<Item> getItemPurchasePool()
    {
        return itemPurchasePool;
    }

    /**
     * Sets the item purchase pool.
     * @param itemPurchasePool the list of item purchase pool
     */
    public void setItemPurchasePool(List<Item> itemPurchasePool)
    {
        this.itemPurchasePool.clear();
        this.itemPurchasePool.addAll(itemPurchasePool);
    }

    /**
     * Removes the item from purchase pool.
     * @param item the item involved in the operation
     */
    public void removeItemFromPurchasePool(Item item)
    {
        itemPurchasePool.remove(item);
    }

    /**
     * Returns whether item pool generated.
     * @return True if item pool generated, otherwise false.
     */
    public boolean isItemPoolGenerated()
    {
        return itemPoolGenerated;
    }

    /**
     * Sets the item pool generated.
     * @param itemPoolGenerated whether item pool generated
     */
    public void setItemPoolGenerated(boolean itemPoolGenerated)
    {
        this.itemPoolGenerated = itemPoolGenerated;
    }

    /**
     * Returns the config.
     * @return The config.
     */
    @JsonProperty("config")
    public GameConfig getConfig()
    {
        if(gameConfig == null)
        {
            return GameConfig.easy();
        }
        return gameConfig;
    }

    /**
     * Sets the game config.
     * @param gameConfig the game config
     */
    @JsonProperty("config")
    public void setGameConfig(GameConfig gameConfig)
    {
        this.gameConfig = gameConfig;
    }

    /**
     * Returns the payout tier.
     * @return The payout tier.
     */
    public PayoutTier getPayoutTier()
    {
        return payoutTier;
    }

    /**
     * Sets the payout tier.
     * @param payoutTier the payout tier
     */
    public void setPayoutTier(PayoutTier payoutTier)
    {
        this.payoutTier = payoutTier;
    }

    /**
     * Returns the stamina tier.
     * @return The stamina tier.
     */
    public StaminaTier getStaminaTier()
    {
        return staminaTier == null ? StaminaTier.EASY : staminaTier;
    }

    /**
     * Sets the stamina tier.
     * @param staminaTier the stamina tier
     */
    public void setStaminaTier(StaminaTier staminaTier)
    {
        this.staminaTier = staminaTier;
    }

    /**
     * Returns the common question pool.
     * @return The common question pool.
     */
    public ArrayList<Question> getCommonQuestionPool()
    {
        return commonQuestionPool;
    }

    /**
     * Sets the common question pool.
     * @param commonQuestionPool the list of common questions
     */
    public void setCommonQuestionPool(List<Question> commonQuestionPool)
    {
        this.commonQuestionPool = new ArrayList<>(commonQuestionPool);
    }

    /**
     * Returns the local question pool.
     * @return The local question pool.
     */
    public ArrayList<Question> getLocalQuestionPool()
    {
        return localQuestionPool;
    }

    /**
     * Sets the local question pool.
     * @param localQuestionPool the list of local questions
     */
    public void setLocalQuestionPool(List<Question> localQuestionPool)
    {
        this.localQuestionPool = new ArrayList<>(localQuestionPool);
    }

    /**
     * Returns the country question pool.
     * @return The country question pool.
     */
    public ArrayList<Question> getCountryQuestionPool()
    {
        return countryQuestionPool;
    }

    /**
     * Sets the country question pool.
     * @param countryQuestionPool the list of country questions
     */
    public void setCountryQuestionPool(List<Question> countryQuestionPool)
    {
        this.countryQuestionPool = new ArrayList<>(countryQuestionPool);
    }

    /**
     * Returns the world question pool.
     * @return The world question pool.
     */
    public ArrayList<Question> getWorldQuestionPool()
    {
        return worldQuestionPool;
    }

    /**
     * Sets the world question pool.
     * @param worldQuestionPool the list of world questions
     */
    public void setWorldQuestionPool(List<Question> worldQuestionPool)
    {
        this.worldQuestionPool = new ArrayList<>(worldQuestionPool);
    }

    /**
     * Returns all items.
     * @return All items.
     */
    public ArrayList<Item> getAllItems()
    {
        return allItems;
    }

    /**
     * Sets all items.
     * @param allItems the list of all items
     */
    public void setAllItems(List<Item> allItems)
    {
        this.allItems = new ArrayList<>(allItems);
    }

    /**
     * Returns the game score.
     * @return The game score.
     */
    public int getGameScore()
    {
        return gameScore;
    }

    /**
     * Adds the game score.
     * @param score the numeric value for the score
     */
    public void addGameScore(int score)
    {
        gameScore += score;
    }

    /**
     * Returns the total money spent.
     * @return The total money spent.
     */
    public double getTotalMoneySpent()
    {
        return totalMoneySpent;
    }

    /**
     * Adds to the total money spent.
     * @param amount the amount to add
     */
    public void addTotalMoneySpent(double amount)
    {
        totalMoneySpent += amount;
    }

    /**
     * Returns the total money earnt.
     * @return The total money earnt.
     */
    public double getTotalMoneyEarnt()
    {
        return totalMoneyEarnt;
    }

    /**
     * Adds to the total money earnt.
     * @param amount the amount to add
     */
    public void addTotalMoneyEarnt(double amount)
    {
        totalMoneyEarnt += amount;
    }
}
