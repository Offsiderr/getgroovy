package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.GameConfig;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.enums.RandomEvent;
import seng201.team67.services.gameplay.RandomEventService;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomEventServiceTest {

    private final RandomEventService service = new RandomEventService();

    @Test
    void triggerChanceUsesConfigThreshold() {
        GameConfig config = GameConfig.aChallenge();

        assertTrue(service.shouldTriggerRandomEvent(config, 0.29));
        assertFalse(service.shouldTriggerRandomEvent(config, 0.30));
    }

    @Test
    void postTourEventUsesCurrentPlaceholderEvent() {
        RandomEvent weightedEvent = new RandomEventService(new Random(0)).getWeightedRandomEvent();
        assertNotNull(weightedEvent);
    }

    @Test
    void randomAffectedArtistIsSelectedFromAvailableArtists() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        Artist firstArtist = gameEnvironment.getArtistPool().get(0);
        Artist secondArtist = gameEnvironment.getArtistPool().get(1);
        gameEnvironment.setLabel(new Label("Test Label", List.of(firstArtist, secondArtist), gameEnvironment));

        RandomEventService seededService = new RandomEventService(new Random(0));
        Artist affectedArtist = seededService.getRandomAffectedArtist(gameEnvironment);
        RandomEvent randomEvent = seededService.getWeightedRandomEvent();

        assertNotNull(affectedArtist);
        assertTrue(gameEnvironment.getLabelService().getAllArtists().contains(affectedArtist));
        assertNotNull(randomEvent);
    }

    @Test
    void randomAffectedArtistIsNullWhenNoArtistsProvided() {
        assertNull(service.getRandomAffectedArtist(List.of()));
    }
    
    @Test
    void starPowerEventChangesAffectedArtistStarPower() {
        Artist artist = new seng201.team67.models.artists.Popstar("Test Pop", 3, "Pop");

        boolean changed = service.applyRandomEvent(new GameEnvironment(), RandomEvent.VIRAL_MOMENT, artist);

        assertTrue(changed);
        assertEquals(5, artist.getStarPower());
    }

    @Test
    void starPowerEventSkipsArtistsAtMinimumAndMaximumStarPower() {
        Artist minArtist = new seng201.team67.models.artists.Popstar("Min Pop", 1, "Pop");
        Artist midArtist = new seng201.team67.models.artists.Rapper("Mid Rap", 3, "Rap");
        Artist maxArtist = new seng201.team67.models.artists.Rockstar("Max Rock", 5, "Rock");

        RandomEventService seededService = new RandomEventService(new Random(0));
        Artist affectedArtist = seededService.getRandomAffectedArtist(RandomEvent.VIRAL_MOMENT,
                List.of(minArtist, midArtist, maxArtist));

        assertEquals(midArtist, affectedArtist);
    }

    @Test
    void starPowerEventIsIneligibleWhenNoArtistCanGainOrLoseStarPower() {
        Artist minArtist = new seng201.team67.models.artists.Popstar("Min Pop", 1, "Pop");
        Artist maxArtist = new seng201.team67.models.artists.Rockstar("Max Rock", 5, "Rock");

        RandomEvent weightedEvent = new RandomEventService(new Random(0))
                .getWeightedRandomEvent(List.of(minArtist, maxArtist));

        assertNotNull(weightedEvent);
        assertFalse("star_power".equals(weightedEvent.getStat()));
    }

    @Test
    void skillEventChangesAffectedArtistSkillLevel() {
        Artist artist = new seng201.team67.models.artists.Rapper("Test Rap", 2, "Rap");

        boolean changed = service.applyRandomEvent(new GameEnvironment(), RandomEvent.FAN_INSPIRATION, artist);

        assertTrue(changed);
        assertEquals(3, artist.getSkillLevel());
    }

    @Test
    void retirementEventPaysOutAndRemovesArtistWhenPossible() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        Artist retiringArtist = new seng201.team67.models.artists.Popstar("Retiring Artist", 2, "Pop");
        Artist remainingArtist = new seng201.team67.models.artists.Rapper("Remaining Artist", 2, "Rap");
        gameEnvironment.setLabel(new Label("Test Label", List.of(retiringArtist, remainingArtist), gameEnvironment));
        double startingMoney = gameEnvironment.getLabelService().getMoney();

        boolean changed = service.applyRandomEvent(gameEnvironment, RandomEvent.AMICABLE_EXIT, retiringArtist);

        assertTrue(changed);
        assertFalse(gameEnvironment.getLabelService().getAllArtists().contains(retiringArtist));
        assertEquals(startingMoney + RandomEvent.AMICABLE_EXIT.getValue(), gameEnvironment.getLabelService().getMoney(), 0.0001);
    }

    @Test
    void retirementEventFailsWhenArtistIsLastRemainingRosterMember() {
        GameEnvironment gameEnvironment = new GameEnvironment();
        Artist soloArtist = new seng201.team67.models.artists.Popstar("Solo Artist", 2, "Pop");
        gameEnvironment.setLabel(new Label("Test Label", List.of(soloArtist), gameEnvironment));
        double startingMoney = gameEnvironment.getLabelService().getMoney();

        boolean changed = service.applyRandomEvent(gameEnvironment, RandomEvent.AMICABLE_EXIT, soloArtist);

        assertFalse(changed);
        assertTrue(gameEnvironment.getLabelService().getAllArtists().contains(soloArtist));
        assertEquals(startingMoney, gameEnvironment.getLabelService().getMoney(), 0.0001);
    }
}
