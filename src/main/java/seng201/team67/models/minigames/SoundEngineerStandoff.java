package seng201.team67.models.minigames;

import seng201.team67.interfaces.Minigame;

import java.util.List;

public class SoundEngineerStandoff implements Minigame {

    public long countMatches(List<Double> player, List<Double> target, double tolerance) {
        long count = 0;
        for (int i = 0; i < player.size(); i++) {
            if (Math.abs(player.get(i) - target.get(i)) <= tolerance) count++;
        }
        return count;
    }
}
