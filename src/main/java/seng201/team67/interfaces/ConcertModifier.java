package seng201.team67.interfaces;

import seng201.team67.services.gameplay.ConcertService;

@FunctionalInterface
public interface ConcertModifier {
    void apply(ConcertService concertService);
}
