package seng201.team67;

import seng201.team67.models.Artist;
import seng201.team67.models.GameConfig;
import seng201.team67.models.Label;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.models.enums.PayoutTier;
import seng201.team67.models.enums.PayoutType;
import seng201.team67.models.enums.Rarity;
import seng201.team67.models.questionmodels.Question;
import seng201.team67.services.ArtistLoaderService;
import seng201.team67.services.LabelService;
import seng201.team67.services.QuestionLoaderService;

import java.util.*;

public class GameEnvironment {

    //This is the main hub, this class wires everything together.

    //UI variables

    //Game variables
    private Label label;
    private String tempName;

    private int currentTour;
    private int selectedNumTours; //Selected amount of tours
    private int tourCount = 0; //tours so far
    private int concertCount = 0;
    private Difficulty difficulty;

    private LabelService labelService;

    //All artists loaded into the game. Not artists in the label.
    private ArrayList<Artist> artistPool;
    //the studio and the market use these arrays
    private ArrayList<Artist> artistPurchasePool = new ArrayList<>();
    private boolean poolGenerated = false;

    private GameConfig gameConfig;

    private PayoutTier payoutTier;

    //question pools
    private ArrayList<Question> commonQuestionPool;
    private ArrayList<Question> localQuestionPool;
    private ArrayList<Question> countryQuestionPool;
    private ArrayList<Question> worldQuestionPool;

    public GameEnvironment()
    {
        //Load our artists
        List<Artist> allArtists = new ArtistLoaderService().loadAll();
        this.artistPool = new ArrayList<>(allArtists);
        Collections.shuffle(this.artistPool);

        //just testing for now
        for (Artist artist : this.getArtistPool()) {
            System.out.println(artist.getName() + " | " + artist.getClass().getSimpleName() + " | SP: " + artist.getStarPower());
        }

        this.commonQuestionPool = new ArrayList<>(new QuestionLoaderService().loadEventPool("common"));
        this.localQuestionPool = new ArrayList<>(new QuestionLoaderService().loadEventPool("local"));
        this.countryQuestionPool = new ArrayList<>(new QuestionLoaderService().loadEventPool("country"));
        this.worldQuestionPool = new ArrayList<>(new QuestionLoaderService().loadEventPool("world"));


        for (Question question : commonQuestionPool)
        {
            System.out.println(question.getPrompt() +  " " + question.getAnswers().get(0).getLabel());
        }


    }


    //These are setters called in the set up UI.

    public void setLabelName(String name)
    {
        tempName = name;
    }

    public void setDifficulty(int difType)
    {
        //Gets the difficulty as well as sets the starting money.
        if(difType == 0)
        {
            difficulty = Difficulty.EASY;
            this.gameConfig = GameConfig.easy();

        }
        if(difType == 1)
        {
            difficulty = Difficulty.ACHALLENGE;
            this.gameConfig = GameConfig.aChallenge();

        }
        if(difType == 2)
        {
            difficulty = Difficulty.HEARTLESS;
            this.gameConfig = GameConfig.hard();

        }



        System.out.println(difficulty);

        switch (difficulty)
        {
            case EASY:
                payoutTier = PayoutTier.EASY;
                break;
            case ACHALLENGE:
                payoutTier = PayoutTier.MEDIUM;
                break;
            case HEARTLESS:
                payoutTier = PayoutTier.HARD;
                break;
        }

    }

    public void setSelectedNumTours(int selectedNumTours)
    {
        this.selectedNumTours = selectedNumTours;
        System.out.println(selectedNumTours);
    }

    public void setArtistPool(List<Artist> artistList)
    {
        artistPool.clear();
        artistPool.addAll(artistList);
    }

    public void createLabel(List<Artist> selectedArtists)
    {
        labelService = new LabelService(this);
        labelService.setLabel(new Label(tempName, selectedArtists, this));
    }

    public ArrayList<Artist> resetArtistPurchasePool()
    {
        //return the existing pool if it has been generated already.
        if (poolGenerated)
        {
            return artistPurchasePool;
        }

        artistPurchasePool.clear();

        ArrayList<Rarity> rarities = new ArrayList<>(Arrays.asList(Rarity.COMMON, Rarity.RARE, Rarity.VERY_RARE));
        Collections.shuffle(rarities);

        for (Rarity rarity : rarities)
        {
            ArrayList<Artist> candidates = new ArrayList<>();
            for (Artist artist : artistPool)
            {
                if (!artist.owned && artist.getStarPower() == rarity.get_starpower())
                {
                    candidates.add(artist);
                }
            }

            if (!candidates.isEmpty())
            {
                artistPurchasePool.add(candidates.get(new Random().nextInt(candidates.size())));
            }
        }

        poolGenerated = true;
        return artistPurchasePool;
    }

    //getters

    public ArrayList<Artist> getArtistPool(){return artistPool;}


    public Difficulty getDifficulty()
    {
        return difficulty;
    }

    public LabelService getLabelService()
    {
        return labelService;
    }

    public int getTourCount()
    {
        return tourCount;
    }

    public int getSelectedNumTours()
    {
        return selectedNumTours;
    }

    public ArrayList<Artist> getArtistPurchasePool(){return artistPool;}

    public void setPoolGenerated(Boolean poolGenerated)
    {
        this.poolGenerated = poolGenerated;
    }

    public GameConfig getConfig()
    {
        if(gameConfig == null)
        {
            //fallback
            return GameConfig.easy();
        }
        return gameConfig;
    }

    public int getConcertCount()
    {
        return concertCount;
    }

    public void increaseConcertCount()
    {
        concertCount += 1;
    }

    public boolean checkGameStatus()
    {
        boolean noArtists = labelService.getAllArtists().isEmpty();
        if (!noArtists)
        {
            return false;
        }

        return !canAffordArtistRecovery();
    }

    private boolean canAffordArtistRecovery()
    {
        double money = labelService.getMoney();
        boolean hasAvailableArtist = artistPool.stream().anyMatch(artist -> !artist.owned);

        if (!hasAvailableArtist)
        {
            return false;
        }

        boolean canAffordHire = artistPool.stream()
                .filter(artist -> !artist.owned)
                .anyMatch(artist -> artist.getCost() <= money);

        boolean canAffordGacha = money >= getConfig().gachaStandardCost
                || money >= getConfig().gachaGoldenCost
                || money >= getConfig().gachaPlatinumCost;

        return canAffordHire || canAffordGacha;
    }

    public Question getQuestion(String type)
    { //TODO: potential bug here:
        switch (type)
            {
                case "common":
                    return commonQuestionPool.get((int) (Math.random() * (commonQuestionPool.size())));
                case "local":
                    return localQuestionPool.get((int) (Math.random() * (localQuestionPool.size())));
                case "country":
                    return countryQuestionPool.get((int) (Math.random() * (countryQuestionPool.size())));
                case "world":
                    return worldQuestionPool.get((int) (Math.random() * (worldQuestionPool.size())));
            }

        return null; //TODO: add exception handling for this
    }

    public void increaseTours()
    {
        tourCount += 1;
    }

    public Double getPayoutAmount(PayoutType payoutType)
    {
        return payoutTier.resolve(payoutType);
    }
}
