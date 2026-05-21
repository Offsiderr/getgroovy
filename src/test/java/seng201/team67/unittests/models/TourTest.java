package seng201.team67.unittests.models;

import org.junit.jupiter.api.Test;
import seng201.team67.models.ConcertResults;
import seng201.team67.models.Tour;
import seng201.team67.models.enums.TourType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TourTest {

    @Test
    void constructorStoresTourTypeAndStartsIncomplete() {
        Tour tour = new Tour(TourType.LOCAL);

        assertEquals(TourType.LOCAL, tour.type);
        assertFalse(tour.isComplete());
        assertFalse(tour.hasStopOrder());
    }

    @Test
    void addStaminaAccumulatesTotalStaminaDrained() {
        Tour tour = new Tour(TourType.COUNTRY);

        tour.addStamina(5.5);
        tour.addStamina(2.0);

        assertEquals(7.5, tour.getTotalStaminaDrained());
    }

    @Test
    void stopOrderCanBeSetAndRetrieved() {
        Tour tour = new Tour(TourType.WORLD);
        List<Integer> stopOrder = List.of(3, 1, 2);

        tour.setStopOrder(stopOrder);

        assertTrue(tour.hasStopOrder());
        assertIterableEquals(stopOrder, tour.getStopOrder());
    }

    @Test
    void isCompleteBecomesTrueWhenCurrentStopReachesTourLength() {
        Tour tour = new Tour(TourType.LOCAL);

        tour.currentStopIndex = TourType.LOCAL.getStops();

        assertTrue(tour.isComplete());
    }

    @Test
    void addCreditsEarnedAccumulatesCredits() {
        Tour tour = new Tour(TourType.COUNTRY);

        tour.addCreditsEarned(120.5);
        tour.addCreditsEarned(79.5);

        assertEquals(200.0, tour.getCreditsEarned());
    }

    @Test
    void concertResultsCanBeStoredOnTour() {
        Tour tour = new Tour(TourType.LOCAL);
        ConcertResults result = new ConcertResults(100.0, 25.0, 10.0, 80, 30.0, 95.0);

        tour.addConcertResult(result);

        assertEquals(1, tour.getConcertResults().size());
        assertEquals(result, tour.getConcertResults().getFirst());
    }

    @Test
    void payMultiplierReflectsTourType() {
        Tour tour = new Tour(TourType.WORLD);

        assertEquals(TourType.WORLD.getPayMultiplier(), tour.getPayMultiplier());
    }

    @Test
    void exhaustionFlagsAndRefundCanBeUpdated() {
        Tour tour = new Tour(TourType.COUNTRY);

        tour.setEndedByExhaustion(true);
        tour.setExhaustionRefund(87.5);
        tour.setCancellationRefund(25.0);

        assertTrue(tour.isEndedByExhaustion());
        assertEquals(87.5, tour.getExhaustionRefund());
        assertEquals(25.0, tour.getCancellationRefund());
    }
}
