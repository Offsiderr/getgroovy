package seng201.team67.interfaces;

import seng201.team67.models.artists.Artist;
import seng201.team67.models.questionmodels.Outcome;

import java.util.List;

/**
 * Defines a modifier for payout behaviour.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
@FunctionalInterface
public interface PayoutModifier {
    /**
     * Processes applying a payout modifier.
     * @param artist the artist
     * @param basePayout the numeric value for the base payout
     * @param outcome the outcome to apply
     * @param lineup the current artist lineup
     * @param crowdEnergy the numeric value for the crowd energy
     * @param completedConcerts the numeric value for the completed concerts
     * @param eventNumber the numeric value for the event number
     * @param totalEvents the numeric value for the total events
     * @return The apply.
     */
    int apply(Artist artist, int basePayout, Outcome outcome, List<Artist> lineup,
              int crowdEnergy, int completedConcerts, int eventNumber, int totalEvents);
}
