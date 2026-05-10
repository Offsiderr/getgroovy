package seng201.team67.interfaces;

import seng201.team67.models.artists.Artist;

@FunctionalInterface
public interface PayoutModifier {
    int apply(Artist artist, int basePayout);
}
