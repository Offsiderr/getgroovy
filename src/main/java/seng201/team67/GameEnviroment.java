package seng201.team67;

import seng201.team67.models.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.models.enums.Rarity;
import seng201.team67.services.ArtistLoaderService;
import seng201.team67.services.LabelService;

import java.util.*;

public class GameEnviroment {

    //This is the main hub, this class wires everything together.

    //UI variables

    //Game variables
    private Label label;
    private String tempName;

    private double money;
    private int currentTour;
    private int totalTours; //Selected amount of tours
    private int tourCount = 1; //tours so far
    private Difficulty difficulty;

    private LabelService labelService;

    //All artists loaded into the game. Not artists in the label.
    private ArrayList<Artist> artistPool;

    //the studio and the market use these arrays
    private ArrayList<Artist> artistPurchasePool;


    public GameEnviroment()
    {
        //Load our artists
        List<Artist> allArtists = new ArtistLoaderService().loadAll();
        this.artistPool = new ArrayList<>(allArtists);
        Collections.shuffle(this.artistPool);

        //just testing for now
        for (Artist artist : this.getArtistPool()) {
            System.out.println(artist.getName() + " | " + artist.getClass().getSimpleName() + " | SP: " + artist.getStar_power());
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

        }
        if(difType == 1)
        {
            difficulty = Difficulty.A_CHALLENGE;

        }
        if(difType == 2)
        {
            difficulty = Difficulty.HEARTLESS;
        }

        money = difficulty.getStartingCredits();

        System.out.println(difficulty);
        System.out.println(money);

    }

    public void setTotalTours(int totalTours)
    {
        this.totalTours = totalTours;
        System.out.println(totalTours);
    }

    public void setArtistPool(List<Artist> artistList)
    {
        artistPool.clear();
        artistPool.addAll(artistList);
    }

    public void createLabel(List<Artist> selectedArtists)
    {
        labelService = new LabelService();
        labelService.setLabel(new Label(tempName, selectedArtists));
    }

    public ArrayList<Artist> resetArtistPurchasePool()
    {
        //Clear and regenerate the purchase pool. For each artist pick it selects a random rarity

        artistPurchasePool.clear();

        ArrayList<Rarity> rarities = new ArrayList<>(Arrays.asList(Rarity.COMMON, Rarity.RARE, Rarity.VERY_RARE));

        ArrayList<Artist> picked = new ArrayList<>();
        for (int i = 0; i < rarities.size(); i++)
        {
            int selected_starpower = rarities.get(new Random().nextInt(3)).get_starpower();
            int z = i;
            while (artistPool.get(z).owned && artistPool.get(z).getStar_power() != selected_starpower)
            {
                z += 1;
            }
            picked.add(artistPool.get(z));
        }
        artistPurchasePool.addAll(picked);
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

    public int getTotalTours()
    {
        return totalTours;
    }

    public ArrayList<Artist> getArtistPurchasePool(){return artistPool;}


}
