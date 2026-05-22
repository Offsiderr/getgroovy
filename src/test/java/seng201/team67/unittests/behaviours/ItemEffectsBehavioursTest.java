package seng201.team67.unittests.behaviours;

import org.junit.jupiter.api.Test;
import seng201.team67.GameEnvironment;
import seng201.team67.behaviours.ItemEffectBehaviours;
import seng201.team67.models.Label;
import seng201.team67.models.Tour;
import seng201.team67.models.artists.Artist;
import seng201.team67.models.artists.Popstar;
import seng201.team67.models.artists.Rapper;
import seng201.team67.models.artists.Rockstar;
import seng201.team67.models.enums.TourType;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.services.gameplay.ConcertService;
import seng201.team67.services.gameplay.TourService;
import seng201.team67.services.setup.DifficultyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemEffectsBehavioursTest {

    @Test
    void flatCrowdBoostAddsAmountToCrowd() {
        ConcertService service = createService(createLineup(2));
        service.setCrowdEnergyForDebug(10);

        ItemEffectBehaviours.flatCrowdBoost(15).apply(service);

        assertEquals(25, service.getCrowdEnergy());
    }

    @Test
    void crowdMultiplierScalesCrowdMeter() {
        ConcertService service = createService(createLineup(2));
        service.setCrowdEnergyForDebug(20);

        ItemEffectBehaviours.crowdMultiplier(1.5).apply(service);

        assertEquals(30, service.getCrowdEnergy());
    }

    @Test
    void crowdBoostIfWinStreakTriggersWhenRequiredStreakMet() {
        ConcertService service = createService(createLineup(2));
        service.setCrowdEnergyForDebug(25);
        service.setAnsweredQuestionCountForDebug(3);
        service.setWinStreakForDebug(3);
        service.setLastEventWonForDebug(true);

        ItemEffectBehaviours.crowdBoostIfWinStreak(3, 2.0).apply(service);

        assertEquals(50, service.getCrowdEnergy());
    }

    @Test
    void crowdBoostIfWinStreakDoesNothingWhenStreakTooLow() {
        ConcertService service = createService(createLineup(2));
        service.setCrowdEnergyForDebug(25);
        service.setAnsweredQuestionCountForDebug(2);
        service.setWinStreakForDebug(2);
        service.setLastEventWonForDebug(true);

        ItemEffectBehaviours.crowdBoostIfWinStreak(3, 2.0).apply(service);

        assertEquals(25, service.getCrowdEnergy());
    }

    @Test
    void crowdBoostIfStaminaBelowAddsCrowdWhenAnyArtistIsBelowThresholdAfterRequiredEvent() {
        List<Artist> lineup = createLineup(2);
        lineup.getFirst().setStamina(15);
        ConcertService service = createService(lineup);
        service.setCrowdEnergyForDebug(20);
        service.setAnsweredQuestionCountForDebug(4);

        ItemEffectBehaviours.crowdBoostIfStaminaBelowAfterEvent(20, 4, 15).apply(service);

        assertEquals(35, service.getCrowdEnergy());
    }

    @Test
    void crowdBoostIfStaminaBelowDoesNothingBeforeRequiredEvent() {
        List<Artist> lineup = createLineup(2);
        lineup.getFirst().setStamina(15);
        ConcertService service = createService(lineup);
        service.setCrowdEnergyForDebug(20);
        service.setAnsweredQuestionCountForDebug(3);

        ItemEffectBehaviours.crowdBoostIfStaminaBelowAfterEvent(20, 4, 15).apply(service);

        assertEquals(20, service.getCrowdEnergy());
    }

    @Test
    void crowdBoostIfLineupFullAddsCrowdWhenLineupMatchesExpectedSize() {
        ConcertService service = createService(createLineup(5));
        service.setCrowdEnergyForDebug(12);
        service.setAnsweredQuestionCountForDebug(2);

        ItemEffectBehaviours.crowdBoostIfLineupFull(5, 5).apply(service);

        assertEquals(17, service.getCrowdEnergy());
    }

    @Test
    void crowdAndStaminaBoostIfFirstEventWonBoostsCrowdAndAllArtistStamina() {
        List<Artist> lineup = createLineup(3);
        lineup.forEach(artist -> artist.setStamina(40));
        ConcertService service = createService(lineup);
        service.setCrowdEnergyForDebug(10);
        service.setAnsweredQuestionCountForDebug(1);
        service.setWinStreakForDebug(1);
        service.setLastEventWonForDebug(true);

        ItemEffectBehaviours.crowdAndStaminaBoostIfFirstEventWon(25, 10).apply(service);

        assertEquals(35, service.getCrowdEnergy());
        assertEquals(50, lineup.get(0).getStamina());
        assertEquals(50, lineup.get(1).getStamina());
        assertEquals(50, lineup.get(2).getStamina());
    }

    @Test
    void crowdAndStaminaBoostIfFirstEventWonDoesNothingForNonWinningOrLaterEvents() {
        List<Artist> lineup = createLineup(3);
        lineup.forEach(artist -> artist.setStamina(40));
        ConcertService service = createService(lineup);
        service.setCrowdEnergyForDebug(10);
        service.setAnsweredQuestionCountForDebug(2);
        service.setWinStreakForDebug(0);
        service.setLastEventWonForDebug(false);

        ItemEffectBehaviours.crowdAndStaminaBoostIfFirstEventWon(25, 10).apply(service);

        assertEquals(10, service.getCrowdEnergy());
        assertEquals(40, lineup.get(0).getStamina());
        assertEquals(40, lineup.get(1).getStamina());
        assertEquals(40, lineup.get(2).getStamina());
    }

    @Test
    void crowdBoostOnFinalEventAddsCrowdWhenEventMatchesTotalEvents() {
        ConcertService service = createService(createLineup(2));
        service.setCrowdEnergyForDebug(18);
        int totalEvents = service.getTotalConcertEvents();
        service.setAnsweredQuestionCountForDebug(totalEvents);

        ItemEffectBehaviours.crowdBoostOnFinalEvent(totalEvents, 50).apply(service);

        assertEquals(68, service.getCrowdEnergy());
    }

    @Test
    void incomeMultiplierIfCrowdAboveScalesIncomeMultiplierOnPenultimateEvent() {
        ConcertService service = createService(createLineup(2));
        service.setCrowdEnergyForDebug(80);
        service.setAnsweredQuestionCountForDebug(service.getTotalConcertEvents() - 1);

        ItemEffectBehaviours.incomeMultiplierIfCrowdAboveBeforeFinal(70, 2.0).apply(service);

        assertEquals(2.0, service.getCurrentIncomeMultiplier(), 0.0001);
    }

    @Test
    void incomeMultiplierIfEveryoneFullyEquippedScalesIncomeMultiplier() {
        List<Artist> lineup = createLineup(3);
        lineup.forEach(artist -> {
            artist.addItem(new EquippedItem());
            artist.addItem(new EquippedItem());
            artist.addItem(new EquippedItem());
        });
        ConcertService service = createService(lineup);
        service.setCrowdEnergyForDebug(30);
        service.setAnsweredQuestionCountForDebug(3);

        ItemEffectBehaviours.incomeMultiplierIfEveryoneFullyEquipped(3, 2.0).apply(service);

        assertEquals(2.0, service.getCurrentIncomeMultiplier(), 0.0001);
    }

    @Test
    void incomeMultiplierIfFinalEventWonScalesIncomeMultiplierAfterWinningFinalEvent() {
        ConcertService service = createService(createLineup(2));
        service.setCrowdEnergyForDebug(30);
        service.setAnsweredQuestionCountForDebug(service.getTotalConcertEvents());
        service.setWinStreakForDebug(1);
        service.setLastEventWonForDebug(true);

        ItemEffectBehaviours.incomeMultiplierIfFinalEventWon(2.0).apply(service);

        assertEquals(2.0, service.getCurrentIncomeMultiplier(), 0.0001);
    }

    @Test
    void autoWinNextEventRequestsForcedBestOutcome() {
        ConcertService service = createService(createLineup(2));
        service.setCrowdEnergyForDebug(15);
        service.setAnsweredQuestionCountForDebug(3);

        ItemEffectBehaviours.autoWinNextEventIfCrowdBelowAfterEvent(20, 3).apply(service);

        assertTrue(service.isForceBestOutcomeNextEventRequested());
    }

    @Test
    void restoreAllStaminaIfLowestBelowBoostsEntireLineup() {
        List<Artist> lineup = createLineup(3);
        lineup.getFirst().setStamina(20);
        lineup.get(1).setStamina(30);
        lineup.get(2).setStamina(40);
        ConcertService service = createService(lineup);
        service.setCrowdEnergyForDebug(30);
        service.setAnsweredQuestionCountForDebug(3);

        ItemEffectBehaviours.restoreAllStaminaIfLowestBelow(35, 25).apply(service);

        assertEquals(45, lineup.get(0).getCurrentStaminaValue());
        assertEquals(55, lineup.get(1).getCurrentStaminaValue());
        assertEquals(65, lineup.get(2).getCurrentStaminaValue());
    }

    @Test
    void finalEventHelperTracksCurrentServiceState() {
        ConcertService service = createService(createLineup(2));

        service.setAnsweredQuestionCountForDebug(service.getTotalConcertEvents() - 1);
        assertFalse(service.isFinalConcertEvent());

        service.setAnsweredQuestionCountForDebug(service.getTotalConcertEvents());
        assertTrue(service.isFinalConcertEvent());
    }

    private ConcertService createService(List<Artist> lineup) {
        GameEnvironment gameEnvironment = new GameEnvironment();
        new DifficultyService().applyDifficulty(gameEnvironment, 0);
        gameEnvironment.setLabelName("Test Label");
        gameEnvironment.setLabel(new Label("Test Label", lineup, gameEnvironment));
        return new ConcertService(gameEnvironment, new TourService(new Tour(TourType.LOCAL), gameEnvironment));
    }

    private List<Artist> createLineup(int size) {
        List<Artist> artists = List.of(
                new Popstar("Pop One", 1, "Pop"),
                new Rapper("Rap Two", 2, "Rap"),
                new Rockstar("Rock Three", 3, "Rock"),
                new Popstar("Pop Four", 1, "Pop"),
                new Rapper("Rap Five", 2, "Rap")
        );

        return artists.subList(0, size);
    }
}
