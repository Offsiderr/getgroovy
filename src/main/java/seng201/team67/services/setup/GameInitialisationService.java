package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.items.Item;
import seng201.team67.services.data.ArtistLoaderService;
import seng201.team67.services.data.ItemLoaderService;
import seng201.team67.services.data.QuestionLoaderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameInitialisationService {

    public void initialise(GameEnvironment gameEnvironment)
    {
        List<Artist> allArtists = new ArtistLoaderService().loadAll();
        ArrayList<Artist> artistPool = new ArrayList<>(allArtists);
        Collections.shuffle(artistPool);
        gameEnvironment.setArtistPool(artistPool);

        //List<Item> allItems = new ItemLoaderService().loadAll();
        //gameEnvironment.setAllItems(allItems);

        QuestionLoaderService questionLoaderService = new QuestionLoaderService();
        gameEnvironment.setCommonQuestionPool(questionLoaderService.loadEventPool("common"));
        gameEnvironment.setLocalQuestionPool(questionLoaderService.loadEventPool("local"));
        gameEnvironment.setCountryQuestionPool(questionLoaderService.loadEventPool("country"));
        gameEnvironment.setWorldQuestionPool(questionLoaderService.loadEventPool("world"));
    }
}
