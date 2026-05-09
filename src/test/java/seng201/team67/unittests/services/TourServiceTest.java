package seng201.team67.unittests.services;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.Label;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.Minigame;
import seng201.team67.models.enums.TourType;
import seng201.team67.services.LabelService;
import seng201.team67.services.TourService;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TourServiceTest {

    @Test
    void stopOrderAndConcertFlagsCanBeManaged() {
        TourService service = createTourService(TourType.LOCAL, List.of(new Popstar("One", 1, "Pop")));

        service.setStopOrder(List.of(2, 1, 3));
        service.setConcertFinished();

        assertTrue(service.hasStopOrder());
        assertEquals(List.of(2, 1, 3), service.getStopOrder());
        assertTrue(service.getConcertStatus());

        service.resetConcertFinished();

        assertFalse(service.getConcertStatus());
    }

    @Test
    void tourEndedAddsEarnedCreditsToLabel() {
        TourService service = createTourService(TourType.LOCAL, List.of(new Popstar("One", 1, "Pop")));
        double startingMoney = serviceEnvironment(service).getLabelService().getMoney();

        service.addCreditsEarned(125.5);
        service.tourEnded();

        assertEquals(startingMoney + 125.5, serviceEnvironment(service).getLabelService().getMoney(), 0.0001);
    }

    @Test
    void tourEndedResetsLineupStaminaToBaseAmounts() {
        Artist artistOne = new Popstar("One", 1, "Pop");
        Artist artistTwo = new Rapper("Two", 2, "Rap");
        artistOne.setStamina(0);
        artistTwo.setStamina(1);
        TourService service = createTourService(TourType.LOCAL, List.of(artistOne, artistTwo));

        service.tourEnded();

        assertEquals(artistOne.getBaseStamina(), artistOne.getStamina());
        assertEquals(artistTwo.getBaseStamina(), artistTwo.getStamina());
    }

    @Test
    void concertResultsCanBeAddedThroughService() {
        TourService service = createTourService(TourType.LOCAL, List.of(new Popstar("One", 1, "Pop")));
        ConcertResults result = new ConcertResults(100.0, 10.0, 5.0, 75, 30.0, 80.0);

        service.addConcertResult(result);

        assertEquals(1, service.getConcertResults().size());
        assertEquals(result, service.getConcertResults().getFirst());
    }

    @Test
    void rollMiniGameTriggerReturnsEligibleMinigameWhenChanceIsGuaranteed() throws Exception {
        TourService service = createTourService(TourType.LOCAL, List.of(new Popstar("One", 1, "Pop")));
        setMiniGameTriggerChance(1.0);

        Minigame minigame = service.rollMiniGameTrigger(50);

        assertEquals(Minigame.SOUNDENGINEER, minigame);
    }

    @Test
    void rollMiniGameTriggerReturnsNullWhenCrowdMeterHasNoEligibleGames() throws Exception {
        TourService service = createTourService(TourType.LOCAL, List.of(new Popstar("One", 1, "Pop")));
        setMiniGameTriggerChance(1.0);

        assertNull(service.rollMiniGameTrigger(5));
    }

    @Test
    void lineupExhaustionDetectsWhenAllArtistsHaveNoStamina() {
        Artist artistOne = new Popstar("One", 1, "Pop");
        Artist artistTwo = new Rapper("Two", 2, "Rap");
        artistOne.setStamina(0);
        artistTwo.setStamina(0);
        TourService service = createTourService(TourType.LOCAL, List.of(artistOne, artistTwo));

        assertTrue(service.isLineupExhausted());
    }

    @Test
    void endTourDueToExhaustionFlagsTourAndAppliesRefundPenalty() {
        TourService service = createTourService(TourType.COUNTRY, List.of(new Popstar("One", 1, "Pop")));
        service.addCreditsEarned(500.0);
        service.increaseStopIndex();
        service.increaseStopIndex();

        service.endTourDueToExhaustion();

        assertTrue(service.isEndedByExhaustion());
        assertEquals(4 * serviceEnvironment(service).getConfig().cancelTourPenalty, service.getExhaustionRefund(), 0.0001);
        assertEquals(500.0 - service.getExhaustionRefund(), service.getCreditsEarned(), 0.0001);
    }

    private TourService createTourService(TourType tourType, List<Artist> artists) {
        GameEnvironment gameEnvironment = new GameEnvironment();
        gameEnvironment.setDifficulty(0);
        LabelService labelService = new LabelService(gameEnvironment);
        labelService.setLabel(new Label("Test Label", artists, gameEnvironment));

        try {
            Field field = GameEnvironment.class.getDeclaredField("labelService");
            field.setAccessible(true);
            field.set(gameEnvironment, labelService);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return new TourService(new Tour(tourType), gameEnvironment);
    }

    private GameEnvironment serviceEnvironment(TourService service) {
        try {
            Field field = TourService.class.getDeclaredField("gameEnvironment");
            field.setAccessible(true);
            return (GameEnvironment) field.get(service);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private void setMiniGameTriggerChance(double value) throws Exception {
        Field field = TourService.class.getDeclaredField("miniGameTriggerChance");
        field.setAccessible(true);
        field.set(null, value);
    }
}
