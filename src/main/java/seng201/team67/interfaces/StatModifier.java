package seng201.team67.interfaces;

import seng201.team67.models.artists.Artist;

@FunctionalInterface
public interface StatModifier {
    int apply(Artist artist);
}
