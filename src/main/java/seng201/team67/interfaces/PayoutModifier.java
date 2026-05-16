package seng201.team67.interfaces;

import seng201.team67.models.artists.Artist;
import seng201.team67.models.questionmodels.Outcome;

import java.util.List;

@FunctionalInterface
public interface PayoutModifier {
    int apply(Artist artist, int basePayout, Outcome outcome, List<Artist> lineup,
              int crowdEnergy, int completedConcerts, int eventNumber, int totalEvents);
}
