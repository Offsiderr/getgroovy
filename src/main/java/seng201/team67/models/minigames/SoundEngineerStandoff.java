package seng201.team67.models.minigames;

import seng201.team67.interfaces.Minigame;

import java.util.List;

/**
 * Represents the sound engineer standoff used by the game.
 * @author Louie Campion
 * @author Keenan Aubrey
 */
public class SoundEngineerStandoff implements Minigame {

    /**
     * Processes the count matches.
     * @param player the list of player
     * @param target the list of target
     * @param tolerance the numeric value for the tolerance
     * @return The count matches.
     */
    public long countMatches(List<Double> player, List<Double> target, double tolerance) {
        long count = 0;
        for (int i = 0; i < player.size(); i++) {
            if (Math.abs(player.get(i) - target.get(i)) <= tolerance) count++;
        }
        return count;
    }
}
