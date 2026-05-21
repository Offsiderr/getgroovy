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

/**
 * Provides game initialisation operations for the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class GameInitialisationService {

    /** The random. */
    private static final Random random = new Random();

    /**
     * Initialises the game by loading in all the skills, artists, items and questions.
     * It updates related state as needed while performing the operation.
     * @param gameEnvironment the active game environment
     */
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

    /**
     * Picks a random skill. Used for the artist's random assignment of skills at startup.
     * @param array
     * @return Random Skill
     */
    private Skill pickRandom(List<Skill> array) {
        if (array == null || array.size() == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        return array.get(random.nextInt(array.size()));
    }
}
