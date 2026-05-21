package seng201.team67.interfaces;

import seng201.team67.models.artists.Artist;

/**
 * Defines a modifier for stat behaviour.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
@FunctionalInterface
public interface StatModifier {
    /**
     * Processes applying a stat modifier.
     * @param artist the artist
     * @param value the numeric value for the value
     * @return The apply.
     */
    int apply(Artist artist, double value);
}
