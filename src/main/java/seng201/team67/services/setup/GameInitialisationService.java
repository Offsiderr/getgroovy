package seng201.team67.services.setup;

import seng201.team67.GameEnvironment;
import seng201.team67.models.Skill;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.items.Item;
import seng201.team67.services.data.ArtistLoaderService;
import seng201.team67.services.data.ItemLoaderService;
import seng201.team67.services.data.QuestionLoaderService;
import seng201.team67.services.data.SkillLoaderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameInitialisationService {

    private static final Random random = new Random();

    public void initialise(GameEnvironment gameEnvironment)
    {
        List<Artist> allArtists = new ArtistLoaderService().loadAll();

        SkillLoaderService skillLoaderService = new SkillLoaderService();
        for (Artist artist : allArtists)
        {
            artist.setSkill(pickRandom(skillLoaderService.getEligibleSkills(artist)));
        }

        ArrayList<Artist> artistPool = new ArrayList<>(allArtists);
        Collections.shuffle(artistPool);
        gameEnvironment.setArtistPool(artistPool);

        List<Item> allItems = new ItemLoaderService().loadAll();
        gameEnvironment.setAllItems(allItems);

        QuestionLoaderService questionLoaderService = new QuestionLoaderService();
        gameEnvironment.setCommonQuestionPool(questionLoaderService.loadEventPool("common"));
        gameEnvironment.setLocalQuestionPool(questionLoaderService.loadEventPool("local"));
        gameEnvironment.setCountryQuestionPool(questionLoaderService.loadEventPool("country"));
        gameEnvironment.setWorldQuestionPool(questionLoaderService.loadEventPool("world"));

    }


    private Skill pickRandom(List<Skill> array) {
        if (array == null || array.size() == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        return array.get(random.nextInt(array.size()));
    }
}
