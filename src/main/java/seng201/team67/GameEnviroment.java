package seng201.team67;

import seng201.team67.models.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.enums.Difficulty;
import seng201.team67.services.ArtistLoaderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameEnviroment {

    //This is the main hub, this class wires everything together.

    //UI variables

    //Game variables
    private Label label;
    //Temp label name. This is because the artist selection is on another scene.
    private String tempName;

    private int money;
    private int currentTour;
    private int totalTours;
    private Difficulty difficulty;

    //Artist Variables
    private ArrayList<Artist> artistPool;


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
        System.out.println(tempName);
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

    //getters

    public ArrayList<Artist> getArtistPool(){return artistPool;}
}
