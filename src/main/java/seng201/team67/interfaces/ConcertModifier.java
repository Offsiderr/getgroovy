package seng201.team67.interfaces;

import seng201.team67.services.gameplay.ConcertService;

/**
 * Defines a modifier for concert behaviour.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
@FunctionalInterface
public interface ConcertModifier {
    /**
     * Processes applying a concert modifier.
     * @param concertService the concert service to use
     */
    void apply(ConcertService concertService);
}
